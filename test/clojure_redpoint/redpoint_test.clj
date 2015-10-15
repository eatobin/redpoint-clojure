(ns clojure-redpoint.redpoint-test
  (:require [clojure.test :refer :all]
            [clojure-redpoint.redpoint :refer :all]
            [clojure-redpoint.roster :refer :all]
            [clojure-redpoint.hats :refer :all]
            [clojure-redpoint.rules :refer :all]))

(defn setup []
  (initialize-state))

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

(deftest start-new-year-test
  (start-new-year)
  (is (= 1
         (deref year)))
  (is (not= :none
            (deref giver)))
  (is (not= :none
            (deref givee)))
  (is (= [:AndLad {:name         "Andrew Ladd",
                   :gift-history [{:giver :KriVer, :givee :JoeQue}
                                  {:giver :none, :givee :none}]}]
         (first (deref roster)))))

(deftest select-new-giver-test
  (start-new-year)
  (discard-puck :AdaBur)
  (is (= 1
         (count (deref discards))))
  (select-new-giver)
  (is (= 17
         (count (deref giver-hat))))
  (is (= 0
         (count (deref discards)))))

(deftest givee-is-success-test
  (start-new-year)
  (givee-is-success)
  (is (= (deref givee)
         (get-givee-code (deref giver) (deref year))))
  (is (= (deref giver)
         (get-giver-code (deref givee) (deref year))))
  (is (= nil
         (some #{(deref givee)} (deref givee-hat)))))
