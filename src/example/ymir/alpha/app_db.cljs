(ns example.ymir.alpha.app-db
  (:require [cljs.spec.alpha :as s]
            [cljs.spec.gen.alpha :as gen]
            [reagent.core :as reagent]
            [re-frame.core :as rf]))

(s/def ::app-db (s/keys :opt []))

(def default-db {})
