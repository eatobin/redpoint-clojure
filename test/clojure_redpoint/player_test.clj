(ns clojure-redpoint.player-test
  (:require [clojure.test :refer [deftest is]]
            [clojure-redpoint.player :as plr]
            [clojure.spec.alpha :as s]
            [clojure-redpoint.gift-pair :as gp]))

(def player (plr/map->Player {:player-name  "Paul McCartney",
                              :gift-history [(gp/->Gift-Pair :GeoHar :JohLen)]}))

(deftest player-update-gift-history-test
  (is (= (plr/map->Player {:player-name  "Paul McCartney",
                           :gift-history [(gp/->Gift-Pair :nope :yup)]})
         (plr/player-update-gift-history player [(gp/->Gift-Pair :nope :yup)]))))
(s/conform ::plr/player
           (plr/player-update-gift-history player [(gp/->Gift-Pair :nope :yup)]))
