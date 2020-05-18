(ns clojure-redpoint.roster
  (:require [clojure.spec.alpha :as s]
            [orchestra.spec.test :as ostest]))

(s/def ::givee keyword?)
(s/def ::giver keyword?)
(s/def :unq/gift-pair (s/keys :req-un [::givee ::giver]))
(s/def :unq/gift-history (s/coll-of :unq/gift-pair :kind vector?))
(s/def ::player-key keyword?)
(s/def ::gift-year (s/and int? #(> % -1)))
(s/def ::player-name string?)
(s/def :unq/player (s/keys :req-un [::player-name :unq/gift-history]))
(s/def :unq/players (s/map-of ::player-key :unq/player))
(s/def ::roster-name string?)
(s/def ::roster-year int?)
(s/def :unq/roster (s/keys :req-un [::roster-name ::roster-year :unq/players]))

(defn get-player-name
  [roster plr-key]
  (get-in roster [:players plr-key :player-name]))
(s/fdef get-player-name
        :args (s/cat :players :unq/roster
                     :plr-key ::player-key)
        :ret (s/or :found ::player-name
                   :not-found nil?))

(defn add-year
  "Add a year for each player in roster"
  [roster]
  {:roster-name (roster :roster-name)
   :roster-year (roster :roster-year)
   :players     (into {} (for [[plr-key player] (roster :players)]
                           (let [{:keys [player-name gift-history]} player]
                             [plr-key {:player-name  player-name,
                                       :gift-history (conj gift-history {:givee plr-key, :giver plr-key})}])))})
(s/fdef add-year
        :args (s/cat :roster :unq/roster)
        :ret :unq/roster)

(defn get-givee
  [players plr-key g-year]
  (get-in players [plr-key :gift-history g-year :givee]))
(s/fdef get-givee
        :args (s/cat :players :unq/players
                     :plr-key ::player-key
                     :g-year ::gift-year)
        :ret ::givee)

(defn get-giver
  [players plr-key g-year]
  (get-in players [plr-key :gift-history g-year :giver]))
(s/fdef get-givee
        :args (s/cat :players :unq/players
                     :plr-key ::player-key
                     :g-year ::gift-year)
        :ret ::giver)

(defn set-givee
  [players plr-key g-year givee]
  (assoc-in players [plr-key :gift-history g-year :givee] givee))
(s/fdef set-givee
        :args (s/cat :players :unq/players
                     :plr-key ::player-key
                     :g-year ::gift-year
                     :givee ::givee)
        :ret :unq/players)

(defn set-giver
  [players plr-key g-year giver]
  (assoc-in players [plr-key :gift-history g-year :giver] giver))
(s/fdef set-givee
        :args (s/cat :players :unq/players
                     :plr-key ::player-key
                     :g-year ::gift-year
                     :giver ::giver)
        :ret :unq/players)

(ostest/instrument)
