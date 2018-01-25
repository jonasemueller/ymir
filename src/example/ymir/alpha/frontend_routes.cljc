(ns example.ymir.alpha.frontend-routes)

(def routes
  ["/" {"" ::index
        "about" ::about
        "notfound" ::page-not-found}])

(defn urls []
  (set (keys (second routes))))

(defn ids []
  (set (vals (second routes))))
