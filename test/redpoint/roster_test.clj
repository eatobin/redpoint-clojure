(ns redpoint.roster-test
  (:require [clojure.test :refer [deftest is]]
            [redpoint.roster :as ros]
            [clojure.spec.alpha :as s]))

(def roster {:roster-name "The Beatles",
             :roster-year 2014,
             :players     {:PauMcc {:player-name  "Paul McCartney",
                                    :gift-history [{:giver :JohLen, :givee :GeoHar}]},
                           :GeoHar {:player-name  "George Harrison",
                                    :gift-history [{:giver :PauMcc, :givee :RinSta}]},
                           :JohLen {:player-name "John Lennon", :gift-history [{:giver :RinSta, :givee :PauMcc}]},
                           :RinSta {:player-name "Ringo Starr", :gift-history [{:giver :GeoHar, :givee :JohLen}]}}})

(def players (roster :players))

(def extended-players {:PauMcc {:player-name  "Paul McCartney",
                                :gift-history [{:giver :JohLen, :givee :GeoHar} {:givee :PauMcc, :giver :PauMcc}]},
                       :GeoHar {:player-name  "George Harrison",
                                :gift-history [{:giver :PauMcc, :givee :RinSta} {:givee :GeoHar, :giver :GeoHar}]},
                       :JohLen {:player-name "John Lennon", :gift-history [{:giver :RinSta, :givee :PauMcc} {:givee :JohLen, :giver :JohLen}]},
                       :RinSta {:player-name "Ringo Starr", :gift-history [{:giver :GeoHar, :givee :JohLen} {:givee :RinSta, :giver :RinSta}]}})

(s/conform :unq/players
           players)

(deftest roster-name-test
  (is (= "The Beatles"
         (roster :roster-name))))

(deftest roster-year-test
  (is (= 2014
         (roster :roster-year))))

(deftest get-player-name-test
  (is (= "George Harrison"
         (ros/get-player-name players :GeoHar)))
  (is (nil?
        (ros/get-player-name players :GeoHarX))))
(s/conform (s/or :found ::ros/player-name
                 :not-found nil?)
           (ros/get-player-name players :GeoHar))
(s/conform (s/or :found ::ros/player-name
                 :not-found nil?)
           (ros/get-player-name players :GeoHarX))

(deftest add-year-test
  (is (= extended-players
         (ros/add-year players))))
(s/conform :unq/players
           (ros/add-year players))

(deftest get-givee-giver-test
  (is (= :RinSta
         (ros/get-givee players :GeoHar 0)))
  (is (= :PauMcc
         (ros/get-giver players :GeoHar 0))))
(s/conform ::ros/givee
           (ros/get-givee players :GeoHar 0))
(s/conform ::ros/giver
           (ros/get-giver players :GeoHar 0))

(deftest set-givee-giver-test
  (is (= {:PauMcc {:player-name "Paul McCartney", :gift-history [{:giver :JohLen, :givee :GeoHar}]},
          :GeoHar {:player-name "George Harrison", :gift-history [{:giver :PauMcc, :givee :you}]},
          :JohLen {:player-name "John Lennon", :gift-history [{:giver :RinSta, :givee :PauMcc}]},
          :RinSta {:player-name "Ringo Starr", :gift-history [{:giver :GeoHar, :givee :JohLen}]}}
         (ros/update-givee players :GeoHar 0 :you)))
  (is (= {:PauMcc {:player-name "Paul McCartney", :gift-history [{:giver :JohLen, :givee :GeoHar}]},
          :GeoHar {:player-name "George Harrison", :gift-history [{:giver :you, :givee :RinSta}]},
          :JohLen {:player-name "John Lennon", :gift-history [{:giver :RinSta, :givee :PauMcc}]},
          :RinSta {:player-name "Ringo Starr", :gift-history [{:giver :GeoHar, :givee :JohLen}]}}
         (ros/update-giver players :GeoHar 0 :you))))
(s/conform :unq/players
           (ros/update-givee players :GeoHar 0 :you))
(s/conform :unq/players
           (ros/update-giver players :GeoHar 0 :you))
