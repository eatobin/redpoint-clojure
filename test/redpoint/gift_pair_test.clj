(ns redpoint.gift-pair-test
  (:require [clojure.spec.alpha :as s]
            [clojure.test :refer [deftest is]]
            [redpoint.gift-pair :as gp]))

(def json-string-GP "{\"givee\":\"GeoHar\",\"giver\":\"JohLen\"}")
(def gift-pair (gp/gift-pair-json-string-to-Gift-Pair json-string-GP))

(deftest giv-ee-er-test
  (is (= :GeoHar
         (:givee gift-pair)))
  (is (= :JohLen
         (:giver gift-pair))))
(s/conform ::gp/givee
           (:givee gift-pair))
(s/conform ::gp/giver
           (:giver gift-pair))

(deftest update-giv-ee-er-test
  (is (= {:givee :NewBee :giver :JohLen}
         (gp/gift-pair-update-givee :NewBee gift-pair)))
  (is (= {:givee :GeoHar :giver :NewBee}
         (gp/gift-pair-update-giver :NewBee gift-pair))))
(s/conform :unq/gift-pair
           (gp/gift-pair-update-givee :NewBee gift-pair))
(s/conform :unq/gift-pair
           (gp/gift-pair-update-giver :NewBee gift-pair))

(deftest gift-pair-update-giver-test
  (is (= {:givee :GeoHar :giver :NewBee}
         (gp/gift-pair-update-giver :NewBee gift-pair))))
