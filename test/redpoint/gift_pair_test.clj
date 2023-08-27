(ns redpoint.gift-pair-test
  (:require [clojure.spec.alpha :as s]
    [clojure.test :refer [deftest is]]
    [redpoint.domain :as dom]
    [redpoint.gift-pair :as gp]))

(def json-string-GP "{\"givee\":\"GeoHar\",\"giver\":\"JohLen\"}")
(def gift-pair (gp/gift-pair-json-string-to-Gift-Pair json-string-GP))

(deftest giv-ee-er-test
  (is (= :GeoHar
        (:givee gift-pair)))
  (is (= :JohLen
        (:giver gift-pair))))
(s/conform ::dom/givee
  (:givee gift-pair))
(s/conform ::dom/giver
  (:giver gift-pair))

(deftest update-giv-ee-er-test
  (is (= {:givee :NewBee :giver :JohLen}
        (gp/gift-pair-update-givee gift-pair :NewBee)))
  (is (= {:givee :GeoHar :giver :NewBee}
        (gp/gift-pair-update-giver gift-pair :NewBee))))
(s/conform :unq/gift-pair
  (gp/gift-pair-update-givee gift-pair :NewBee))
(s/conform :unq/gift-pair
  (gp/gift-pair-update-giver gift-pair :NewBee))
;; Added in Calva:
(s/conform :unq/gift-pair
  (gp/gift-pair-update-givee gift-pair :Calva))
