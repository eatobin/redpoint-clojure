(ns clojure-redpoint.player-test
  (:require [clojure.test :refer :all]
            [clojure-redpoint.player :as plr]
            [clojure.spec.alpha :as s]))

(def player {:player-name  "Paul McCartney",
             :gift-history [{:giver :JohLen, :givee :GeoHar}]})

(deftest get-player-name-in-player-test
  (is (= "Paul McCartney"
         (plr/get-player-name player))))
(s/conform ::plr/player-name
           (plr/get-player-name player))

(deftest get-gift-history-in-player-test
  (is (= [{:givee :GeoHar
           :giver :JohLen}]
         (plr/get-gift-history player))))
(s/conform :unq/gift-history
           (plr/get-gift-history player))
