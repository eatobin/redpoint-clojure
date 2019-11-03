(ns clojure-redpoint.players
  (:require [clojure-redpoint.gift-pair :as gp]
            [clojure-redpoint.gift-history :as gh]
            [clojure-redpoint.player :as plr]
            [clojure.spec.alpha :as s]
            [orchestra.spec.test :as ostest]))

(s/def :unq/players (s/map-of ::gh/player-key :unq/player))

(defn get-player
  "Returns a player given players and a player-key"
  [players plr-key]
  (players plr-key))
(s/fdef get-player
        :args (s/cat :players :unq/players
                     :plr-key ::gh/player-key)
        :ret (s/or :found :unq/player
                   :not-found nil?))

(defn set-player
  "Sets a player given players, a player-key and a new player"
  [players plr-key player]
  (assoc players plr-key player))
(s/fdef set-player
        :args (s/cat :players :unq/players
                     :plr-key ::gh/player-key
                     :player :unq/player)
        :ret :unq/players)

(defn add-year-players
  "Add a year for each player in roster"
  [players]
  (into {}
        (for [[plr-key player] players]
          [plr-key (plr/add-year-player player plr-key)])))
(s/fdef add-year-players
        :args (s/cat :players :unq/players)
        :ret :unq/players)

(defn get-player-name-players
  [players plr-key]
  (->
    players
    (get-player plr-key)
    :player-name))
(s/fdef get-player-name-players
        :args (s/cat :players :unq/players
                     :plr-key ::gh/player-key)
        :ret ::plr/player-name)

(defn get-givee-players
  [players plr-key g-year]
  (->
    players
    (get-player plr-key)
    :gift-history
    (get g-year)
    :givee))
(s/fdef get-givee-players
        :args (s/cat :players :unq/players
                     :plr-key ::gh/player-key
                     :g-year ::gh/gift-year)
        :ret ::gp/givee)

(defn get-giver-players
  [players plr-key g-year]
  (->
    players
    (get-player plr-key)
    :gift-history
    (get g-year)
    :giver))
(s/fdef get-giver-players
        :args (s/cat :players :unq/players
                     :plr-key ::gh/player-key
                     :g-year ::gh/gift-year)
        :ret ::gp/giver)

(defn set-givee-players
  [players plr-key g-year givee]
  (let [plr (get-player players plr-key)
        gh (plr :gift-history)
        gp (gh g-year)
        ngp (gp/set-givee gp givee)
        ngh (gh/set-gift-pair gh g-year ngp)
        nplr (plr/set-gift-history plr ngh)]
    (set-player players plr-key nplr)))
(s/fdef set-givee-players
        :args (s/cat :players :unq/players
                     :plr-key ::gh/player-key
                     :g-year ::gh/gift-year
                     :givee ::gp/givee)
        :ret :unq/players)

(defn set-giver-players
  [players plr-key g-year giver]
  (let [plr (get-player players plr-key)
        gh (plr :gift-history)
        gp (gh g-year)
        ngp (gp/set-giver gp giver)
        ngh (gh/set-gift-pair gh g-year ngp)
        nplr (plr/set-gift-history plr ngh)]
    (set-player players plr-key nplr)))
(s/fdef set-givee-players
        :args (s/cat :players :unq/players
                     :plr-key ::gh/player-key
                     :g-year ::gh/gift-year
                     :giver ::gp/giver)
        :ret :unq/players)

(ostest/instrument)
