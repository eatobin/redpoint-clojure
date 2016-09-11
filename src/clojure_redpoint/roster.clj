(ns clojure-redpoint.roster
  (:require [clojure-redpoint.roster-utility :refer :all]
            [clojure-csv.core :as csv]
            [clojure.string :as cs]))

(defn get-roster-name [roster-list]
  (let [line (make-roster-info roster-list)]
    (first line)))

(defn get-roster-year [roster-list]
  (let [line (make-roster-info roster-list)]
    (read-string (last line))))

(defn get-player-name-in-roster [plr-sym plrs-map]
  (let [plr (get-player-in-roster plr-sym plrs-map)]
    (get plr :name)))

(defn get-givee-in-roster [plr-sym plrs-map g-year]
  (let [gp (get-gift-pair-in-roster plr-sym plrs-map g-year)]
    (get-givee-in-gift-pair gp)))

(defn get-giver-in-roster [plr-sym plrs-map g-year]
  (let [gp (get-gift-pair-in-roster plr-sym plrs-map g-year)]
    (get-giver-in-gift-pair gp)))

(defn set-givee-in-roster [plr-sym g-year ge plrs-map]
  (if (check-give plr-sym g-year ge plrs-map)
    (let [gr (get-giver-in-roster plr-sym plrs-map g-year)
          gp (make-gift-pair ge gr)]
      (set-gift-pair-in-roster plr-sym g-year gp plrs-map))
    plrs-map))

(defn set-giver-in-roster [plr-sym g-year gr plrs-map]
  (if (check-give plr-sym g-year gr plrs-map)
    (let [ge (get-givee-in-roster plr-sym plrs-map g-year)
          gp (make-gift-pair ge gr)]
      (set-gift-pair-in-roster plr-sym g-year gp plrs-map))
    plrs-map))
