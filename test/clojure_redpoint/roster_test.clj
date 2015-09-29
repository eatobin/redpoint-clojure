(ns clojure-redpoint.roster-test
  (:require [clojure.test :refer :all]
            [clojure-redpoint.roster :refer :all]))

(defn setup []
  (spit "roster-test.txt" "Blackhawks, 1956
TroBro, Troy Brouwer, DavBol, JoeQue
JoeQue, Joel Quenneville, TroBro, AndLad
AdaBur, Adam Burish, DunKei, JonToe\n")
  (make-roster "roster-test.txt"))

(defn teardown [])

(defn each-fixture [f]
  (setup)
  (f)
  (teardown))

(use-fixtures :each each-fixture)

(deftest make-roster-test
  (is (= 3
         (count (deref roster))))
  (is (= [:TroBro {:name "Troy Brouwer", :gift-history [{:giver :JoeQue, :givee :DavBol}]}]
         (first (deref roster)))))

(deftest get-player-name-test
  (is (= "Adam Burish"
         (get-player-name :AdaBur))))

(deftest get-givee-code-test
  (is (= nil
         (get-givee-code :TroBroX 0)))
  (is (= nil
         (get-givee-code :TroBro 9)))
  (is (= :DavBol
         (get-givee-code :TroBro 0))))

(deftest set-givee-code-test
  (is (= {:TroBro {:name "Troy Brouwer", :gift-history [{:giver :JoeQue, :givee :DavBol}]},
          :JoeQue {:name "Joel Quenneville", :gift-history [{:giver :AndLad, :givee :TroBro}]},
          :AdaBur {:name "Adam Burish", :gift-history [{:giver :JonToe, :givee :test1}]}}
         (set-givee-code :AdaBur 0 :test1)))
  (is (= nil
         (set-givee-code :AdaBurX 0 :test1)))
  (is (= nil
         (set-givee-code :AdaBur 1 :test1))))

(deftest get-giver-code-test
  (is (= nil
         (get-giver-code :TroBroX 0)))
  (is (= nil
         (get-giver-code :TroBro 9)))
  (is (= :JoeQue
         (get-giver-code :TroBro 0))))

(deftest set-giver-code-test
  (is (= {:TroBro {:name "Troy Brouwer", :gift-history [{:giver :JoeQue, :givee :DavBol}]},
          :JoeQue {:name "Joel Quenneville", :gift-history [{:giver :AndLad, :givee :TroBro}]},
          :AdaBur {:name "Adam Burish", :gift-history [{:giver :test1, :givee :DunKei}]}}
         (set-giver-code :AdaBur 0 :test1)))
  (is (= nil
         (set-giver-code :AdaBurX 0 :test1)))
  (is (= nil
         (set-giver-code :AdaBur 1 :test1))))

(deftest add-new-year-test
  (is (= nil
         (add-new-year)))
  (is (= {:TroBro {:name "Troy Brouwer", :gift-history [{:giver :JoeQue, :givee :DavBol}
                                                        {:givee :none, :giver :none}]},
          :JoeQue {:name "Joel Quenneville", :gift-history [{:giver :AndLad, :givee :TroBro}
                                                            {:givee :none, :giver :none}]},
          :AdaBur {:name "Adam Burish", :gift-history [{:giver :JonToe, :givee :DunKei}
                                                       {:givee :none, :giver :none}]}}
         (deref roster))))
