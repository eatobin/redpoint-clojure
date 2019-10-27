(ns clojure-redpoint.gift-pair-test
  (:require [clojure.test :refer :all]
            [clojure-redpoint.gift-pair :as gp]
            [clojure.spec.alpha :as s]
            [clojure.spec.test.alpha :as stest]))

(def gift-pair {:giver :JohLen, :givee :GeoHar})

(deftest get-givee-test
  (is (= :GeoHar
         (gp/get-givee gift-pair))))
(s/conform ::gp/givee
           (gp/get-givee gift-pair))

(deftest get-giver-test
  (is (= :JohLen
         (gp/get-giver gift-pair))))
(s/conform ::gp/giver
           (gp/get-giver gift-pair))
