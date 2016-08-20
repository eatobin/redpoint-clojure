(ns clojure-redpoint.rules-test
  (:require [clojure.test :refer :all]
            [clojure-redpoint.rules :refer :all]
            [clojure-redpoint.roster :refer :all]
            [clojure-redpoint.hats :refer :all]))

(defn setup []
  (spit "beatles+2014.txt" "The Beatles, 2014
RinSta, Ringo Starr, JohLen, KarLav
JohLen, John Lennon, GeoHar, RinSta
GeoHar, George Harrison, PauMcc, JohLen
PauMcc, Paul McCartney, EriTob, GeoHar
EriTob, Eric Tobin, KarLav, PauMcc
KarLav, Karen Lavengood, RinSta, EriTob\n")
  (make-roster "beatles+2014.txt"))

(defn teardown [])

(defn each-fixture [f]
  (setup)
  (f)
  (teardown))

(use-fixtures :each each-fixture)

(deftest givee-not-self-test
  (is (= true
         (givee-not-self? :RinSta :GeoHar)))
  (is (= false
         (givee-not-self? :RinSta :RinSta))))

(deftest givee-not-recip-test
  (is (= true
         (givee-not-recip? :RinSta :JohLen 0)))
  (is (= false
         (givee-not-recip? :RinSta :KarLav 0))))

(deftest givee-not-repeat-test
  (add-new-year)
  (add-new-year)
  (add-new-year)
  (add-new-year)
  (set-givee-code :RinSta 1 :GeoHar)
  (set-givee-code :RinSta 2 :PauMcc)
  (set-givee-code :RinSta 3 :EriTob)
  (set-givee-code :RinSta 4 :KarLav)
  (is (= false
         (givee-not-repeat? :RinSta :JohLen 2)))
  (is (= false
         (givee-not-repeat? :RinSta :GeoHar 2)))
  (is (= true
         (givee-not-repeat? :RinSta :KarLav 2)))
  (is (= true
         (givee-not-repeat? :RinSta :JohLen 5)))
  (is (= true
         (givee-not-repeat? :RinSta :GeoHar 5)))
  (is (= false
         (givee-not-repeat? :RinSta :PauMcc 5)))
  (is (= false
         (givee-not-repeat? :RinSta :EriTob 5)))
  (is (= false
         (givee-not-repeat? :RinSta :KarLav 5))))
