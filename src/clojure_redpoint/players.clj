(ns clojure-redpoint.players
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

(defn get-player-name
  [players plr-key]
  (get-in players [plr-key :player-name]))
(s/fdef get-player-name
        :args (s/cat :players :unq/players
                     :plr-key ::player-key)
        :ret (s/or :found ::player-name
                   :not-found nil?))

(defn add-year
  "Add a year for each player in roster"
  [players]
  (into {} (for [[plr-key player] players]
             (let [{:keys [player-name gift-history]} player]
               [plr-key {:player-name  player-name,
                         :gift-history (conj gift-history {:givee plr-key, :giver plr-key})}]))))
(s/fdef add-year
        :args (s/cat :players :unq/players)
        :ret :unq/players)

(defn get-givee
  [players plr-key g-year]
  (get-in players [plr-key :gift-history g-year :givee]))
(s/fdef get-givee
        :args (s/cat :players :unq/players
                     :plr-key ::player-key
                     :g-year ::gift-year)
        :ret ::givee)

;(defn get-player
;  "Returns a player given players and a player-key"
;  [players plr-key]
;  (players plr-key))
;(s/fdef get-player
;        :args (s/cat :players :unq/players
;                     :plr-key ::gh/player-key)
;        :ret (s/or :found :unq/player
;                   :not-found nil?))
;
;(defn set-player
;  "Sets a player given players, a player-key and a new player"
;  [players plr-key player]
;  (assoc players plr-key player))
;(s/fdef set-player
;        :args (s/cat :players :unq/players
;                     :plr-key ::gh/player-key
;                     :player :unq/player)
;        :ret :unq/players)
;

;
;
;

;
;(defn set-giv-ee-er-players
;  [players plr-key g-year giv ee-er]
;  (let [plr (get-player players plr-key)
;        gh (plr :gift-history)
;        gp (gh g-year)
;        ngp (gp/set-giv-ee-er gp giv ee-er)
;        ngh (gh/set-gift-pair gh g-year ngp)
;        nplr (plr/set-gift-history plr ngh)]
;    (set-player players plr-key nplr)))
;(s/fdef set-giv-ee-er-players
;        :args (s/cat :players :unq/players
;                     :plr-key ::gh/player-key
;                     :g-year ::gh/gift-year
;                     :giv ::gp/giv
;                     :ee-er ::gp/ee-er)
;        :ret :unq/players)

(ostest/instrument)
