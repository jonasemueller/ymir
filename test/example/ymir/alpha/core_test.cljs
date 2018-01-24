(ns example.ymir.alpha.core-test
  "Perfunctory docstring."
  (:require
   [cljs.test :refer-macros [is are deftest testing use-fixtures]]
   [pjstadig.humane-test-output]
   [example.ymir.alpha.core :as rc]
   [reagent.core :as reagent :refer [atom]]))

(deftest test-index
  (is (= true true)))
