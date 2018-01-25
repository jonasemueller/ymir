(ns example.ymir.alpha.navigation
  (:require [example.ymir.alpha.re-frame :refer [>evt <sub]]
            [reagent.core :as reagent :refer [atom]]
            [example.ymir.alpha.link :as link]
            [example.ymir.alpha.frontend-routes :as routes]))

(defn navigation []
    [:ul.nav.navbar-nav.navbar-center
     [:li [link/to ::routes/index "Home"]]
     [:li [link/to ::routes/about "About"]]])
