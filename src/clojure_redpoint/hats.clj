(ns clojure-redpoint.hats
  (:require [clojure-redpoint.gift-pair :as gp]
            [clojure-redpoint.players :as plrs]
            [clojure.spec.alpha :as s]
            [orchestra.spec.test :as ostest]))

(s/def ::hat (s/coll-of ::plrs/player-key :kind set?))
(s/def ::discards (s/coll-of ::plrs/player-key :kind set?))

(defn make-hat [players]
  (into #{} (keys players)))
(s/fdef make-hat
        :args (s/cat :players ::plrs/players)
        :ret ::hat)

(defn remove-puck [hat plr-key]
  (into #{} (remove #(= % plr-key) hat)))
(s/fdef remove-puck
        :args (s/cat :hat ::hat
                     :plr-key ::plrs/player-key)
        :ret ::hat)

(defn discard-givee [discards givee]
  (conj discards givee))
(s/fdef discard-givee
        :args (s/cat :discards ::discards
                     :givee ::gp/givee)
        :ret ::discards)

(defn return-discards [ge-hat discards]
  (into ge-hat discards))
(s/fdef return-discards
        :args (s/cat :ge-hat ::hat
                     :discards ::discards)
        :ret ::hat)

(ostest/instrument)
