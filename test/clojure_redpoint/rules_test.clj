(ns clojure-redpoint.rules-test
  (:require [clojure.test :refer :all]
            [clojure-redpoint.rules :refer :all]
            [clojure-redpoint.roster :refer :all]
            [clojure-redpoint.hats :refer :all]))

(defn setup []
  (spit "beatles2014.txt" "The Beatles, 2014
RinSta, Ringo Starr, JohLen, GeoHar
JohLen, John Lennon, PauMcc, RinSta
GeoHar, George Harrison, RinSta, PauMcc
PauMcc, Paul McCartney, GeoHar, JohLen\n")
  (make-roster "beatles2014.txt"))

(defn teardown [])

(defn each-fixture [f]
  (setup)
  (f)
  (teardown))

(use-fixtures :each each-fixture)

(deftest make-hats-test
  (is (= [:RinSta :JohLen :GeoHar :PauMcc]
         (deref pucks-givee)))
  (is (= [:RinSta :JohLen :GeoHar :PauMcc]
         (deref pucks-giver)))
  (is (= []
         (deref discards))))

(deftest draw-puck-givee-test
  (is (some?
        (some #{(draw-puck-givee)} (deref pucks-givee)))))

(deftest draw-puck-givee-test
  (is (some?
        (some #{(draw-puck-giver)} (deref pucks-giver)))))

(deftest discard-puck-test
  (is (= [:RinSta]
         (discard-puck :RinSta)))
  (is (= [:JohLen :GeoHar :PauMcc]
         (deref pucks-givee)))
  (is (= [:RinSta]
         (deref discards)))
  (is (= nil
         (discard-puck :RinStaX))))

(deftest remove-puck-giver-test
  (is (= [:JohLen :GeoHar :PauMcc]
         (remove-puck-giver :RinSta))))

(deftest return-discards-test
  (discard-puck :RinSta)
  (is (= []
         (return-discards)))
  (is (= [:JohLen :GeoHar :PauMcc :RinSta]
         (deref pucks-givee)))
  (is (= []
         (deref discards))))
