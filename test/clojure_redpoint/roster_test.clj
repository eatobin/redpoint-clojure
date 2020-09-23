(ns clojure-redpoint.roster-test
  (:require [clojure-redpoint.domain :as dom]
            [clojure.test :refer [deftest is]]
            [clojure-redpoint.roster :as ros]
            [clojure.spec.alpha :as s]))

(def json-string "{
   \"roster-name\": \"The Beatles\",
   \"roster-year\": 2014,
   \"players\": {
     \"RinSta\": {
       \"player-name\": \"Ringo Starr\",
       \"gift-history\": [
         {
           \"givee\": \"JohLen\",
           \"giver\": \"GeoHar\"
         }
       ]
     },
     \"JohLen\": {
       \"player-name\": \"John Lennon\",
       \"gift-history\": [
         {
           \"givee\": \"PauMcc\",
           \"giver\": \"RinSta\"
         }
       ]
     },
     \"GeoHar\": {
       \"player-name\": \"George Harrison\",
       \"gift-history\": [
         {
           \"givee\": \"RinSta\",
           \"giver\": \"PauMcc\"
         }
       ]
     },
     \"PauMcc\": {
       \"player-name\": \"Paul McCartney\",
       \"gift-history\": [
         {
           \"givee\": \"GeoHar\",
           \"giver\": \"JohLen\"
         }
       ]
     }
   }
 }
 ")

(def roster (ros/json-string-to-roster json-string))

(s/conform ::dom/roster
           roster)

(deftest roster-name-test
  (is (= "The Beatles"
         (:roster-name roster))))

(deftest roster-year-test
  (is (= 2014
         (:roster-year roster))))
