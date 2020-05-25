(ns clojure-redpoint.rules-test
  (:require [clojure.test :refer :all]
            [clojure-redpoint.rules :as rule]
            [clojure-redpoint.roster :as ros]
            [clojure.spec.alpha :as s]))

(def beatles-plus-pm {:roster-name "The Beatles",
                      :roster-year 2014,
                      :players     {:RinSta {:player-name "Ringo Starr", :gift-history [{:giver :KarLav, :givee :JohLen}]},
                                    :JohLen {:player-name "John Lennon", :gift-history [{:giver :RinSta, :givee :GeoHar}]},
                                    :GeoHar {:player-name  "George Harrison",
                                             :gift-history [{:giver :JohLen, :givee :PauMcc}]},
                                    :PauMcc {:player-name  "Paul McCartney",
                                             :gift-history [{:giver :GeoHar, :givee :EriTob}]},
                                    :EriTob {:player-name "Eric Tobin", :gift-history [{:giver :PauMcc, :givee :KarLav}]},
                                    :KarLav {:player-name  "Karen Lavengood",
                                             :gift-history [{:giver :EriTob, :givee :RinSta}]}}})

(def extended ((comp ros/add-year
                     ros/add-year
                     ros/add-year
                     ros/add-year)
               beatles-plus-pm))

(def beatles-plus-4 (ros/set-givee
                      (ros/set-givee
                        (ros/set-givee
                          (ros/set-givee extended :RinSta 1 :GeoHar) :RinSta 2 :PauMcc) :RinSta 3 :EriTob) :RinSta 4 :KarLav))

(deftest givee-not-self-test
  (is (= true
         (rule/givee-not-self? :RinSta :GeoHar)))
  (is (= false
         (rule/givee-not-self? :RinSta :RinSta))))
(s/conform boolean?
           (rule/givee-not-self? :RinSta :GeoHar))

(deftest givee-not-recip-test
  (is (= true
         (rule/givee-not-recip? :RinSta :JohLen 0 beatles-plus-pm)))
  (is (= false
         (rule/givee-not-recip? :RinSta :KarLav 0 beatles-plus-pm))))
(s/conform boolean?
           (rule/givee-not-recip? :RinSta :JohLen 0 beatles-plus-pm))

(deftest givee-not-repeat-test
  (is (= false
         (rule/givee-not-repeat? :RinSta :JohLen 2 beatles-plus-4)))
  (is (= false
         (rule/givee-not-repeat? :RinSta :GeoHar 2 beatles-plus-4)))
  (is (= true
         (rule/givee-not-repeat? :RinSta :KarLav 2 beatles-plus-4)))
  (is (= true
         (rule/givee-not-repeat? :RinSta :JohLen 5 beatles-plus-4)))
  (is (= true
         (rule/givee-not-repeat? :RinSta :GeoHar 5 beatles-plus-4)))
  (is (= false
         (rule/givee-not-repeat? :RinSta :PauMcc 5 beatles-plus-4)))
  (is (= false
         (rule/givee-not-repeat? :RinSta :EriTob 5 beatles-plus-4)))
  (is (= false
         (rule/givee-not-repeat? :RinSta :KarLav 5 beatles-plus-4))))
(s/conform boolean?
           (rule/givee-not-repeat? :RinSta :JohLen 2 beatles-plus-4))
