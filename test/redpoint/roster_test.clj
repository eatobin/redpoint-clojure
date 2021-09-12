(ns redpoint.roster-test
  (:require [clojure.test :refer [deftest is]]
            [redpoint.domain]
            [redpoint.roster :as ros]
            [clojure.spec.alpha :as s]))

(def json-string-Roster-good [nil "{\"rosterName\":\"The Beatles\",\"rosterYear\":2014,\"players\":{\"PauMcc\":{\"playerName\":\"Paul McCartney\",\"giftHistory\":[{\"givee\":\"GeoHar\",\"giver\":\"JohLen\"}]},\"GeoHar\":{\"playerName\":\"George Harrison\",\"giftHistory\":[{\"givee\":\"RinSta\",\"giver\":\"PauMcc\"}]},\"JohLen\":{\"playerName\":\"John Lennon\",\"giftHistory\":[{\"givee\":\"PauMcc\",\"giver\":\"RinSta\"}]},\"RinSta\":{\"playerName\":\"Ringo Starr\",\"giftHistory\":[{\"givee\":\"JohLen\",\"giver\":\"GeoHar\"}]}}}"])
(def json-string-Roster-bad [nil "[ \"test\" :: 123 ]"])
(def maybe-roster-success (ros/roster-json-string-to-Roster json-string-Roster-good))
(def maybe-roster-failure (ros/roster-json-string-to-Roster json-string-Roster-bad))
(def error-string-success (first maybe-roster-success))
(def error-string-failure (first maybe-roster-failure))
(def roster-success (last maybe-roster-success))
(def roster-failure (last maybe-roster-failure))

(s/conform :unq/roster
           roster-success)

(deftest roster-name-test
  (is (= "The Beatles"
         (:roster-name roster-success))))

(deftest roster-year-test
  (is (= 2014
         (:roster-year roster-success))))

(deftest roster-failure-test
  (is (nil? roster-failure)))

(deftest error-string-failure-test
  (is (= "JSON error (invalid array)"
         error-string-failure)))

(deftest roster-success-test
  (is (any? roster-success)))

(deftest error-string-success-test
  (is (nil? error-string-success)))
