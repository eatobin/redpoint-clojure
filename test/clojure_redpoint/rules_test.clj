(ns clojure-redpoint.rules-test
  (:require [clojure.test :refer :all]
            [clojure-redpoint.rules :refer :all]
            [clojure-redpoint.roster :refer :all]
            [clojure-redpoint.roster-utility :refer :all]
            [clojure-redpoint.roster-test :refer :all]))

(def beatles-plus-string "The Beatles, 2014\nRinSta, Ringo Starr, JohLen, KarLav\nJohLen, John Lennon, GeoHar, RinSta\nGeoHar, George Harrison, PauMcc, JohLen\nPauMcc, Paul McCartney, EriTob, GeoHar\nEriTob, Eric Tobin, KarLav, PauMcc\nKarLav, Karen Lavengood, RinSta, EriTob\n")
(def beatles-plus-rl (make-roster-seq beatles-plus-string))
(def beatles-plus-pm (make-players-map beatles-plus-rl))
(def extended ((comp add-year-in-roster
                     add-year-in-roster
                     add-year-in-roster
                     add-year-in-roster)
                beatles-plus-pm))
(def beatles-plus-4 (set-givee-in-roster
                      (set-givee-in-roster
                        (set-givee-in-roster
                          (set-givee-in-roster extended :RinSta 1 :GeoHar) :RinSta 2 :PauMcc) :RinSta 3 :EriTob) :RinSta 4 :KarLav))

(deftest givee-not-self-test
  (is (= true
         (givee-not-self? :RinSta :GeoHar)))
  (is (= false
         (givee-not-self? :RinSta :RinSta))))

(deftest givee-not-recip-test
  (is (= true
         (givee-not-recip? :RinSta :JohLen 0 beatles-plus-pm)))
  (is (= false
         (givee-not-recip? :RinSta :KarLav 0 beatles-plus-pm))))

(deftest givee-not-repeat-test
  (is (= false
         (givee-not-repeat? :RinSta :JohLen 2 beatles-plus-4)))
  (is (= false
         (givee-not-repeat? :RinSta :GeoHar 2 beatles-plus-4)))
  (is (= true
         (givee-not-repeat? :RinSta :KarLav 2 beatles-plus-4)))
  (is (= true
         (givee-not-repeat? :RinSta :JohLen 5 beatles-plus-4)))
  (is (= true
         (givee-not-repeat? :RinSta :GeoHar 5 beatles-plus-4)))
  (is (= false
         (givee-not-repeat? :RinSta :PauMcc 5 beatles-plus-4)))
  (is (= false
         (givee-not-repeat? :RinSta :EriTob 5 beatles-plus-4)))
  (is (= false
         (givee-not-repeat? :RinSta :KarLav 5 beatles-plus-4))))
