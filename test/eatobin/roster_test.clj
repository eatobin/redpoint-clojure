(ns eatobin.roster-test
  (:require [eatobin.domain :as dom]
            [clojure.test :refer [deftest is]]
            [eatobin.gift-pair :as gp]
            [eatobin.player :as plr]
            [eatobin.roster :as ros]
            [clojure.spec.alpha :as s]))

(def json-string-Roster "{\"rosterName\":\"The Beatles\",\"rosterYear\":2014,\"players\":{\"PauMcc\":{\"playerName\":\"Paul McCartney\",\"giftHistory\":[{\"givee\":\"GeoHar\",\"giver\":\"JohLen\"}]},\"GeoHar\":{\"playerName\":\"George Harrison\",\"giftHistory\":[{\"givee\":\"RinSta\",\"giver\":\"PauMcc\"}]},\"JohLen\":{\"playerName\":\"John Lennon\",\"giftHistory\":[{\"givee\":\"PauMcc\",\"giver\":\"RinSta\"}]},\"RinSta\":{\"playerName\":\"Ringo Starr\",\"giftHistory\":[{\"givee\":\"JohLen\",\"giver\":\"GeoHar\"}]}}}")
(def roster (ros/map->Roster {:roster-name "The Beatles",
                              :roster-year 2014,
                              :players     {:PauMcc (plr/map->Player {:player-name  "Paul McCartney",
                                                                      :gift-history [(gp/map->Gift-Pair {:giver :JohLen, :givee :GeoHar})]}),
                                            :GeoHar (plr/map->Player {:player-name  "George Harrison",
                                                                      :gift-history [(gp/map->Gift-Pair {:giver :PauMcc, :givee :RinSta})]}),
                                            :JohLen (plr/map->Player {:player-name "John Lennon", :gift-history [(gp/map->Gift-Pair {:giver :RinSta, :givee :PauMcc})]}),
                                            :RinSta (plr/map->Player {:player-name "Ringo Starr", :gift-history [(gp/map->Gift-Pair {:giver :GeoHar, :givee :JohLen})]})}}))

(s/conform ::dom/roster
           roster)

(s/conform ::dom/roster
           (ros/roster-json-string-to-Roster json-string-Roster))

(deftest roster-name-test
  (is (= "The Beatles"
         (:roster-name roster))))

(deftest roster-year-test
  (is (= 2014
         (:roster-year roster))))

(deftest roster-json-string-to-Roster-test
  (is (= (ros/roster-json-string-to-Roster json-string-Roster)
         roster)))