;; Copyright © 2016, JUXT LTD.

(ns example.ymir.alpha.web-server
  (:require
   [bidi.bidi :refer [tag]]
   [bidi.vhosts :refer [make-handler vhosts-model]]
   [example.ymir.alpha.frontend-routes :as frontend-routes]
   [clojure.tools.logging :refer :all]
   [com.stuartsierra.component :refer [Lifecycle using]]
   [clojure.java.io :as io]
   [hiccup.core :refer [html]]
   [selmer.parser :as selmer]
   [yada.resources.classpath-resource :refer [new-classpath-resource]]
   [yada.resources.file-resource :refer [new-directory-resource]]
   [yada.yada :refer [handler resource] :as yada]))

(defn render-frontend-file [ctx]
  (selmer/render-file "app.html" {:title "Ymir"
                                  :ctx ctx}))

(def frontend-resource
  (yada/resource
   {:id :example.ymir.alpha.resources/frontend
    :methods
    {:get
     {:produces #{"text/html"}
      :response (fn [ctx]
                  (render-frontend-file ctx))}}}))

(defn frontend-routes []
  ["/" (mapv #(identity [%1 frontend-resource]) (frontend-routes/urls))])

(defn content-routes []
  ["/"
    {"public/" (assoc (new-classpath-resource "public") :id :example.ymir.alpha.resources/static)}])

(defn healthz-routes []
  ["/healthz" (yada/resource
               {:id  :example.ymir.alpha.resources/healthz
                :methods
                {:get
                 {:produces #{"application/edn"}
                   :response (fn [ctx] {:status :running})}}})])

(defn routes
  "Create the URI route structure for our application."
  [db config]
  [""
   [(frontend-routes)
    (content-routes)
    (healthz-routes)

    ;; This is a backstop. Always produce a 404 if we ge there. This
    ;; ensures we never pass nil back to Aleph.
    [true (yada/resource {:produces "text/html"
                          :response (fn [ctx]
                                      (assoc (:response ctx)
                                             :status 404
                                             :body (render-frontend-file ctx)))})]]])

(defrecord WebServer [host
                      port
                      db
                      listener]
  Lifecycle
  (start [component]
    (if listener
      component                         ; idempotence
      (let [vhosts-model (vhosts-model [host (routes db {:port port})])
            listener (yada/listener vhosts-model {:port port})]
        (infof "Started web-server on port %s host %s" (:port listener) host)
        (assoc component :listener listener))))

  (stop [component]
    (when-let [close (get-in component [:listener :close])]
      (close))
    (assoc component :listener nil)))

(defn new-web-server [m]
  (using
   (map->WebServer m)
   [:db]))
