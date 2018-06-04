(ns clojure-redpoint.roster
  (:require [clojure-redpoint.roster-utility :refer :all]))

(defn get-roster-name [roster-list]
  (let [line (extract-roster-info roster-list)]
    (first line)))

(defn get-roster-year [roster-list]
  (let [line (extract-roster-info roster-list)]
    (read-string (last line))))

(defn get-player-name-in-roster [plrs-map plr-sym]
  (let [plr (get-player-in-roster plrs-map plr-sym)]
    (get plr :name)))

(defn get-givee-in-roster [plrs-map plr-sym g-year]
  (let [gp (get-gift-pair-in-roster plrs-map plr-sym g-year)]
    (get-givee-in-gift-pair gp)))

(defn get-giver-in-roster [plrs-map plr-sym g-year]
  (let [gp (get-gift-pair-in-roster plrs-map plr-sym g-year)]
    (get-giver-in-gift-pair gp)))

(defn set-givee-in-roster [plrs-map plr-sym g-year ge]
  (if (check-give plrs-map plr-sym g-year ge)
    (let [gr (get-giver-in-roster plrs-map plr-sym g-year)
          gp (make-gift-pair ge gr)]
      (set-gift-pair-in-roster plrs-map plr-sym g-year gp))
    plrs-map))

(defn set-giver-in-roster [plrs-map plr-sym g-year gr]
  (if (check-give plrs-map plr-sym g-year gr)
    (let [ge (get-givee-in-roster plrs-map plr-sym g-year)
          gp (make-gift-pair ge gr)]
      (set-gift-pair-in-roster plrs-map plr-sym g-year gp))
    plrs-map))
