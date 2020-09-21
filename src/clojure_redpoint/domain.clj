(ns clojure-redpoint.domain
  (:require [clojure.spec.alpha :as s]))

(s/def ::givee keyword?)
(s/def ::giver keyword?)
(s/def ::gift-pair (s/keys :req-un [::givee ::giver]))

(s/def ::gift-history (s/coll-of ::gift-pair :kind vector?))
(s/def ::player-key keyword?)
(s/def ::gift-year (s/and int? #(> % -1)))

(s/def ::player-name string?)
(s/def ::player (s/keys :req-un [::player-name ::gift-history]))

(s/def ::players (s/map-of ::player-key ::player))

(s/def ::roster-name string?)
(s/def ::roster-year int?)
(s/def ::roster (s/keys :req-un [::roster-name ::roster-year ::players]))

(s/def ::hat (s/coll-of ::player-key :kind set?))
(s/def ::discards (s/coll-of ::player-key :kind set?))