;; $ clojure -T:build test
;; $ clojure -M:test/kaocha-plain

(ns eatobin.gift-pair-test
  (:require [clojure.spec.alpha :as s]
            [clojure.test :refer [deftest is testing]]
            [eatobin.domain]
            [eatobin.gift-pair :refer [gift-pair-json-string-to-gift-pair
                                       gift-pair-update-givee
                                       gift-pair-update-giver]]))

(def gift-pair-test-json-string "{\"givee\":\"GeoHar\",\"giver\":\"JohLen\"}")
(def gift-pair-test-gift-pair {:givee :GeoHar :giver :JohLen})

(deftest update-givee-test
  (is (= {:givee :NewBee :giver :JohLen}
         (gift-pair-update-givee :NewBee gift-pair-test-gift-pair))))
(s/conform :unq/gift-pair
           (gift-pair-update-givee :NewBee gift-pair-test-gift-pair))

(deftest update-giver-test
  (is (= {:givee :GeoHar :giver :NewBee}
         (gift-pair-update-giver :NewBee gift-pair-test-gift-pair))))
(s/conform :unq/gift-pair
           (gift-pair-update-giver :NewBee gift-pair-test-gift-pair))

(deftest json-test
  (testing "gift-pair from JSON correctly constructed"
    (is (= gift-pair-test-gift-pair
           (gift-pair-json-string-to-gift-pair gift-pair-test-json-string)))))
(s/conform :unq/gift-pair
           (gift-pair-json-string-to-gift-pair gift-pair-test-json-string))
