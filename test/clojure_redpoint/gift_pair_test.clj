(ns clojure-redpoint.gift-pair-test
  (:require [clojure.test :refer :all]
            [clojure-redpoint.gift-pair :as gp]
            [clojure.spec.alpha :as s]))

(def gift-pair (gp/->Gift-Pair :GeoHar :JohLen))

(deftest get-giv-ee-er-test
  (is (= :GeoHar
         (:givee gift-pair)))
  (is (= :JohLen
         (:giver gift-pair))))
;(s/conform ::gp/givee
;           (gp/get-givee gift-pair))
;(s/conform ::gp/giver
;           (gp/get-giver gift-pair))
;
;(deftest set-giv-ee-er-test
;  (is (= {:giver :JohLen, :givee :NewBee}
;         (gp/set-givee gift-pair :NewBee)))
;  (is (= {:givee :GeoHar, :giver :NewBee}
;         (gp/set-giver gift-pair :NewBee))))
;(s/conform :unq/gift-pair
;           (gp/set-givee gift-pair :NewBee))
;(s/conform :unq/gift-pair
;           (gp/set-giver gift-pair :NewBee))
