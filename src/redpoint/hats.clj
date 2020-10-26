(ns redpoint.hats
  (:require [redpoint.domain :as dom]
            [clojure.spec.alpha :as s]
            [orchestra.spec.test :as ostest]))

(defn make-hat [players]
  (into #{} (keys players)))
(s/fdef make-hat
        :args (s/cat :players :unq/players)
        :ret :unq/hat)

(defn remove-puck [hat plr-key]
  (into #{} (remove #(= % plr-key) hat)))
(s/fdef remove-puck
        :args (s/cat :hat :unq/hat
                     :plr-key ::dom/player-key)
        :ret :unq/hat)

(defn discard-givee [discards givee]
  (conj discards givee))
(s/fdef discard-givee
        :args (s/cat :discards :unq/discards
                     :givee ::dom/givee)
        :ret :unq/discards)

(defn return-discards [ge-hat discards]
  (into ge-hat discards))
(s/fdef return-discards
        :args (s/cat :ge-hat :unq/hat
                     :discards :unq/discards)
        :ret :unq/hat)

(ostest/instrument)
