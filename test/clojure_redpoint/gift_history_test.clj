(ns clojure-redpoint.gift-history-test
  (:require [clojure.test :refer :all]
            [clojure-redpoint.gift-history :as gh]
            [clojure.spec.alpha :as s]))

(def gift-history [{:giver :JohLen, :givee :GeoHar}])

(deftest add-year-test
  (is (= [{:giver :JohLen, :givee :GeoHar}, {:giver :NewBee, :givee :NewBee}]
         (gh/add-year gift-history :NewBee))))
(s/conform :unq/gift-history
           (gh/add-year gift-history :NewBee))
