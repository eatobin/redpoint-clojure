(ns redpoint.gift-pair-test
  (:require [clojure.test :refer [deftest is]]
            [redpoint.domain :as dom]
            [redpoint.gift-pair :as gp]
            [clojure.spec.alpha :as s]))

(def json-string-GP "{\"givee\":\"GeoHar\",\"giver\":\"JohLen\"}")
(def gift-pair (gp/->Gift-Pair :GeoHar :JohLen))

(deftest get-giv-ee-er-test
  (is (= :GeoHar
         (:givee gift-pair)))
  (is (= :JohLen
         (:giver gift-pair))))
(s/conform ::dom/givee
           (:givee gift-pair))
(s/conform ::dom/giver
           (:giver gift-pair))

(deftest set-giv-ee-er-test
  (is (= (gp/->Gift-Pair :NewBee :JohLen)
         (gp/gift-pair-update-givee gift-pair :NewBee)))
  (is (= (gp/->Gift-Pair :GeoHar :NewBee)
         (gp/gift-pair-update-giver gift-pair :NewBee))))
(s/conform ::dom/gift-pair
           (gp/gift-pair-update-givee gift-pair :NewBee))
(s/conform ::dom/gift-pair
           (gp/gift-pair-update-giver gift-pair :NewBee))

(deftest gift-pair-json-string-to-Gift-Pair-test
  (is (= (gp/gift-pair-json-string-to-Gift-Pair json-string-GP)
         gift-pair)))