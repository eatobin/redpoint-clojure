(ns eatobin.hat
  (:require [clojure.spec.alpha :as s]
            [eatobin.domain :as dom]
            [orchestra.spec.test :as ostest]))

(defn hat-make-hat
  [players]
  (into #{} (keys players)))
(s/fdef hat-make-hat
  :args (s/cat :players :unq/players)
  :ret :unq/hat)

(defn hat-remove-puck
  [player-key hat]
  (into #{} (remove #(= % player-key) hat)))
(s/fdef hat-remove-puck
  :args (s/cat :player-key ::dom/player-key
               :hat :unq/hat)
  :ret :unq/hat)

(defn hat-discard-givee
  [givee discards]
  (conj discards givee))
(s/fdef hat-discard-givee
  :args (s/cat :givee ::dom/givee
               :discards :unq/discards)
  :ret :unq/discards)

(defn hat-return-discards
  [discards givee-hat]
  (into givee-hat discards))
(s/fdef hat-return-discards
  :args (s/cat :discards :unq/discards
               :givee-hat :unq/hat)
  :ret :unq/hat)

(ostest/instrument)
