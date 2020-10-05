(ns redpoint.gift-history-test
  (:require [clojure.test :refer [deftest is]]
            [redpoint.domain :as dom]
            [redpoint.gift-pair :as gp]
            [redpoint.gift-history :as gh]
            [clojure.spec.alpha :as s]))

(def json-string-GH "[{\"givee\":\"GeoHar\",\"giver\":\"JohLen\"}]")
(def gift-history [(gp/->Gift-Pair :GeoHar :JohLen)])

(deftest add-year-test
  (is (= [(gp/->Gift-Pair :GeoHar :JohLen), (gp/->Gift-Pair :NewBee :NewBee)]
         (gh/gift-history-add-year gift-history :NewBee))))
(s/conform ::dom/gift-history
           (gh/gift-history-add-year gift-history :NewBee))

(deftest get-gift-pair-test
  (is (= (gp/map->Gift-Pair {:giver :JohLen, :givee :GeoHar})
         (gift-history 0))))
(s/conform ::dom/gift-pair
           (gift-history 0))

(deftest set-gift-pair-test
  (is (= [(gp/->Gift-Pair :me :you)]
         (gh/gift-history-update-gift-history gift-history 0 (gp/->Gift-Pair :me :you)))))
(s/conform ::dom/gift-history
           (gh/gift-history-update-gift-history gift-history 0 (gp/map->Gift-Pair {:givee :me, :giver :you})))

(deftest gift-history-json-string-to-Gift-History-test
  (is (= (gh/gift-history-json-string-to-Gift-History json-string-GH)
         gift-history)))