(ns clojure-redpoint.hats-test
  (:require [clojure.test :refer :all]
            [clojure-redpoint.hats :refer :all]
            [clojure-redpoint.roster-test :refer :all]))

(def test-hat [:RinSta :JohLen :GeoHar :PauMcc])

(deftest make-hats-test
  (is (= [:RinSta :JohLen :GeoHar :PauMcc]
         (make-hat test-players-map))))

(deftest remove-puck-givee-test
  (is (= [:JohLen :GeoHar :PauMcc]
         (remove-puck-givee :RinSta test-hat))))

(deftest remove-puck-givee-empty-test
  (is (= []
         (remove-puck-givee :RinSta []))))

(deftest discard-puck-givee-test
  (is (= [:PauMcc :JohLen]
         (discard-puck-givee :JohLen [:PauMcc]))))

(deftest return-discards-test
  (is (= [:PauMcc :JohLen :GeoHar]
         (return-discards [:GeoHar] [:PauMcc :JohLen]))))
