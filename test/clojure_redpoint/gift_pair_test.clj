(ns clojure-redpoint.gift-pair-test
  (:require [clojure.test :refer :all]
            [clojure-redpoint.gift-pair :as gp]
            [clojure.spec.alpha :as s]
            [clojure.spec.test.alpha :as stest]))

(def gift-pair {:giver :JohLen, :givee :GeoHar})

(deftest get-givee-test
  (is (= :GeoHar
         (gp/get-givee-in-gift-pair gift-pair))))

(deftest get-giver-test
  (is (= :JohLen
         (gp/get-giver-in-gift-pair gift-pair))))
