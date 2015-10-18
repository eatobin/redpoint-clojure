(ns clojure-redpoint.roster-test
  (:require [clojure.test :refer :all]
            [clojure-redpoint.roster :refer :all]))

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

(deftest make-roster-test
  (is (= "The Beatles"
         team-name))
  (is (= 2014
         first-year))
  (is (= 4
         (count (deref roster))))
  (is (= [:RinSta {:name "Ringo Starr", :gift-history [{:giver :GeoHar, :givee :JohLen}]}]
         (first (deref roster)))))

(deftest get-player-name-test
  (is (= "Ringo Starr"
         (get-player-name :RinSta))))

(deftest get-givee-code-test
  (is (= nil
         (get-givee-code :RinStaX 0)))
  (is (= nil
         (get-givee-code :RinSta 9)))
  (is (= :JohLen
         (get-givee-code :RinSta 0))))

(deftest set-givee-code-test
  (is (= {:RinSta {:name "Ringo Starr", :gift-history [{:giver :GeoHar, :givee :JohLen}]},
          :JohLen {:name "John Lennon", :gift-history [{:giver :RinSta, :givee :PauMcc}]},
          :GeoHar {:name "George Harrison", :gift-history [{:giver :PauMcc, :givee :RinSta}]},
          :PauMcc {:name "Paul McCartney", :gift-history [{:giver :JohLen, :givee :GeoHar}]}}
         (set-givee-code :PauMcc 0 :GeoHar)))
  (is (= nil
         (set-givee-code :PauMccX 0 :test1)))
  (is (= nil
         (set-givee-code :PauMcc 1 :test1))))

(deftest get-giver-code-test
  (is (= nil
         (get-giver-code :RinStaX 0)))
  (is (= nil
         (get-giver-code :RinSta 9)))
  (is (= :GeoHar
         (get-giver-code :RinSta 0))))

(deftest set-giver-code-test
  (is (= {:RinSta {:name "Ringo Starr", :gift-history [{:giver :GeoHar, :givee :JohLen}]},
          :JohLen {:name "John Lennon", :gift-history [{:giver :RinSta, :givee :PauMcc}]},
          :GeoHar {:name "George Harrison", :gift-history [{:giver :PauMcc, :givee :RinSta}]},
          :PauMcc {:name "Paul McCartney", :gift-history [{:giver :JohLen, :givee :GeoHar}]}}
         (set-giver-code :PauMcc 0 :JohLen)))
  (is (= nil
         (set-giver-code :PauMccX 0 :test1)))
  (is (= nil
         (set-giver-code :PauMcc 1 :test1))))

(deftest add-new-year-test
  (is (= nil
         (add-new-year)))
  (is (= {:RinSta {:name "Ringo Starr", :gift-history [{:giver :GeoHar, :givee :JohLen} {:givee :none, :giver :none}]},
          :JohLen {:name "John Lennon", :gift-history [{:giver :RinSta, :givee :PauMcc} {:givee :none, :giver :none}]},
          :GeoHar {:name "George Harrison", :gift-history [{:giver :PauMcc, :givee :RinSta} {:givee :none, :giver :none}]},
          :PauMcc {:name "Paul McCartney", :gift-history [{:giver :JohLen, :givee :GeoHar} {:givee :none, :giver :none}]}}
         (deref roster))))

(deftest print-string-giving-roster-test
  (make-roster "beatles2014.txt")
  (is (= "The Beatles - Year 2014 Gifts:

George Harrison is buying for Ringo Starr
John Lennon is buying for Paul McCartney
Paul McCartney is buying for George Harrison
Ringo Starr is buying for John Lennon\n"
         (print-string-giving-roster 0)))
  (make-roster "beatles-partial2014.txt")
  (is (= "The Partial Beatles - Year 2014 Gifts:

George Harrison is buying for Ringo Starr
Paul McCartney is buying for George Harrison
Ringo Starr is buying for John Lennon

There is a logic error in this year's pairings.
Do you see it?\nIf not... call me and I'll explain!

John Lennon is buying for no one.
Paul McCartney is receiving from no one.\n"
         (print-string-giving-roster 0))))
