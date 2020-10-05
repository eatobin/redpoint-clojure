(ns eatobin.hats
  (:require [eatobin.redpoint-domain :as dom]
            [clojure.spec.alpha :as s]
            [orchestra.spec.test :as ostest]))

(defn make-hat [players]
  (into #{} (keys players)))
(s/fdef make-hat
        :args (s/cat :players ::dom/players)
        :ret ::dom/hat)

(defn remove-puck [hat plr-key]
  (into #{} (remove #(= % plr-key) hat)))
(s/fdef remove-puck
        :args (s/cat :hat ::dom/hat
                     :plr-key ::dom/player-key)
        :ret ::dom/hat)

(defn discard-givee [discards givee]
  (conj discards givee))
(s/fdef discard-givee
        :args (s/cat :discards ::dom/discards
                     :givee ::dom/givee)
        :ret ::dom/discards)

(defn return-discards [ge-hat discards]
  (into ge-hat discards))
(s/fdef return-discards
        :args (s/cat :ge-hat ::dom/hat
                     :discards ::dom/discards)
        :ret ::dom/hat)

(ostest/instrument)
