(ns eatobin.players
  (:require [eatobin.json-utilities :refer [json-utilities-my-value-reader
                                            json-utilities-my-key-reader]]
            [clojure.data.json :as json]
            [clojure.spec.alpha :as s]
            [orchestra.spec.test :as ostest]
            [eatobin.domain :as dom]
            [eatobin.gift-history :refer [gift-history-update-gift-history]]
            [eatobin.gift-pair :refer [gift-pair-update-givee
                                       gift-pair-update-giver]]
            [eatobin.player :refer [player-update-gift-history]]))

(defn players-json-string-to-players
  [json-string]
  (json/read-str json-string
                 :value-fn json-utilities-my-value-reader
                 :key-fn json-utilities-my-key-reader))
(s/fdef players-json-string-to-players
  :args (s/cat :json-string ::dom/json-string)
  :ret :unq/players)

(defn players-update-player
  [player-key player players]
  (assoc players player-key player))
(s/fdef players-update-player
  :args (s/cat :player-key ::dom/player-key
               :player :unq/player
               :players :unq/players)
  :ret :unq/players)

(defn players-get-player-name
  [player-key players]
  (:player-name (get players player-key)))
(s/fdef players-get-player-name
  :args (s/cat :player-key ::dom/player-key
               :players :unq/players)
  :ret (s/or :found ::dom/player-name
             :not-found nil?))

(defn players-add-year
  "Add a year for each player in roster"
  [players]
  (into {} (for [[player-key player] players]
             (let [{:keys [player-name gift-history]} player]
               [player-key {:player-name  player-name,
                            :gift-history (conj gift-history {:givee player-key, :giver player-key})}]))))
(s/fdef players-add-year
  :args (s/cat :players :unq/players)
  :ret :unq/players)

(defn players-get-my-givee
  [self-key players gift-year]
  (:givee (get (:gift-history (get players self-key)) gift-year)))
(s/fdef players-get-my-givee
  :args (s/cat :self-key ::dom/player-key
               :players :unq/players
               :gift-year ::dom/gift-year)
  :ret ::dom/givee)

(defn players-get-my-giver
  [self-key players gift-year]
  (:giver (get (:gift-history (get players self-key)) gift-year)))
(s/fdef players-get-my-giver
  :args (s/cat :self-key ::dom/player-key
               :players :unq/players
               :gift-year ::dom/gift-year)
  :ret ::dom/giver)

(defn- players-set-gift-pair
  [players player-key gift-year gift-pair]
  (let [plr (player-key players)
        ogh (:gift-history plr)
        ngh (gift-history-update-gift-history gift-year gift-pair ogh)
        nplr (player-update-gift-history ngh plr)]
    (players-update-player player-key nplr players)))
(s/fdef players-set-gift-pair
  :args (s/cat :players :unq/players
               :player-key ::dom/player-key
               :gift-year ::dom/gift-year
               :gift-pair :unq/gift-pair)
  :ret :unq/players)

(defn players-update-my-givee
  [self-key givee gift-year players]
  (let [plr (self-key players)
        ogh (:gift-history plr)
        ogp (get ogh gift-year)
        ngp (gift-pair-update-givee givee ogp)]
    (players-set-gift-pair players self-key gift-year ngp)))
(s/fdef players-update-my-givee
  :args (s/cat :self-key ::dom/player-key
               :givee ::dom/givee
               :gift-year ::dom/gift-year
               :players :unq/players)
  :ret :unq/players)

(defn players-update-my-giver
  [self-key giver gift-year players]
  (let [plr (self-key players)
        ogh (:gift-history plr)
        ogp (get ogh gift-year)
        ngp (gift-pair-update-giver giver ogp)]
    (players-set-gift-pair players self-key gift-year ngp)))
(s/fdef players-update-my-giver
  :args (s/cat :self-key ::dom/player-key
               :giver ::dom/giver
               :gift-year ::dom/gift-year
               :players :unq/players)
  :ret :unq/players)


; (defn players-update-my-giver
;   [players player-key gift-year giver]
;   (let [plr (player-key players)
;         ogh (:gift-history plr)
;         ogp (get ogh gift-year)
;         ngp (gift-pair-update-giver giver ogp)]
;     (players-set-gift-pair players player-key gift-year ngp)))
; (s/fdef players-update-my-giver
;   :args (s/cat :players :unq/players
;           :player-key ::dom/player-key
;           :gift-year ::dom/gift-year
;           :givee ::dom/giver)
;   :ret :unq/players)

(ostest/instrument)
