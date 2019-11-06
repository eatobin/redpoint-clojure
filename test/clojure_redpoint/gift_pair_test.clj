(ns clojure-redpoint.gift-pair-test
  (:require [clojure.test :refer :all]
            [clojure-redpoint.gift-pair :as gp]
            [clojure.spec.alpha :as s]))

(def gift-pair {:giver :JohLen, :givee :GeoHar})

(deftest get-giv-ee-er-test
  (is (= :GeoHar
         (gp/get-giv-ee-er gift-pair :ee)))
  (is (= :JohLen
         (gp/get-giv-ee-er gift-pair :er))))
(s/conform ::gp/giv
           (gp/get-giv-ee-er gift-pair :ee))
(s/conform ::gp/giv
           (gp/get-giv-ee-er gift-pair :er))

(deftest set-giv-ee-er-test
  (is (= {:giver :JohLen, :givee :NewBee}
         (gp/set-giv-ee-er gift-pair :NewBee :ee)))
  (is (= {:givee :GeoHar, :giver :NewBee}
         (gp/set-giv-ee-er gift-pair :NewBee :er))))
(s/conform :unq/gift-pair
           (gp/set-giv-ee-er gift-pair :NewBee :ee))
(s/conform :unq/gift-pair
           (gp/set-giv-ee-er gift-pair :NewBee :er))
