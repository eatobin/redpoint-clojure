(ns clojure-redpoint.player-test
  (:require [clojure.test :refer :all]
            [clojure-redpoint.player :as plr]
            [clojure.spec.alpha :as s]))

(def player {:player-name  "Paul McCartney",
             :gift-history [{:giver :JohLen, :givee :GeoHar}]})

(deftest get-player-name-test
  (is (= "Paul McCartney"
         (plr/get-player-name player))))
(s/conform ::plr/player-name
           (plr/get-player-name player))

(deftest get-gift-history-test
  (is (= [{:givee :GeoHar
           :giver :JohLen}]
         (plr/get-gift-history player))))
(s/conform :unq/gift-history
           (plr/get-gift-history player))

(deftest set-gift-history-test
  (is (= {:player-name  "Paul McCartney",
          :gift-history [{:givee :nope :giver :yup}]}
         (plr/set-gift-history player [{:givee :nope :giver :yup}]))))
(s/conform :unq/player
           (plr/set-gift-history player [{:givee :nope :giver :yup}]))

(deftest add-year-player-test
  (is (= {:player-name  "Paul McCartney",
          :gift-history [{:giver :JohLen, :givee :GeoHar} {:givee :mee, :giver :mee}]}
         (plr/add-year-player player :mee))))
(s/conform :unq/player
           (plr/add-year-player player :mee))
