(ns redpoint.player-test
  (:require [clojure.test :refer [deftest is]]
            [redpoint.player :as plr]
            [clojure.spec.alpha :as s]))

(def json-string-Player "{\"playerName\":\"Paul McCartney\",\"giftHistory\":[{\"givee\":\"GeoHar\",\"giver\":\"JohLen\"}]}")
(def player {:player-name  "Paul McCartney",
             :gift-history [{:givee :GeoHar :giver :JohLen}]})

(deftest player-update-gift-history-test
  (is (= {:player-name  "Paul McCartney",
          :gift-history [{:givee :nope :giver :yup}]}
         (plr/player-update-gift-history player [{:givee :nope :giver :yup}]))))
(s/conform :unq/player
           (plr/player-update-gift-history player [{:givee :nope :giver :yup}]))

(deftest player-json-string-to-Player-test
  (is (= (plr/player-json-string-to-Player json-string-Player)
         player)))
(s/conform :unq/player
           (plr/player-json-string-to-Player json-string-Player))
