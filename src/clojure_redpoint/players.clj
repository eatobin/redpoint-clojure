(ns clojure-redpoint.players
  (:require [clojure-redpoint.domain :as dom]
            [clojure-redpoint.gift-history :as gh]
            [clojure-redpoint.player :as plr]
            [clojure.data.json :as json]
            [clojure.spec.alpha :as s]
            [orchestra.spec.test :as ostest]
            [clojure-redpoint.gift-pair :as gp]))

(defn players-update-player
  [players plr-key player]
  (assoc players plr-key player))
(s/fdef players-update-player
        :args (s/cat :players ::dom/players
                     :plr-key ::dom/player-key
                     :player ::dom/player)
        :ret ::dom/players)

(defn players-get-player-name
  [players plr-key]
  (:player-name (get players plr-key)))
(s/fdef players-get-player-name
        :args (s/cat :players ::dom/players
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
        :args (s/cat :players ::dom/players)
        :ret ::dom/players)

(defn players-get-givee
  [players plr-key g-year]
  (:givee (get (:gift-history (get players plr-key)) g-year)))
(s/fdef players-get-givee
        :args (s/cat :players ::dom/players
                     :plr-key ::dom/player-key
                     :g-year ::dom/gift-year)
        :ret ::dom/givee)

(defn players-get-giver
  [players plr-key g-year]
  (:giver (get (:gift-history (get players plr-key)) g-year)))
(s/fdef players-get-giver
        :args (s/cat :players ::dom/players
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
        :args (s/cat :players ::dom/players
                     :plr-key ::dom/player-key
                     :g-year ::dom/gift-year
                     :g-pair ::dom/gift-pair)
        :ret ::dom/players)

(defn players-update-givee
  [players plr-key g-year givee]
  (let [plr (plr-key players)
        ogh (:gift-history plr)
        ogp (get ogh g-year)
        ngp (gp/gift-pair-update-givee ogp givee)]
    (set-gift-pair players plr-key g-year ngp)))
(s/fdef players-update-givee
        :args (s/cat :players ::dom/players
                     :plr-key ::dom/player-key
                     :g-year ::dom/gift-year
                     :givee ::dom/givee)
        :ret ::dom/players)

(defn players-update-giver
  [players plr-key g-year giver]
  (let [plr (plr-key players)
        ogh (:gift-history plr)
        ogp (get ogh g-year)
        ngp (gp/gift-pair-update-giver ogp giver)]
    (set-gift-pair players plr-key g-year ngp)))
(s/fdef players-update-giver
        :args (s/cat :players ::dom/players
                     :plr-key ::dom/player-key
                     :g-year ::dom/gift-year
                     :givee ::dom/giver)
        :ret ::dom/players)

(defn players-plain-player-upgrade
  [plain-players]
  (into {} (for [[k v] plain-players]
             {k (plr/player-plain-upgrade v)})))
(s/fdef players-plain-player-upgrade
        :args (s/cat :plain-players map?)
        :ret ::dom/players)

(defn- my-key-reader
  [key]
  (cond
    (= key "playerName") :player-name
    (= key "giftHistory") :gift-history
    :else (keyword key)))

(defn- my-value-reader
  [key value]
  (if (or (= key :givee)
          (= key :giver))
    (keyword value)
    value))

(defn players-json-string-to-Players [plrs-string]
  (let [players (json/read-str plrs-string
                               :value-fn my-value-reader
                               :key-fn my-key-reader)]
    (players-plain-player-upgrade players)))
(s/fdef players-json-string-to-Players
        :args (s/cat :plrs-string string?)
        :ret ::dom/players)

(ostest/instrument)
