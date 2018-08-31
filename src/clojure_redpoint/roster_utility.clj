(ns clojure-redpoint.roster-utility
  (:require [clojure-redpoint.domain :as dom]
            [clojure.string :as cs]
            [clojure-csv.core :as csv]
            [clojure.spec.alpha :as s]
            [orchestra.spec.test :as st]
            [clojure.repl :refer :all]))

(defn make-roster-seq
  "Returns a lazy roster-seq"
  [roster-string]
  (let [de-spaced (cs/replace roster-string #", " ",")]
    (csv/parse-csv de-spaced)))
(s/fdef make-roster-seq
        :args (s/cat :roster-string ::dom/roster-string)
        :ret ::dom/roster-seq)

(defn extract-roster-info-vector
  "Returns a vector containing the roster name and year"
  [roster-sequence]
  (let [res (first roster-sequence)]
    (if (nil? res)
      ["Is" "Empty!"]
      res)))
(s/fdef extract-roster-info-vector
        :args (s/cat :roster-sequence ::dom/roster-seq)
        :ret ::dom/roster-line)

(defn extract-players-list
  "Returns a list of vectors - each vector a player symbol, player name, first
  givee and first giver"
  [roster-sequence]
  (let [res (rest roster-sequence)]
    (if (= res '())
      (into () [["Is" "Empty!"]])
      (into () (rest roster-sequence)))))
(s/fdef extract-players-list
        :args (s/cat :roster-sequence ::dom/roster-seq)
        :ret ::dom/plrs-list)

(defn make-gift-pair
  "Returns a gift pair hash map given givee and giver as strings"
  [givee giver]
  (hash-map
    :givee (keyword givee)
    :giver (keyword giver)))
(s/fdef make-gift-pair
        :args (s/cat :givee string? :giver string?)
        :ret :unq/gift-pair)

(defn make-player
  "Returns a player hash map given a player name (string) and a gift history
  (vector of gift pairs)"
  [p-name g-hist]
  (hash-map
    :name p-name
    :gift-history g-hist))
(s/fdef make-player
        :args (s/cat :p-name ::dom/name :g-hist :unq/gift-history)
        :ret :unq/player)

(defn make-player-map
  "Returns a hash map linking a player symbol (key) to a player hash map (value)
  given a player symbol, name, initial givee and initial giver - all strings"
  [[s n ge gr]]
  (let [gp (make-gift-pair ge gr)
        plr (make-player n (vector gp))]
    (hash-map
      (keyword s) plr)))
(s/fdef make-player-map
        :args (s/cat :arg1 ::dom/plr-map-vec)
        :ret ::dom/plr-map)

(defn make-players-map
  "Returns a hash map of multiple players given a roster string"
  [players-list]
  (into {} (map make-player-map players-list)))
(s/fdef make-players-map
        :args (s/cat :players-list ::dom/plrs-list)
        :ret ::dom/plr-map)

(defn get-player-in-roster
  "Returns a player given a players map and a player symbol"
  [plrs-map plr-sym]
  (get plrs-map plr-sym))
(s/fdef get-player-in-roster
        :args (s/cat :plrs-map ::dom/plr-map :plr-sym keyword?)
        :ret :unq/player)

(defn get-gift-history-in-player
  "Returns a gift history given a player"
  [plr]
  (get plr :gift-history))
(s/fdef get-gift-history-in-player
        :args (s/cat :plr :unq/player)
        :ret :unq/gift-history)

(defn get-gift-pair-in-gift-history
  "Returns a gift pair given a gift history and a gift year"
  [g-hist g-year]
  (get g-hist g-year))
(s/fdef get-gift-pair-in-gift-history
        :args (s/cat :g-hist :unq/gift-history
                     :g-year int?)
        :ret :unq/gift-pair)

(defn get-gift-pair-in-roster
  "Returns s gift pair given a player map, a player symbol and a gift year"
  [plrs-map plr-sym g-year]
  (let [plr (get-player-in-roster plrs-map plr-sym)
        gh (get-gift-history-in-player plr)]
    (get-gift-pair-in-gift-history gh g-year)))
(s/fdef get-gift-pair-in-roster
        :args (s/cat :plrs-map ::dom/plr-map
                     :plr-sym keyword?
                     :g-year int?)
        :ret :unq/gift-pair)

(defn get-givee-in-gift-pair [g-pair]
  (get g-pair :givee))
(s/fdef get-givee-in-gift-pair
        :args (s/cat :g-pair :unq/gift-pair)
        :ret ::dom/givee)

(defn get-giver-in-gift-pair [g-pair]
  (get g-pair :giver))
(s/fdef get-giver-in-gift-pair
        :args (s/cat :g-pair :unq/gift-pair)
        :ret ::dom/giver)

(st/instrument)


;(defn check-give [plrs-map plr-sym g-year give]
;  (let [plr (get-player-in-roster plrs-map plr-sym)
;        gh (get-gift-history-in-player plr)
;        h-len (count gh)]
;    (and (contains? plrs-map plr-sym)
;         (contains? plrs-map give)
;         (<= (+ g-year 1) h-len))))
;
;(defn add-year-in-player [plr]
;  (let [gh (get-gift-history-in-player plr)
;        ngh (conj gh {:giver :none, :givee :none})]
;    (set-gift-history-in-player ngh plr)))
;
;(defn add-year-in-roster [plrs-map]
;  (into {}
;        (for [[k v] plrs-map]
;          [k (add-year-in-player v)])))
