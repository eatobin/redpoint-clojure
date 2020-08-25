(ns clojure-redpoint.player
  (:require [clojure-redpoint.gift-history :as gh]
            [clojure.spec.alpha :as s]
            [orchestra.spec.test :as ostest]))

(s/def ::player-name string?)
(s/def ::player (s/keys :req-un [::player-name ::gh/gift-history]))

(defrecord Player [player-name gift-history])
(s/fdef ->Player
        :args (s/cat :player-name ::player-name
                     :gift-history ::gh/gift-history)
        :ret ::player)

(defn player-update-gift-history
  "Sets a gift history into the provided player"
  [player g-hist]
  (assoc player :gift-history g-hist))
(s/fdef player-update-gift-history
        :args (s/cat :player ::player
                     :g-hist ::gh/gift-history)
        :ret ::player)

(ostest/instrument)
