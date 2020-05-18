(ns clojure-redpoint.roster-test
  (:require [clojure.test :refer :all]
            [clojure-redpoint.roster :as ros]
            [clojure.spec.alpha :as s]))

(def players {:PauMcc {:player-name  "Paul McCartney",
                       :gift-history [{:giver :JohLen, :givee :GeoHar}]},
              :GeoHar {:player-name  "George Harrison",
                       :gift-history [{:giver :PauMcc, :givee :RinSta}]},
              :JohLen {:player-name "John Lennon", :gift-history [{:giver :RinSta, :givee :PauMcc}]},
              :RinSta {:player-name "Ringo Starr", :gift-history [{:giver :GeoHar, :givee :JohLen}]}})

(def extended-players {:PauMcc {:player-name  "Paul McCartney",
                                :gift-history [{:giver :JohLen, :givee :GeoHar} {:givee :PauMcc, :giver :PauMcc}]},
                       :GeoHar {:player-name  "George Harrison",
                                :gift-history [{:giver :PauMcc, :givee :RinSta} {:givee :GeoHar, :giver :GeoHar}]},
                       :JohLen {:player-name "John Lennon", :gift-history [{:giver :RinSta, :givee :PauMcc} {:givee :JohLen, :giver :JohLen}]},
                       :RinSta {:player-name "Ringo Starr", :gift-history [{:giver :GeoHar, :givee :JohLen} {:givee :RinSta, :giver :RinSta}]}})

(def roster {:roster-name "The Beatles",
             :roster-year 2014,
             :players     {:PauMcc {:player-name  "Paul McCartney",
                                    :gift-history [{:giver :JohLen, :givee :GeoHar}]},
                           :GeoHar {:player-name  "George Harrison",
                                    :gift-history [{:giver :PauMcc, :givee :RinSta}]},
                           :JohLen {:player-name "John Lennon", :gift-history [{:giver :RinSta, :givee :PauMcc}]},
                           :RinSta {:player-name "Ringo Starr", :gift-history [{:giver :GeoHar, :givee :JohLen}]}}})

(def extended-roster {:roster-name "The Beatles",
                      :roster-year 2014,
                      :players     {:PauMcc {:player-name  "Paul McCartney",
                                             :gift-history [{:giver :JohLen, :givee :GeoHar} {:givee :PauMcc, :giver :PauMcc}]},
                                    :GeoHar {:player-name  "George Harrison",
                                             :gift-history [{:giver :PauMcc, :givee :RinSta} {:givee :GeoHar, :giver :GeoHar}]},
                                    :JohLen {:player-name "John Lennon", :gift-history [{:giver :RinSta, :givee :PauMcc} {:givee :JohLen, :giver :JohLen}]},
                                    :RinSta {:player-name "Ringo Starr", :gift-history [{:giver :GeoHar, :givee :JohLen} {:givee :RinSta, :giver :RinSta}]}}})

(s/conform :unq/roster
           roster)

(deftest get-player-name-test
  (is (= "George Harrison"
         (ros/get-player-name roster :GeoHar)))
  (is (nil?
        (ros/get-player-name roster :GeoHarX))))
(s/conform (s/or :found ::ros/player-name
                 :not-found nil?)
           (ros/get-player-name roster :GeoHar))
(s/conform (s/or :found ::ros/player-name
                 :not-found nil?)
           (ros/get-player-name roster :GeoHarX))

(deftest add-year-test
  (is (= extended-roster
         (ros/add-year roster))))
(s/conform :unq/roster
           (ros/add-year roster))

(deftest get-givee-giver-test
  (is (= :RinSta
         (ros/get-givee roster :GeoHar 0)))
  (is (= :PauMcc
         (ros/get-giver roster :GeoHar 0))))
(s/conform ::ros/givee
           (ros/get-givee roster :GeoHar 0))
(s/conform ::ros/giver
           (ros/get-giver roster :GeoHar 0))

(deftest set-givee-giver-test
  (is (= {:roster-name "The Beatles",
          :roster-year 2014,
          :players     {:PauMcc {:player-name "Paul McCartney", :gift-history [{:giver :JohLen, :givee :GeoHar}]},
                        :GeoHar {:player-name "George Harrison", :gift-history [{:giver :PauMcc, :givee :you}]},
                        :JohLen {:player-name "John Lennon", :gift-history [{:giver :RinSta, :givee :PauMcc}]},
                        :RinSta {:player-name "Ringo Starr", :gift-history [{:giver :GeoHar, :givee :JohLen}]}}}
         (ros/set-givee roster :GeoHar 0 :you)))
  (is (= {:roster-name "The Beatles",
          :roster-year 2014,
          :players     {:PauMcc {:player-name "Paul McCartney", :gift-history [{:giver :JohLen, :givee :GeoHar}]},
                        :GeoHar {:player-name "George Harrison", :gift-history [{:giver :you, :givee :RinSta}]},
                        :JohLen {:player-name "John Lennon", :gift-history [{:giver :RinSta, :givee :PauMcc}]},
                        :RinSta {:player-name "Ringo Starr", :gift-history [{:giver :GeoHar, :givee :JohLen}]}}}
         (ros/set-giver roster :GeoHar 0 :you))))
(s/conform :unq/roster
           (ros/set-givee roster :GeoHar 0 :you))
(s/conform :unq/roster
           (ros/set-giver roster :GeoHar 0 :you))
