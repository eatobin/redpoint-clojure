(ns clojure-redpoint.player
  (:require [clojure-redpoint.gift-pair :as gp]
            [clojure.spec.alpha :as s]
            [orchestra.spec.test :as ostest]))

(s/def ::player-name string?)
(s/def :unq/gift-history (s/coll-of :unq/gift-pair :kind vector?))
(s/def :unq/player (s/keys :req-un [::player-name :unq/gift-history]))

(ostest/instrument)
