(ns clojure-redpoint.players
  (:require [clojure-redpoint.domain :as dom]
            [clojure-redpoint.gift-history :as gh]
            [clojure-redpoint.player :as plr]
            [clojure.spec.alpha :as s]
            [orchestra.spec.test :as ostest]
            [clojure-redpoint.gift-pair :as gp]))

(s/def ::players (s/map-of ::dom/player-key ::plr/player))

(defn players-update-player
  [players plr-key player]
  (assoc players plr-key player))
(s/fdef players-update-player
        :args (s/cat :players ::players
                     :plr-key ::dom/player-key
                     :player ::plr/player)
        :ret ::players)

(defn players-get-player-name
  [players plr-key]
  (:player-name (get players plr-key)))
(s/fdef players-get-player-name
        :args (s/cat :players ::players
                     :plr-key ::dom/player-key)
        :ret (s/or :found ::dom/player-name
                   :not-found nil?))

(defn players-add-year
  "Add a year for each player in roster"
  [players]
  (into {} (for [[plr-key player] players]
             (let [{:keys [player-name gift-history]} player]
               [plr-key (plr/map->Player {:player-name  player-name,
                                          :gift-history (conj gift-history (gp/map->Gift-Pair {:givee plr-key, :giver plr-key}))})]))))
(s/fdef players-add-year
        :args (s/cat :players ::players)
        :ret ::players)

(defn players-get-givee
  [players plr-key g-year]
  (:givee (get (:gift-history (get players plr-key)) g-year)))
(s/fdef players-get-givee
        :args (s/cat :players ::players
                     :plr-key ::dom/player-key
                     :g-year ::dom/gift-year)
        :ret ::dom/givee)

(defn players-get-giver
  [players plr-key g-year]
  (:giver (get (:gift-history (get players plr-key)) g-year)))
(s/fdef players-get-giver
        :args (s/cat :players ::players
                     :plr-key ::dom/player-key
                     :g-year ::dom/gift-year)
        :ret ::dom/giver)

(defn set-gift-pair
  [players plr-key g-year g-pair]
  (let [plr (plr-key players)
        ogh (:gift-history plr)
        ngh (gh/gift-history-update-gift-history ogh g-year g-pair)
        nplr (plr/player-update-gift-history plr ngh)]
    (players-update-player players plr-key nplr)))
(s/fdef set-gift-pair
        :args (s/cat :players ::players
                     :plr-key ::dom/player-key
                     :g-year ::dom/gift-year
                     :g-pair ::dom/gift-pair)
        :ret ::players)

(defn players-update-givee
  [players plr-key g-year givee]
  (let [plr (plr-key players)
        ogh (:gift-history plr)
        ogp (get ogh g-year)
        ngp (gp/gift-pair-update-givee ogp givee)]
    (set-gift-pair players plr-key g-year ngp)))
(s/fdef players-update-givee
        :args (s/cat :players ::players
                     :plr-key ::dom/player-key
                     :g-year ::dom/gift-year
                     :givee ::dom/givee)
        :ret ::players)

(defn players-update-giver
  [players plr-key g-year giver]
  (let [plr (plr-key players)
        ogh (:gift-history plr)
        ogp (get ogh g-year)
        ngp (gp/gift-pair-update-giver ogp giver)]
    (set-gift-pair players plr-key g-year ngp)))
(s/fdef players-update-giver
        :args (s/cat :players ::players
                     :plr-key ::dom/player-key
                     :g-year ::dom/gift-year
                     :givee ::dom/giver)
        :ret ::players)

(defn players-plain-player-upgrade
  [plain-players]
  (into {} (for [[k v] plain-players]
             {k (plr/player-plain-upgrade v)})))
(s/fdef players-plain-player-upgrade
        :args (s/cat :plain-players map?)
        :ret ::players)

(ostest/instrument)
