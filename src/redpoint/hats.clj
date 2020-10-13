(ns redpoint.hats
  (:require [redpoint.roster :as ros]
            [clojure.spec.alpha :as s]
            [orchestra.spec.test :as ostest]))

(s/def ::hat (s/coll-of ::ros/player-key :kind set?))
(s/def ::discards (s/coll-of ::ros/player-key :kind set?))

(defn make-hat [players]
  (into #{} (keys players)))
(s/fdef make-hat
        :args (s/cat :players :unq/players)
        :ret ::hat)

(defn remove-puck [hat plr-key]
  (into #{} (remove #(= % plr-key) hat)))
(s/fdef remove-puck
        :args (s/cat :hat ::hat
                     :plr-key ::ros/player-key)
        :ret ::hat)

(defn discard-givee [discards givee]
  (conj discards givee))
(s/fdef discard-givee
        :args (s/cat :discards ::discards :givee ::ros/givee)
        :ret ::discards)

(defn return-discards [ge-hat discards]
  (into ge-hat discards))
(s/fdef return-discards
        :args (s/cat :ge-hat ::hat
                     :discards ::discards)
        :ret ::hat)

(ostest/instrument)
