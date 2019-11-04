(ns clojure-redpoint.roster-test
  (:require [clojure.test :refer :all]
            [clojure-redpoint.roster :as ros]))

(def roster {:roster-name "The Beatles",
             :roster-year 2014,
             :players     {:PauMcc {:player-name  "Paul McCartney",
                                    :gift-history [{:giver :JohLen, :givee :GeoHar}]},
                           :GeoHar {:player-name  "George Harrison",
                                    :gift-history [{:giver :PauMcc, :givee :RinSta}]},
                           :JohLen {:player-name "John Lennon", :gift-history [{:giver :RinSta, :givee :PauMcc}]},
                           :RinSta {:player-name "Ringo Starr", :gift-history [{:giver :GeoHar, :givee :JohLen}]}}})

(deftest get-roster-name-test
  (is (= "The Beatles"
         (ros/get-roster-name roster))))

(deftest get-roster-year-test
  (is (= 2014
         (ros/get-roster-year roster))))

(deftest get-roster-players-test
  (is (= {:PauMcc {:player-name  "Paul McCartney",
                   :gift-history [{:giver :JohLen, :givee :GeoHar}]},
          :GeoHar {:player-name  "George Harrison",
                   :gift-history [{:giver :PauMcc, :givee :RinSta}]},
          :JohLen {:player-name "John Lennon", :gift-history [{:giver :RinSta, :givee :PauMcc}]},
          :RinSta {:player-name "Ringo Starr", :gift-history [{:giver :GeoHar, :givee :JohLen}]}}
         (ros/get-roster-players roster))))
