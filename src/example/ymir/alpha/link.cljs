(ns example.ymir.alpha.link
  (:require [example.ymir.alpha.routes :as routes]
            [pushy.core :as pushy]
            [bidi.bidi :as bidi]
            [cljs.spec.alpha :as s]
            [cljs.spec.test.alpha :as stest]
            [example.ymir.alpha.app-db :as db]
            [example.ymir.alpha.re-frame :refer [>evt <sub]]
            [example.ymir.alpha.scroll :as scroll]))

(s/fdef load
        :args (s/cat :tag (routes/ids)))

(s/fdef to
        :args (s/cat :tag (routes/ids) :text string?))

(defn- dispatch-route [match]
  (>evt [::routes/current-page (:handler match)]))

(def history (pushy/pushy dispatch-route (partial bidi/match-route routes/routes)))

(defn init []
  (pushy/start! history))

(defn set-token! [token]
  (pushy/set-token! history token))

(defn path-for [tag & args]
  (apply bidi/path-for routes/routes tag args))

(defn load [tag]
  (do
    (scroll/jump-to-top)
    (pushy/set-token! history (path-for tag))))

(defn to [tag text]
  [:a {:href (path-for tag)} text])
