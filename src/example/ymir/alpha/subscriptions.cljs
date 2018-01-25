(ns example.ymir.alpha.subscriptions
  (:require [cljs.spec.alpha :as s]
            [example.ymir.alpha.app-db :as db]
            [example.ymir.alpha.events :as events]
            [example.ymir.alpha.frontend-routes :as routes]
            [re-frame.core :as rf]))

(rf/reg-sub
 ::routes/current-page
 (fn [db _]
   (get-in db [::db/current-page])))

(rf/reg-sub
 ::routes/current-page?
 :<- [::routes/current-page]
 (fn [current-page [query-id query-page]]
   (= current-page query-page)))