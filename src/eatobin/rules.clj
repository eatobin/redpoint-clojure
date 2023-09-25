(ns eatobin.rules
  (:require [clojure.spec.alpha :as s]
    [orchestra.spec.test :as ostest]
    [eatobin.domain :as dom]
    [eatobin.players :refer [players-get-my-givee]]))

(defn rules-givee-not-self?
  "Test 1 - not giving to yourself"
  [self-key givee]
  (not= self-key givee))
(s/fdef rules-givee-not-self?
  :args (s/cat :self-key ::dom/player-key
          :givee ::dom/givee)
  :ret boolean?)

(defn rules-givee-not-recip?
  "Test 2 - not giving to the person who is giving to you"
  [self-key givee gift-year players]
  (let [recip (players-get-my-givee givee players gift-year)]
    (not= self-key recip)))
(s/fdef rules-givee-not-recip?
  :args (s/cat :self-key ::dom/player-key
          :givee ::dom/givee
          :gift-year ::dom/gift-year
          :players :unq/players)
  :ret boolean?)

(defn rules-givee-not-repeat?
  "Test 3 - not giving to someone you have given to in the past 3 years"
  [self-key givee gift-year players]
  (let [past (filter #(>= % 0)
               (range (- gift-year 1) (- gift-year 4) -1))
        ge-y (partial players-get-my-givee self-key players)
        ge-in-yrs (into [] (map ge-y past))]
    (not-any? #{givee} ge-in-yrs)))
(s/fdef rules-givee-not-repeat?
  :args (s/cat :self-key ::dom/player-key
          :givee ::dom/givee
          :gift-year ::dom/gift-year
          :players :unq/players)
  :ret boolean?)

(ostest/instrument)
