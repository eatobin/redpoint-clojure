(ns clojure-redpoint.player
  (:require [clojure-redpoint.gift-history :as gh]
            [clojure.spec.alpha :as s]
            [orchestra.spec.test :as ostest]))

(s/def ::player-name string?)
(s/def :unq/player (s/keys :req-un [::player-name :unq/gift-history]))

(defrecord Player [player-name gift-history])
(s/fdef ->Player
        :args (s/cat :player-name ::player-name
                     :gift-history :unq/gift-history)
        :ret :unq/player)

(defn player-update-gift-history
  "Sets a gift history into the provided player"
  [player g-hist]
  (assoc player :gift-history g-hist))
(s/fdef player-update-gift-history
        :args (s/cat :player :unq/player
                     :g-hist :unq/gift-history)
        :ret :unq/player)

;(defn add-year-player
;  "Adds a new placeholder year to the end of a player's gift history"
;  [player plr-key]
;  (->
;    player
;    (:gift-history player)
;    (gh/gift-history-add-year plr-key)
;    (->>
;      (player-update-gift-history player))))
;(s/fdef add-year-player
;        :args (s/cat :player :unq/player
;                     :plr-key ::gh/player-key)
;        :ret :unq/player)

(ostest/instrument)
