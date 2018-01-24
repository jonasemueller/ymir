(ns example.ymir.alpha.core
  (:require [reagent.core :as reagent]
            [re-frame.core :as rf]
            [example.ymir.alpha.frontend_navigation :as navigation]
            [example.ymir.alpha.app-db :as db]
            [example.ymir.alpha.re-frame :refer [>evt <sub]]
            [example.ymir.alpha.routes :as routes]
            [example.ymir.alpha.link :as link]
            [example.ymir.alpha.subscriptions :as subscriptions]
            [example.ymir.alpha.events :as events]
            [example.ymir.alpha.views :as views]))

;; -------------------------
;; Initialize app
(defn mount-components []
  (reagent/render [#'views/page] (.getElementById js/document "app"))
  (reagent/render [#'navigation/navigation] (.getElementById js/document "navigation")))


(defn init! []
  (enable-console-print!)
  (link/init)
  (mount-components)
  (>evt [::initialize-db]))

