(ns clojure-redpoint.roster-utility
  (:require [clojure-csv.core :as csv]
            [clojure.string :as cs]))

(defn make-roster-list
  "Returns a lazy roster-list"
  [roster-string]
  (let [de-spaced (cs/replace roster-string #", " ",")]
    (csv/parse-csv de-spaced)))

(defn make-roster-info [roster-list]
  (first roster-list))

(defn make-players-list [roster-list]
  (rest roster-list))

(defn make-gift-pair [givee giver]
  (hash-map
    :givee givee
    :giver giver))

(defn make-player [p-name g-hist]
  (hash-map :name p-name
            :gift-history g-hist))

(defn make-player-map [[s n ge gr]]
  (let [gp (make-gift-pair ge gr)
        plr (make-player n (vector gp))]
    (hash-map (keyword s) plr)))

(defn make-players-map [roster-list]
  (let [pl (make-players-list roster-list)]
    (map make-player-map pl)))

(defn get-player-in-roster [plr-sym plrs-map]
  (get plrs-map plr-sym))

(defn get-gift-history-in-player [plr]
  (get plr :gift-history))

(defn get-gift-pair-in-gift-history [g-hist g-year]
  (get g-hist g-year))

(defn get-gift-pair-in-roster [plr-sym plrs-map g-year]
  (let [plr (get-player-in-roster plr-sym plrs-map)
        gh (get-gift-history-in-player plr)]
    (get-gift-pair-in-gift-history gh g-year)))

(defn get-givee-in-gift-pair [g-pair]
  (get g-pair :givee))

(defn get-giver-in-gift-pair [g-pair]
  (get g-pair :giver))

(defn set-gift-pair-in-gift-history [g-year g-pair g-hist]
  (assoc g-hist g-year g-pair))

(defn set-gift-history-in-player [g-hist plr]
  (assoc plr :gift-history g-hist))

(defn set-gift-pair-in-roster [plr-sym g-year g-pair plrs-map]
  (let [plr (get-player-in-roster plr-sym plrs-map)
        gh (get-gift-history-in-player plr)
        ngh (set-gift-pair-in-gift-history g-year g-pair gh)
        nplr (set-gift-history-in-player ngh plr)]
    (assoc plrs-map plr-sym nplr)))

(defn check-give [plr-sym g-year give plrs-map]
  (let [plr (get-player-in-roster plr-sym plrs-map)
        gh (get-gift-history-in-player plr)
        h-len (count gh)]
    (and (contains? plrs-map plr-sym)
         (contains? plrs-map give)
         (<= (+ g-year 1) h-len))))

(defn add-year-in-player [plr]
  (let [gh (get-gift-history-in-player plr)
        ngh (conj gh {:giver :none, :givee :none})]
    (set-gift-history-in-player ngh plr)))

(defn add-year-in-roster [plrs-map]
  (map add-year-in-player (vals plrs-map)))
