(ns example.ymir.alpha.views
  (:require [bidi.bidi :as bidi]
            [pushy.core :as pushy]
            [example.ymir.alpha.pages.index :as page.index]
            [example.ymir.alpha.pages.about :as page.about]
            [example.ymir.alpha.pages.not-found :as page.not-found]
            [example.ymir.alpha.re-frame :refer [>evt <sub]]
            [example.ymir.alpha.routes :as routes]
            [re-frame.core :as rf]))

(def pages {::routes/index [page.index/index]
            ::routes/about [page.about/index]
            ::routes/page-not-found [page.not-found/index]})

(defn page []
  (let [current-page (rf/subscribe [::routes/current-page])]
    (fn []
      (get pages @current-page [page.not-found/index]))))

