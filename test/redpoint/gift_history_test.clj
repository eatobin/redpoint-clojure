(ns redpoint.gift-history-test
  (:require [clojure.spec.alpha :as s]
            [clojure.test :refer [deftest is]]
            [redpoint.domain]
            [redpoint.gift-history :as gh]))

(def json-string-GH "[{\"givee\":\"GeoHar\",\"giver\":\"JohLen\"}]")
(def gift-history (gh/gift-history-json-string-to-Gift-History json-string-GH))

(deftest add-year-test
  (is (= [{:givee :GeoHar :giver :JohLen}, {:givee :NewBee :giver :NewBee}]
         (gh/gift-history-add-year gift-history :NewBee))))
(s/conform :unq/gift-history
           (gh/gift-history-add-year gift-history :NewBee))

(deftest get-gift-pair-test
  (is (= {:givee :GeoHar :giver :JohLen}
         (gift-history 0))))
(s/conform :unq/gift-pair
           (gift-history 0))

(deftest set-gift-pair-test
  (is (= [{:givee :me :giver :you}]
         (gh/gift-history-update-gift-history gift-history 0 {:givee :me :giver :you}))))
(s/conform :unq/gift-history
           (gh/gift-history-update-gift-history gift-history 0 {:givee :me :giver :you}))

(s/conform :unq/gift-history
           (gh/gift-history-json-string-to-Gift-History json-string-GH))
