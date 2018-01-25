(ns example.ymir.alpha.events
  (:require [clojure.string :as string]
            [example.ymir.alpha.app-db :as db]
            [example.ymir.alpha.re-frame :refer [>evt <sub]]
            [example.ymir.alpha.scroll :as scroll]
            [example.ymir.alpha.link :as link]
            [example.ymir.alpha.frontend-routes :as routes]
            [re-frame.core :as rf]
            [cljs.spec.alpha :as s]))

(defn check-and-throw
  "Throws an exception if `db` doesn't match the Spec `a-spec`."
  [a-spec db]
  (when-not (s/valid? a-spec db)
    (throw (ex-info (str "spec check failed: " (s/explain-str a-spec db)) {}))))

(def check-spec-interceptor (rf/after (partial check-and-throw ::db/app-db)))

(def general-interceptors [check-spec-interceptor])

(rf/reg-event-db
 ::routes/current-page
 (fn [db [_ new-data]]
   (assoc-in db [::db/current-page] new-data)))

(rf/reg-event-db
 ::routes/navigate
 (fn [db [_ new-tag]]
   (do (scroll/jump-to-top)
       (link/load new-tag)
       db)))

(rf/reg-event-db
 :example.ymir.alpha.core/initialize-db
 general-interceptors
 (fn [db _]
   (merge db db/default-db)))