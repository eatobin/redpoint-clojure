(ns clojure-redpoint.gift-history-test
  (:require [clojure.test :refer [deftest is]]
            [clojure-redpoint.gift-pair :as gp]
            [clojure-redpoint.gift-history :as gh]
            [clojure.spec.alpha :as s]))

(def gift-history [(gp/->Gift-Pair :GeoHar :JohLen)])

(deftest add-year-test
  (is (= [(gp/->Gift-Pair :GeoHar :JohLen), (gp/->Gift-Pair :NewBee :NewBee)]
         (gh/add-year gift-history :NewBee))))
(s/conform :unq/gift-history
           (gh/add-year gift-history :NewBee))

(deftest get-gift-pair-test
  (is (= (gp/map->Gift-Pair {:giver :JohLen, :givee :GeoHar})
         (gift-history 0))))
(s/conform :unq/gift-pair
           (gift-history 0))

(deftest set-gift-pair-test
  (is (= [(gp/->Gift-Pair :me :you)]
         (gh/update-gift-history gift-history 0 (gp/->Gift-Pair :me :you)))))
(s/conform :unq/gift-history
           (gh/update-gift-history gift-history 0 (gp/map->Gift-Pair {:givee :me, :giver :you})))
