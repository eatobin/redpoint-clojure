(ns clojure-redpoint.player-test
  (:require [clojure.test :refer :all]
            [clojure-redpoint.player :as plr]
            [clojure.spec.alpha :as s]))

(def player (plr/map->Player {:player-name  "Paul McCartney",
                              :gift-history [{:giver :JohLen, :givee :GeoHar}]}))

(deftest get-player-name-test
  (is (= "Paul McCartney"
         (:player-name player))))
(s/conform ::plr/player-name
           (:player-name player))

(deftest get-gift-history-test
  (is (= [{:givee :GeoHar
           :giver :JohLen}]
         (:gift-history player))))
(s/conform :unq/gift-history
           (:gift-history player))

(deftest set-gift-history-test
  (is (= (plr/map->Player {:player-name  "Paul McCartney",
                           :gift-history [{:givee :nope :giver :yup}]})
         (plr/set-gift-history player [{:givee :nope :giver :yup}]))))
(s/conform :unq/player
           (plr/set-gift-history player [{:givee :nope :giver :yup}]))

(deftest add-year-player-test
  (is (= #clojure_redpoint.player.Player{:player-name  "Paul McCartney",
                                         :gift-history [{:giver :JohLen, :givee :GeoHar}
                                                        #clojure_redpoint.gift_pair.Gift-Pair{:givee :mee,
                                                                                              :giver :mee}]}
         (plr/add-year-player player :mee))))
(s/conform :unq/player
           (plr/add-year-player player :mee))
