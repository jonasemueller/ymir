;; Copyright © 2016, JUXT LTD.

(ns example.ymir.alpha.system
  "Components and their dependency relationships"
  (:require
   [aero.core :as aero]
   [clojure.java.io :as io]
   [com.stuartsierra.component :refer [system-map system-using]]
   [example.ymir.alpha.selmer :refer [new-selmer]]
   [example.ymir.alpha.web-server :refer [new-web-server]]
   [example.ymir.alpha.db :as db]))

(defn config
  "Read EDN config, with the given profile. See Aero docs at
  https://github.com/juxt/aero for details."
  [profile]
  (aero/read-config (io/resource "config.edn") {:profile profile}))

(defn new-system-map
  "Create the system. See https://github.com/stuartsierra/component"
  [config]
  (system-map
   :web-server (new-web-server (:web-server config))
   :selmer (new-selmer (:selmer config))
   :db (db/new-database {})))

(defn new-dependency-map
  "Declare the dependency relationships between components. See
  https://github.com/stuartsierra/component"
  []
  {})

(defn new-system
  "Construct a new system, configured with the given profile"
  [profile]
  (let [config (config profile)]
    (-> (new-system-map config)
        (system-using (new-dependency-map)))))
