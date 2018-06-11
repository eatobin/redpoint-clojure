(ns clojure-redpoint.roster-utility
  (:require [clojure.string :as cs]
            [clojure-csv.core :as csv]))

;(defn get-givee-in-gift-pair [g-pair]
;  (get g-pair :givee))
;
;(defn get-giver-in-gift-pair [g-pair]
;  (get g-pair :giver))
;
;(defn set-gift-pair-in-gift-history [g-year g-pair g-hist]
;  (assoc g-hist g-year g-pair))
;
;(defn set-gift-history-in-player [g-hist plr]
;  (assoc plr :gift-history g-hist))
;
;(defn set-gift-pair-in-roster [plrs-map plr-sym g-year g-pair]
;  (let [plr (get-player-in-roster plrs-map plr-sym)
;        gh (get-gift-history-in-player plr)
;        ngh (set-gift-pair-in-gift-history g-year g-pair gh)
;        nplr (set-gift-history-in-player ngh plr)]
;    (assoc plrs-map plr-sym nplr)))
;
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
