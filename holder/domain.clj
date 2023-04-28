(ns redpoint.domain
  (:require [clojure.spec.alpha :as s]))

(s/def :unq/gift-history (s/coll-of :unq/gift-pair :kind vector?))
(s/def ::player-key keyword?)
(s/def ::gift-year (s/and int? #(> % -1)))

(s/def ::player-name string?)
(s/def :unq/player (s/keys :req-un [::player-name :unq/gift-history]))

(s/def :unq/players (s/map-of ::player-key :unq/player))

(s/def ::roster-name string?)
(s/def ::roster-year int?)
(s/def :unq/roster (s/keys :req-un [::roster-name ::roster-year :unq/players]))

(s/def :unq/hat (s/coll-of ::player-key :kind set?))
(s/def :unq/discards (s/coll-of ::player-key :kind set?))
