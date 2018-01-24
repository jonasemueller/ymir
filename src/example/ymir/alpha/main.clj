(ns example.ymir.alpha.main
  "Entrypoint for production Uberjars"
  (:gen-class)
  (:require [clojure.tools.cli :as cli]
            [com.stuartsierra.component :as component]
            [example.ymir.alpha.system :refer [new-system]]))

(def system nil)

(defmulti command (fn [args] (first (:arguments args))))

(defmethod command "version"
  [args]
  (println (some-> "version.txt" clojure.java.io/resource slurp clojure.string/trim)))

(defmethod command :default
  [args]
  (let [system (new-system :prod)]
    (component/start system))
  ;; All threads are daemon, so block forever:
  @(promise))

(defn -main
  [& args]
  (-> (cli/parse-opts args [])
      (command)))

