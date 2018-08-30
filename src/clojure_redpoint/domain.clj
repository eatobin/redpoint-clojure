(ns clojure-redpoint.domain
  (:require [clojure.spec.alpha :as s]
            [clojure.test.check.generators :as gen]))

(s/def ::roster-string (s/with-gen string?
                                   #(gen/fmap (fn [[name year]]
                                                (str name ", " year))
                                              (gen/tuple gen/string-alphanumeric gen/nat))))
(s/def ::roster-line (s/coll-of string? :kind vector?))
(s/def ::roster-seq (s/coll-of ::roster-line :kind seq?))
(s/def ::roster-info-vector (s/coll-of string? :kind vector?))
(s/def ::plrs-list (s/coll-of vector? :kind list?))
(s/def ::givee keyword?)
(s/def ::giver keyword?)
(s/def :unq/gift-pair (s/keys :req-un [::givee ::giver]))
(s/def ::name string?)
(s/def :unq/gift-history (s/coll-of :unq/gift-pair :kind vector?))
(s/def :unq/player (s/keys :req-un [::name :unq/gift-history]))
(s/def ::plr-map-vec (s/tuple string? string? string? string?))
(s/def ::plr-map (s/map-of keyword? :unq/player))
