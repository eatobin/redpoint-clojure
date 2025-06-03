(ns eatobin.gift-history-test
  (:require [clojure.spec.alpha :as s]
            [clojure.test :refer [deftest is testing]]
            [eatobin.domain]
            [eatobin.gift-history :refer [gift-history-add-year
                                          gift-history-json-string-to-gift-history
                                          gift-history-update-gift-history]]))

(def gift-history-test-json-string "[{\"givee\":\"GeoHar\",\"giver\":\"JohLen\"}]")
(def gift-history-test-gift-history [{:givee :GeoHar :giver :JohLen}])

(deftest add-year-test
  (is (= [{:givee :GeoHar :giver :JohLen}, {:givee :NewBee :giver :NewBee}]
         (gift-history-add-year :NewBee gift-history-test-gift-history))))
(s/conform :unq/gift-history
           (gift-history-add-year :NewBee gift-history-test-gift-history))

(deftest get-gift-pair-test
  (is (= {:givee :GeoHar :giver :JohLen}
         (gift-history-test-gift-history 0))))
(s/conform :unq/gift-pair
           (gift-history-test-gift-history 0))

(deftest set-gift-pair-test
  (is (= [{:givee :me :giver :you}]
         (gift-history-update-gift-history 0 {:givee :me :giver :you} gift-history-test-gift-history))))
(s/conform :unq/gift-history
           (gift-history-update-gift-history 0 {:givee :me :giver :you} gift-history-test-gift-history))

(deftest json-test
  (testing "gift-history from JSON correctly constructed"
    (is (= gift-history-test-gift-history
           (gift-history-json-string-to-gift-history gift-history-test-json-string)))))
(s/conform :unq/gift-history
           (gift-history-json-string-to-gift-history gift-history-test-json-string))
