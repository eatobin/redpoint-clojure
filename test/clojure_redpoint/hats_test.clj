(ns clojure-redpoint.hats-test
  (:require [clojure.test :refer :all]
            [clojure-redpoint.hats :refer :all]))

(defn setup []
  (make-hats (atom {:RinSta {:name "Ringo Starr", :gift-history [{:giver :GeoHar, :givee :JohLen}]},
                    :JohLen {:name "John Lennon", :gift-history [{:giver :RinSta, :givee :PauMcc}]},
                    :GeoHar {:name "George Harrison", :gift-history [{:giver :PauMcc, :givee :RinSta}]},
                    :PauMcc {:name "Paul McCartney", :gift-history [{:giver :JohLen, :givee :GeoHar}]}})))

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
