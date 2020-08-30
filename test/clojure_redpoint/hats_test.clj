(ns clojure-redpoint.hats-test
  (:require [clojure-redpoint.gift-pair :as gp]
            [clojure-redpoint.player :as plr]
            [clojure.test :refer [deftest is]]
            [clojure-redpoint.hats :as hat]
            [clojure-redpoint.roster]
            [clojure.spec.alpha :as s]))

(def test-hat #{:PauMcc :GeoHar :JohLen :RinSta})

(def players {:PauMcc (plr/map->Player {:player-name  "Paul McCartney",
                                        :gift-history [(gp/map->Gift-Pair {:giver :JohLen, :givee :GeoHar})]}),
              :GeoHar (plr/map->Player {:player-name  "George Harrison",
                                        :gift-history [(gp/map->Gift-Pair {:giver :PauMcc, :givee :RinSta})]}),
              :JohLen (plr/map->Player {:player-name "John Lennon", :gift-history [(gp/map->Gift-Pair {:giver :RinSta, :givee :PauMcc})]}),
              :RinSta (plr/map->Player {:player-name "Ringo Starr", :gift-history [(gp/map->Gift-Pair {:giver :GeoHar, :givee :JohLen})]})})

(deftest make-hats-test
  (is (= test-hat
         (hat/make-hat players))))
(s/conform ::hat/hat
           (hat/make-hat players))

(deftest remove-puck-test
  (is (= #{:PauMcc :GeoHar :JohLen}
         (hat/remove-puck test-hat :RinSta))))

(deftest remove-puck-empty-test
  (is (= #{}
         (hat/remove-puck #{} :RinSta))))

(deftest discard-puck-givee-test
  (is (= #{:PauMcc :JohLen}
         (hat/discard-givee #{:PauMcc} :JohLen))))
(s/conform ::hat/discards
           (hat/discard-givee #{:PauMcc} :JohLen))

(deftest return-discards-test
  (is (= #{:PauMcc :JohLen :GeoHar}
         (hat/return-discards #{:PauMcc :JohLen} #{:GeoHar}))))
