(ns clojure-redpoint.roster
  (:require [clojure-redpoint.domain :as dom]
            [clojure-redpoint.roster-utility :refer :all]
            [clojure.spec.alpha :as s]
            [orchestra.spec.test :as st]
            [clojure.repl :refer :all]))

(defn get-roster-name
  "Return the roster name from a roster sequence"
  [roster-seq]
  (let [line (extract-roster-info-vector roster-seq)]
    (first line)))
(s/fdef get-roster-name
        :args (s/cat :roster-seq ::dom/roster-seq)
        :ret string?)

(st/instrument)

;;(defn get-roster-year [roster-list]
;;  (let [line (extract-roster-info-vector roster-list)]
;;    (read-string (last line))))
;;
;;(defn get-player-name-in-roster [plrs-map plr-sym]
;;  (let [plr (get-player-in-roster plrs-map plr-sym)]
;;    (get plr :name)))
;;
;;(defn get-givee-in-roster [plrs-map plr-sym g-year]
;;  (let [gp (get-gift-pair-in-roster plrs-map plr-sym g-year)]
;;    (get-givee-in-gift-pair gp)))
;;
;;(defn get-giver-in-roster [plrs-map plr-sym g-year]
;;  (let [gp (get-gift-pair-in-roster plrs-map plr-sym g-year)]
;;    (get-giver-in-gift-pair gp)))
;;
;;(defn set-givee-in-roster [plrs-map plr-sym g-year ge]
;;  (if (check-give plrs-map plr-sym g-year ge)
;;    (let [gr (get-giver-in-roster plrs-map plr-sym g-year)
;;          gp (make-gift-pair ge gr)]
;;      (set-gift-pair-in-roster plrs-map plr-sym g-year gp))
;;    plrs-map))
;;
;;(defn set-giver-in-roster [plrs-map plr-sym g-year gr]
;;  (if (check-give plrs-map plr-sym g-year gr)
;;    (let [ge (get-givee-in-roster plrs-map plr-sym g-year)
;;          gp (make-gift-pair ge gr)]
;;      (set-gift-pair-in-roster plrs-map plr-sym g-year gp))
;;    plrs-map))
;
;(st/instrument)
;
;
;;(s/conform vector?
;;           (extract-roster-info-vector rs))
;;(s/conform nil?
;;           (extract-roster-info-vector ""))
;;(s/conform :unq/gift-pair
;;           (make-gift-pair "one" "two"))
;;(s/conform ::plrs-list
;;           (extract-players-list rs))
;;(s/conform ::plrs-list
;;           (extract-players-list ""))
;;(def x (make-gift-pair "joe" "bob"))
;;(def y (make-gift-pair "joey" "bobby"))
;;(def h [x y])
;;(s/conform :unq/gift-history h)
;;(s/conform :unq/player
;;           (make-player "eric" h))
;;(s/conform ::plr-map (make-player-map ["s" "n" "ge" "gr"]))
;;(def pm (make-players-map rs))
;;(s/conform (s/or :found :unq/player
;;                 :not-found nil?)
;;           (get-player-in-roster pm :GeoHar))
;;; =>
;;; [:found
;;;  {:name "George Harrison", :gift-history [{:giver :PauMcc, :givee :RinSta}]}]
;;(s/conform (s/or :found :unq/player
;;                 :not-found nil?)
;;           (get-player-in-roster pm :GeoHarX))
;;; => [:not-found nil]
;;(s/conform ::givee
;;           (get-givee-in-gift-pair {:giver :PauMcc, :givee :RinSta}))
;;(s/conform :unq/player
;;           (set-gift-history-in-player [{:giver :RinSta, :givee :PauMcc}] {:name "Ringo Starr", :gift-history [{:giver :GeoHar, :givee :JohLen}]}))
;;(stest/check `set-gift-pair-in-gift-history)
