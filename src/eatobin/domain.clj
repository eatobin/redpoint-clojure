(ns eatobin.domain
  (:require [clojure.spec.alpha :as s]))

(s/def ::json-string string?)
(s/def ::player-key keyword?)
(s/def ::givee ::player-key)
(s/def ::giver ::player-key)
(s/def :unq/gift-pair (s/keys :req-un [::givee ::giver]))

(s/def :unq/gift-history (s/coll-of :unq/gift-pair :kind vector?))
(s/def ::player-key keyword?)
(s/def ::gift-year (s/and int? #(> % -1)))

(s/def ::player-name string?)
(s/def :unq/player (s/keys :req-un [::player-name :unq/gift-history]))

(s/def :unq/players (s/map-of ::player-key :unq/player))

(s/def :unq/hat (s/coll-of ::player-key :kind set?))
(s/def :unq/discards (s/coll-of ::player-key :kind set?))

(s/def ::roster-name string?)
(s/def ::roster-year int?)
(s/def :unq/givee-hat :unq/hat)
(s/def :unq/giver-hat :unq/hat)
(s/def ::maybe-player-key (s/nilable ::player-key))
(s/def ::maybe-givee (s/nilable ::givee))
(s/def ::maybe-giver (s/nilable ::giver))
(s/def ::quit string?)

(s/def :unq/my-state (s/keys :req-un [::roster-name
                                      ::roster-year
                                      :unq/players
                                      ::gift-year
                                      :unq/givee-hat
                                      :unq/giver-hat
                                      ::maybe-givee
                                      ::maybe-giver
                                      :unq/discards
                                      ::quit]))
;; (s/def :unq/error-seq (s/nilable (s/coll-of ::player-key :kind seq?)))
