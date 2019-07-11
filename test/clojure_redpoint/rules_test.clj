(ns clojure-redpoint.rules-test
  (:require [clojure.test :refer :all]
            [clojure-redpoint.rules :as rule]
            [clojure-redpoint.roster :as ros]))

(def beatles-plus-pm {:RinSta {:name "Ringo Starr", :gift-history [{:giver :KarLav, :givee :JohLen}]},
                      :JohLen {:name "John Lennon", :gift-history [{:giver :RinSta, :givee :GeoHar}]},
                      :GeoHar {:name         "George Harrison",
                               :gift-history [{:giver :JohLen, :givee :PauMcc}]},
                      :PauMcc {:name         "Paul McCartney",
                               :gift-history [{:giver :GeoHar, :givee :EriTob}]},
                      :EriTob {:name "Eric Tobin", :gift-history [{:giver :PauMcc, :givee :KarLav}]},
                      :KarLav {:name         "Karen Lavengood",
                               :gift-history [{:giver :EriTob, :givee :RinSta}]}})

(def extended ((comp ros/add-year-in-roster
                     ros/add-year-in-roster
                     ros/add-year-in-roster
                     ros/add-year-in-roster)
                beatles-plus-pm))
(def beatles-plus-4 (ros/set-givee-in-roster
                      (ros/set-givee-in-roster
                        (ros/set-givee-in-roster
                          (ros/set-givee-in-roster extended :RinSta 1 :GeoHar) :RinSta 2 :PauMcc) :RinSta 3 :EriTob) :RinSta 4 :KarLav))

(deftest givee-not-self-test
  (is (= true
         (rule/givee-not-self? :RinSta :GeoHar)))
  (is (= false
         (rule/givee-not-self? :RinSta :RinSta))))

(deftest givee-not-recip-test
  (is (= true
         (rule/givee-not-recip? :RinSta :JohLen 0 beatles-plus-pm)))
  (is (= false
         (rule/givee-not-recip? :RinSta :KarLav 0 beatles-plus-pm))))

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
