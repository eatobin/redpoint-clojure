(ns redpoint.roster-test
  (:require [clojure.test :refer [deftest is]]
            [redpoint.roster :as ros]
            [clojure.spec.alpha :as s]))

(def json-string-Roster "{\"rosterName\":\"The Beatles\",\"rosterYear\":2014,\"players\":{\"PauMcc\":{\"playerName\":\"Paul McCartney\",\"giftHistory\":[{\"givee\":\"GeoHar\",\"giver\":\"JohLen\"}]},\"GeoHar\":{\"playerName\":\"George Harrison\",\"giftHistory\":[{\"givee\":\"RinSta\",\"giver\":\"PauMcc\"}]},\"JohLen\":{\"playerName\":\"John Lennon\",\"giftHistory\":[{\"givee\":\"PauMcc\",\"giver\":\"RinSta\"}]},\"RinSta\":{\"playerName\":\"Ringo Starr\",\"giftHistory\":[{\"givee\":\"JohLen\",\"giver\":\"GeoHar\"}]}}}")
(def roster {:roster-name "The Beatles",
             :roster-year 2014,
             :players     {:PauMcc {:player-name  "Paul McCartney",
                                    :gift-history [{:giver :JohLen, :givee :GeoHar}]},
                           :GeoHar {:player-name  "George Harrison",
                                    :gift-history [{:giver :PauMcc, :givee :RinSta}]},
                           :JohLen {:player-name "John Lennon", :gift-history [{:giver :RinSta, :givee :PauMcc}]},
                           :RinSta {:player-name "Ringo Starr", :gift-history [{:giver :GeoHar, :givee :JohLen}]}}})

(s/conform :unq/roster
           roster)

(s/conform :unq/roster
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
