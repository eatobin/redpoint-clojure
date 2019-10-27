(ns clojure-redpoint.player-test
  (:require [clojure.test :refer :all]
            [clojure-redpoint.player :as plr]
            [clojure.spec.alpha :as s]
            [clojure.spec.test.alpha :as stest]))

(def player {:player-name         "Paul McCartney",
             :gift-history [{:giver :JohLen, :givee :GeoHar}]})

(deftest get-player-name-in-player-test
  (is (= "Paul McCartney"
         (plr/get-player-name-in-player player))))
;(s/conform ::plr/player-name
;           (plr/get-player-name-in-player player))
