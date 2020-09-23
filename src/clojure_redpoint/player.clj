(ns clojure-redpoint.player
  (:require [clojure-redpoint.gift-pair :as gp]
            [clojure-redpoint.domain :as dom]
            [clojure.spec.alpha :as s]
            [orchestra.spec.test :as ostest]))

(defrecord Player [player-name gift-history])
(s/fdef ->Player
        :args (s/cat :player-name ::dom/player-name
                     :gift-history ::dom/gift-history)
        :ret ::dom/player)

(defn player-update-gift-history
  "Sets a gift history into the provided player"
  [player g-hist]
  (assoc player :gift-history g-hist))
(s/fdef player-update-gift-history
        :args (s/cat :player ::dom/player
                     :g-hist ::dom/gift-history)
        :ret ::dom/player)

(defn player-plain-upgrade
  [plain-player]
  (let [gh (:gift-history plain-player)
        first-gp (get gh 0)
        ngp (gp/map->Gift-Pair first-gp)
        ngh [ngp]
        nplr (map->Player plain-player)]
    (player-update-gift-history nplr ngh)))
(s/fdef player-plain-upgrade
        :args (s/cat :plain-player map?)
        :ret ::dom/player)

(ostest/instrument)
