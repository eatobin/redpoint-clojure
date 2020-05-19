(ns clojure-redpoint.core-test
  (:require [clojure.test :refer :all]
            [clojure-redpoint.core :as core]
            [clojure-redpoint.roster :as ros]
            [clojure-redpoint.hats :as hat]
            [clojure.spec.alpha :as s]))

(def test-hat #{:PauMcc :GeoHar :JohLen :RinSta})

(deftest roster-or-quit-test
  (is (= "The Beatles"
         ((core/roster-or-quit "resources/beatles.json") :roster-name))))
(s/conform ::ros/roster-name
           ((core/roster-or-quit "resources/beatles.json") :roster-name))

(deftest draw-puck-test
  (is (true?
        (nil? (core/draw-puck #{}))))
  (is (true?
        (some? (core/draw-puck test-hat)))))
(s/conform ::ros/givee
           (core/draw-puck test-hat))
(s/conform nil?
           (core/draw-puck #{}))

;(deftest start-new-year-test
;  (reset! core/a-g-year 0)
;  (reset! core/a-giver nil)
;  (reset! core/a-givee nil)
;  (let [players-vector (ros/make-players-vector
;                         (core/scrubbed-or-quit "blackhawks2010.txt"))]
;    (reset! core/a-plrs-map (ros/make-players-map players-vector))
;    (core/start-new-year)
;    (is (= 1
;           (deref core/a-g-year)))
;    (is (not= nil
;              (deref core/a-giver)))
;    (is (not= nil
;              (deref core/a-givee)))
;    (is (= [:AndLad {:name         "Andrew Ladd",
;                     :gift-history [{:giver :KriVer, :givee :JoeQue}
;                                    {:giver :AndLad, :givee :AndLad}]}]
;           (first (deref core/a-plrs-map))))
;    (is (empty? (deref core/a-discards)))))
;
;(deftest select-new-giver-test
;  (reset! core/a-g-year 0)
;  (reset! core/a-giver nil)
;  (reset! core/a-givee nil)
;  (let [players-vector (ros/make-players-vector
;                         (core/scrubbed-or-quit "blackhawks2010.txt"))]
;    (reset! core/a-plrs-map (ros/make-players-map players-vector))
;    (core/start-new-year)
;    (swap! core/a-discards hat/discard-puck-givee :AdaBur)
;    (is (= 1
;           (count (deref core/a-discards))))
;    (core/select-new-giver)
;    (is (= 17
;           (count (deref core/a-gr-hat))))
;    (is (= 0
;           (count (deref core/a-discards))))))
;
;(deftest givee-is-success-test
;  (reset! core/a-g-year 0)
;  (reset! core/a-giver nil)
;  (reset! core/a-givee nil)
;  (let [players-vector (ros/make-players-vector
;                         (core/scrubbed-or-quit "blackhawks2010.txt"))]
;    (reset! core/a-plrs-map (ros/make-players-map players-vector))
;    (core/start-new-year)
;    (let [temp-ge (deref core/a-givee)]
;      (core/givee-is-success)
;      (is (= temp-ge
;             (ros/get-givee-in-roster (deref core/a-plrs-map) (deref core/a-giver) (deref core/a-g-year))))
;      (is (= (deref core/a-giver)
;             (ros/get-giver-in-roster (deref core/a-plrs-map) temp-ge (deref core/a-g-year))))
;      (is (= nil
;             (some #{temp-ge} (deref core/a-ge-hat)))))))
;
;(deftest givee-is-failure-test
;  (reset! core/a-g-year 0)
;  (reset! core/a-giver nil)
;  (reset! core/a-givee nil)
;  (let [players-vector (ros/make-players-vector
;                         (core/scrubbed-or-quit "blackhawks2010.txt"))]
;    (reset! core/a-plrs-map (ros/make-players-map players-vector))
;    (core/start-new-year)
;    (let [temp-ge (deref core/a-givee)]
;      (core/givee-is-failure)
;      (is (= temp-ge
;             (some #{temp-ge} (deref core/a-discards))))
;      (is (= nil
;             (some #{temp-ge} (deref core/a-ge-hat)))))))
