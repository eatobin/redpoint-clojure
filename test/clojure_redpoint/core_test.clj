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

(deftest start-new-year-test
  (reset! core/a-g-year 0)
  (reset! core/a-giver nil)
  (reset! core/a-givee nil)
  (let [roster (core/roster-or-quit core/file-path)]
    (reset! core/a-players roster)
    (core/start-new-year)
    (is (= 1
           (deref core/a-g-year)))
    (is (some?
          (deref core/a-giver)))
    (is (some?
          (deref core/a-givee)))
    (is (= {:player-name  "Ringo Starr",
            :gift-history [{:givee :JohLen, :giver :GeoHar}
                           {:givee :RinSta, :giver :RinSta}]}
           (get-in (deref core/a-players) [:players :RinSta])))
    (is (empty? (deref core/a-discards)))))

(deftest select-new-giver-test
  (reset! core/a-g-year 0)
  (reset! core/a-giver nil)
  (reset! core/a-givee nil)
  (let [roster (core/roster-or-quit core/file-path)]
    (reset! core/a-players roster)
    (core/start-new-year)
    (swap! core/a-discards hat/discard-givee :GeoHar)
    (is (= 1
           (count (deref core/a-discards))))
    (core/select-new-giver)
    (is (= 3
           (count (deref core/a-gr-hat))))
    (is (= 0
           (count (deref core/a-discards))))))

(deftest givee-is-success-test
  (reset! core/a-g-year 0)
  (reset! core/a-giver nil)
  (reset! core/a-givee nil)
  (let [roster (core/roster-or-quit core/file-path)]
    (reset! core/a-players roster)
    (core/start-new-year)
    (let [temp-ge (deref core/a-givee)]
      (core/givee-is-success)
      (is (= temp-ge
             (ros/get-givee (deref core/a-players) (deref core/a-giver) (deref core/a-g-year))))
      (is (= (deref core/a-giver)
             (ros/get-giver (deref core/a-players) temp-ge (deref core/a-g-year))))
      (is (= nil
             (some #{temp-ge} (deref core/a-ge-hat)))))))

(deftest givee-is-failure-test
  (reset! core/a-g-year 0)
  (reset! core/a-giver nil)
  (reset! core/a-givee nil)
  (let [roster (core/roster-or-quit core/file-path)]
    (reset! core/a-players roster)
    (core/start-new-year)
    (let [temp-ge (deref core/a-givee)]
      (core/givee-is-failure)
      (is (= temp-ge
             (some #{temp-ge} (deref core/a-discards))))
      (is (= nil
             (some #{temp-ge} (deref core/a-ge-hat)))))))

(deftest errors?-test
  (reset! core/a-g-year 0)
  (reset! core/a-giver nil)
  (reset! core/a-givee nil)
  (let [roster (core/roster-or-quit core/file-path)]
    (reset! core/a-players roster)
    (core/start-new-year)))
