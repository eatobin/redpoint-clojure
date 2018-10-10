(ns clojure-redpoint.hats-test
  (:require [clojure.test :refer :all]
            [clojure-redpoint.hats :refer :all]))

(def test-hat [:PauMcc :GeoHar :JohLen :RinSta])
(def players-map {:PauMcc {:name         "Paul McCartney",
                           :gift-history [{:giver :JohLen, :givee :GeoHar}]},
                  :GeoHar {:name         "George Harrison",
                           :gift-history [{:giver :PauMcc, :givee :RinSta}]},
                  :JohLen {:name "John Lennon", :gift-history [{:giver :RinSta, :givee :PauMcc}]},
                  :RinSta {:name "Ringo Starr", :gift-history [{:giver :GeoHar, :givee :JohLen}]}})

(deftest make-hats-test
  (is (= test-hat
         (make-hat players-map))))

;(deftest remove-puck-givee-test
;  (is (= [:JohLen :GeoHar :PauMcc]
;         (remove-puck-givee test-hat :RinSta))))
;
;(deftest remove-puck-givee-empty-test
;  (is (= []
;         (remove-puck-givee [] :RinSta))))
;
;(deftest discard-puck-givee-test
;  (is (= [:PauMcc :JohLen]
;         (discard-puck-givee [:PauMcc] :JohLen))))
;
;(deftest return-discards-test
;  (is (= [:PauMcc :JohLen :GeoHar]
;         (return-discards [:PauMcc :JohLen] [:GeoHar]))))
