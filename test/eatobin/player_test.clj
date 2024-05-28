(ns eatobin.player-test
  (:require [clojure.spec.alpha :as s]
            [clojure.test :refer [deftest is testing]]
            [eatobin.player :refer [player-json-string-to-player
                                    player-update-gift-history]]))

(def player-test-json-string "{\"playerName\":\"Paul McCartney\",\"giftHistory\":[{\"givee\":\"GeoHar\",\"giver\":\"JohLen\"}]}")
(def player {:player-name "Paul McCartney" :gift-history [{:givee :GeoHar :giver :JohLen}]})

(deftest player-update-gift-history-test
  (is (= {:player-name  "Paul McCartney",
          :gift-history [{:givee :nope :giver :yup}]}
         (player-update-gift-history [{:givee :nope :giver :yup}] player))))
(s/conform :unq/player
           (player-update-gift-history [{:givee :nope :giver :yup}] player))

(deftest json-test
  (testing "player from JSON correctly constructed"
    (is (= player
           (player-json-string-to-player player-test-json-string)))))
(s/conform :unq/player
           (player-json-string-to-player player-test-json-string))
