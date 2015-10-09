(ns clojure-redpoint.redpoint-test
  (:require [clojure.test :refer :all]
            [clojure-redpoint.redpoint :refer :all]
            [clojure-redpoint.roster :refer :all]
            [clojure-redpoint.hats :refer :all]
            [clojure-redpoint.rules :refer :all]))

(defn setup []
  initialize-state)

(defn teardown [])

(defn each-fixture [f]
  (setup)
  (f)
  (teardown))

(use-fixtures :each each-fixture)

(deftest initialize-state-test
  (is (= 0
         (deref year)))
  (is (= :none
         (deref giver)))
  (is (= :none
         (deref givee)))
  (is (= [:AndLad {:name         "Andrew Ladd",
                   :gift-history [{:giver :KriVer, :givee :JoeQue}]}]
         (first (deref roster)))))
