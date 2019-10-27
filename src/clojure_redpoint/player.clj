(ns clojure-redpoint.player
  (:require [clojure.spec.alpha :as s]
            [orchestra.spec.test :as ostest]))

(s/def ::player-name string?)
(s/def :unq/gift-history (s/coll-of :unq/gift-pair :kind vector?))
(s/def :unq/player (s/keys :req-un [::player-name :unq/gift-history]))

(s/def ::gift-year (s/and int? #(> % -1)))

(defn get-player-name-in-player
  "Return a player-name given a player"
  [player]
  (player :player-name))
(s/fdef get-player-name-in-player
        :args (s/cat :player :unq/player)
        :ret ::player-name)

(defn get-gift-history-in-player
  "Return a gift-history given a player"
  [player]
  (player :gift-history))
(s/fdef get-gift-history-in-player
        :args (s/cat :player :unq/player)
        :ret :unq/gift-history)

(ostest/instrument)
