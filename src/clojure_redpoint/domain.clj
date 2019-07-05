(ns clojure-redpoint.domain
  (:require [clojure.spec.alpha :as s]
            [clojure.string :as str]))

(s/def ::roster-line (s/coll-of string? :kind vector?))
(s/def ::roster-seq (s/coll-of ::roster-line :kind seq?))
(s/def ::plrs-vector (s/coll-of ::roster-line :kind vector?))
(s/def ::givee keyword?)
(s/def ::giver keyword?)
(s/def :unq/gift-pair (s/keys :req-un [::givee ::giver]))
(s/def ::name string?)
(s/def :unq/gift-history (s/coll-of :unq/gift-pair :kind vector?))
(s/def :unq/player (s/keys :req-un [::name :unq/gift-history]))
(s/def ::plr-map-vec (s/tuple string? string? string? string?))
(s/def ::plr-map (s/map-of keyword? :unq/player))
(s/def ::g-year (s/and int? #(> % -1)))
(s/def ::hat (s/coll-of keyword? :kind vector?))
