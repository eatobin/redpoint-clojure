(ns clojure-redpoint.hats
  (:require [clojure-redpoint.gift-pair :as gp]
            [clojure-redpoint.gift-history :as gh]
            [clojure.spec.alpha :as s]
            [orchestra.spec.test :as ostest]))

(s/def ::hat (s/coll-of ::gh/player-key :kind set?))
(s/def ::discards (s/coll-of ::gh/player-key :kind set?))

(defn make-hat [roster]
  (into #{} (keys (roster :players))))
(s/fdef make-hat
  :args (s/cat :roster :unq/roster)
  :ret ::hat)

(defn remove-puck [hat plr-key]
  (into #{} (filter #(not= % plr-key) hat)))
(s/fdef remove-puck
  :args (s/cat :hat ::hat
               :plr-key ::gh/player-key)
  :ret ::hat)

(defn discard-givee [discards givee]
  (conj discards givee))
(s/fdef discard-givee
  :args (s/cat :discards ::discards :givee ::gp/givee)
  :ret ::discards)

(defn return-discards [ge-hat discards]
  (into ge-hat discards))
(s/fdef return-discards
  :args (s/cat :ge-hat ::hat
               :discards ::discards)
  :ret ::hat)

(defn empty-discards
  []
  #{})
(s/fdef empty-discards
  :args (s/cat)
  :ret ::discards)

(ostest/instrument)
