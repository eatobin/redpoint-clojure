(ns clojure-redpoint.roster-test
  (:require [clojure-redpoint.domain :as dom]
            [clojure.test :refer [deftest is]]
            [clojure-redpoint.gift-pair :as gp]
            [clojure-redpoint.player :as plr]
            [clojure-redpoint.roster :as ros]
            [clojure.spec.alpha :as s]))

(def json-string-Roster "{\"roster-name\":\"The Beatles\",\"roster-year\":2014,\"players\":{\"PauMcc\":{\"player-name\":\"Paul McCartney\",\"gift-history\":[{\"givee\":\"GeoHar\",\"giver\":\"JohLen\"}]},\"GeoHar\":{\"player-name\":\"George Harrison\",\"gift-history\":[{\"givee\":\"RinSta\",\"giver\":\"PauMcc\"}]},\"JohLen\":{\"player-name\":\"John Lennon\",\"gift-history\":[{\"givee\":\"PauMcc\",\"giver\":\"RinSta\"}]},\"RinSta\":{\"player-name\":\"Ringo Starr\",\"gift-history\":[{\"givee\":\"JohLen\",\"giver\":\"GeoHar\"}]}}}")
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

(deftest roster-name-test
  (is (= "The Beatles"
         (:roster-name roster))))

(deftest roster-year-test
  (is (= 2014
         (:roster-year roster))))

(deftest roster-json-string-to-Roster-test
  (is (= (ros/roster-json-string-to-Roster json-string-Roster)
         roster)))