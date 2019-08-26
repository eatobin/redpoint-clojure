(ns clojure-redpoint.core-test
  (:require [clojure.test :refer :all]
            [clojure-redpoint.core :as core]
            [clojure-redpoint.roster :as ros]
            [clojure-redpoint.hats :as hat]))

(def scrubbed "The Beatles,2014\nRinSta,Ringo Starr,JohLen,GeoHar\nJohLen,John Lennon,PauMcc,RinSta\nGeoHar,George Harrison,RinSta,PauMcc\nPauMcc,Paul McCartney,GeoHar,JohLen")

(deftest scrubbed-or-quit-test
  (is (= scrubbed
         (core/scrubbed-or-quit "beatles2014.txt")))
  (with-redefs [core/exit-now! (constantly "we exit here")]
    (is (= "we exit here" (core/scrubbed-or-quit "no-file.txt"))))
  (with-redefs [core/exit-now! (constantly "we exit here")]
    (is (= "we exit here" (core/scrubbed-or-quit "bad-length.txt"))))
  (with-redefs [core/exit-now! (constantly "we exit here")]
    (is (= "we exit here" (core/scrubbed-or-quit "bad-symbol.txt"))))
  (with-redefs [core/exit-now! (constantly "we exit here")]
    (is (= "we exit here" (core/scrubbed-or-quit "empty-first-line.txt"))))
  (with-redefs [core/exit-now! (constantly "we exit here")]
    (is (= "we exit here" (core/scrubbed-or-quit "missing-symbol.txt"))))
  (with-redefs [core/exit-now! (constantly "we exit here")]
    (is (= "we exit here" (core/scrubbed-or-quit "no-name.txt"))))
  (with-redefs [core/exit-now! (constantly "we exit here")]
    (is (= "we exit here" (core/scrubbed-or-quit "no-year.txt"))))
  (with-redefs [core/exit-now! (constantly "we exit here")]
    (is (= "we exit here" (core/scrubbed-or-quit "year-has-letter.txt"))))
  (with-redefs [core/exit-now! (constantly "we exit here")]
    (is (= "we exit here" (core/scrubbed-or-quit "year-too-big.txt"))))
  (with-redefs [core/exit-now! (constantly "we exit here")]
    (is (= "we exit here" (core/scrubbed-or-quit "year-too-small.txt")))))

(deftest start-new-year-test
  (reset! core/a-g-year 0)
  (reset! core/a-giver nil)
  (reset! core/a-givee nil)
  (let [players-vector (ros/make-players-vector
                         (core/scrubbed-or-quit "blackhawks2010.txt"))]
    (reset! core/a-plrs-map (ros/make-players-map players-vector))
    (core/start-new-year)
    (is (= 1
           (deref core/a-g-year)))
    (is (not= nil
              (deref core/a-giver)))
    (is (not= nil
              (deref core/a-givee)))
    (is (= [:AndLad {:name         "Andrew Ladd",
                     :gift-history [{:giver :KriVer, :givee :JoeQue}
                                    {:giver :AndLad, :givee :AndLad}]}]
           (first (deref core/a-plrs-map))))
    (is (empty? (deref core/a-discards)))))

(deftest select-new-giver-test
  (reset! core/a-g-year 0)
  (reset! core/a-giver nil)
  (reset! core/a-givee nil)
  (let [players-vector (ros/make-players-vector
                         (core/scrubbed-or-quit "blackhawks2010.txt"))]
    (reset! core/a-plrs-map (ros/make-players-map players-vector))
    (core/start-new-year)
    (swap! core/a-discards hat/discard-puck-givee :AdaBur)
    (is (= 1
           (count (deref core/a-discards))))
    (core/select-new-giver)
    (is (= 17
           (count (deref core/a-gr-hat))))
    (is (= 0
           (count (deref core/a-discards))))))

(deftest givee-is-success-test
  (reset! core/a-g-year 0)
  (reset! core/a-giver nil)
  (reset! core/a-givee nil)
  (let [players-vector (ros/make-players-vector
                         (core/scrubbed-or-quit "blackhawks2010.txt"))]
    (reset! core/a-plrs-map (ros/make-players-map players-vector))
    (core/start-new-year)
    (let [temp-ge (deref core/a-givee)]
      (core/givee-is-success)
      (is (= temp-ge
             (ros/get-givee-in-roster (deref core/a-plrs-map) (deref core/a-giver) (deref core/a-g-year))))
      (is (= (deref core/a-giver)
             (ros/get-giver-in-roster (deref core/a-plrs-map) temp-ge (deref core/a-g-year))))
      (is (= nil
             (some #{temp-ge} (deref core/a-ge-hat)))))))

(deftest givee-is-failure-test
  (reset! core/a-g-year 0)
  (reset! core/a-giver nil)
  (reset! core/a-givee nil)
  (let [players-vector (ros/make-players-vector
                         (core/scrubbed-or-quit "blackhawks2010.txt"))]
    (reset! core/a-plrs-map (ros/make-players-map players-vector))
    (core/start-new-year)
    (let [temp-ge (deref core/a-givee)]
      (core/givee-is-failure)
      (is (= temp-ge
             (some #{temp-ge} (deref core/a-discards))))
      (is (= nil
             (some #{temp-ge} (deref core/a-ge-hat)))))))
