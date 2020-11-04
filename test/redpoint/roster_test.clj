(ns redpoint.roster-test
  (:require [clojure.test :refer [deftest is]]
            [redpoint.domain]
            [redpoint.roster :as ros]
            [clojure.spec.alpha :as s]))

(def json-string-Roster "{\"rosterName\":\"The Beatles\",\"rosterYear\":2014,\"players\":{\"PauMcc\":{\"playerName\":\"Paul McCartney\",\"giftHistory\":[{\"givee\":\"GeoHar\",\"giver\":\"JohLen\"}]},\"GeoHar\":{\"playerName\":\"George Harrison\",\"giftHistory\":[{\"givee\":\"RinSta\",\"giver\":\"PauMcc\"}]},\"JohLen\":{\"playerName\":\"John Lennon\",\"giftHistory\":[{\"givee\":\"PauMcc\",\"giver\":\"RinSta\"}]},\"RinSta\":{\"playerName\":\"Ringo Starr\",\"giftHistory\":[{\"givee\":\"JohLen\",\"giver\":\"GeoHar\"}]}}}")
(def roster (ros/roster-json-string-to-Roster json-string-Roster))

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
