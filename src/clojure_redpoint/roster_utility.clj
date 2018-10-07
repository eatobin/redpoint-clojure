;(ns clojure-redpoint.roster-utility
;  (:require [clojure-redpoint.domain :as dom]
;            [clojure-csv.core :as csv]
;            [clojure.spec.alpha :as s]
;            [orchestra.spec.test :as st]
;            [clojure.repl :refer :all]
;            [clojure.string :as str]))
;
;;(def roster-string "The Beatles, 2014\nRinSta, Ringo Starr, JohLen, GeoHar\nJohLen, John Lennon, PauMcc, RinSta\nGeoHar, George Harrison, RinSta, PauMcc\nPauMcc, Paul McCartney, GeoHar, JohLen\n")
;;(def roster-string-bad-length "The Beatles, 2014\nRinSta, Ringo Starr, JohLen, GeoHar\nJohLen, John Lennon, PauMcc, RinSta\nGeoHar, George Harrison, RinSta, PauMcc\n")
;;(def roster-string-bad-info1 "The Beatles\nRinSta, Ringo Starr, JohLen, GeoHar\nJohLen, John Lennon, PauMcc, RinSta\nGeoHar, George Harrison, RinSta, PauMcc\nPauMcc, Paul McCartney, GeoHar, JohLen\n")
;;(def roster-string-bad-info2 ",2014\nRinSta, Ringo Starr, JohLen, GeoHar\nJohLen, John Lennon, PauMcc, RinSta\nGeoHar, George Harrison, RinSta, PauMcc\nPauMcc, Paul McCartney, GeoHar, JohLen\n")
;;(def roster-string-bad-info3 "The Beatles, 2096\nRinSta, Ringo Starr, JohLen, GeoHar\nJohLen, John Lennon, PauMcc, RinSta\nGeoHar, George Harrison, RinSta, PauMcc\nPauMcc, Paul McCartney, GeoHar, JohLen\n")
;;(def roster-string-bad-info4 "The Beatles, 1896\nRinSta, Ringo Starr, JohLen, GeoHar\nJohLen, John Lennon, PauMcc, RinSta\nGeoHar, George Harrison, RinSta, PauMcc\nPauMcc, Paul McCartney, GeoHar, JohLen\n")
;;(def roster-string-bad-info5 "The Beatles, 2014P\nRinSta, Ringo Starr, JohLen, GeoHar\nJohLen, John Lennon, PauMcc, RinSta\nGeoHar, George Harrison, RinSta, PauMcc\nPauMcc, Paul McCartney, GeoHar, JohLen\n")
;;(def roster-string-bad-info6 "\nRinSta, Ringo Starr, JohLen, GeoHar\nJohLen, John Lennon, PauMcc, RinSta\nGeoHar, George Harrison, RinSta, PauMcc\nPauMcc, Paul McCartney, GeoHar, JohLen\n")
;;(def roster-string-bad-info7 "The Beatles, 2014\nRinStaX, Ringo Starr, JohLen, GeoHar\nJohLen, John Lennon, PauMcc, RinSta\nGeoHar, George Harrison, RinSta, PauMcc\nPauMcc, Paul McCartney, GeoHar, JohLen\n")
;;(def roster-string-bad-info8 "The Beatles, 2014\nRinSta, Ringo Starr, JohLen\nJohLen, John Lennon, PauMcc, RinSta\nGeoHar, George Harrison, RinSta, PauMcc\nPauMcc, Paul McCartney, GeoHar, JohLen\n")
;
;
;
;
;(defn make-gift-pair
;  "Returns a gift pair hash map given givee and giver as strings"
;  [givee giver]
;  (hash-map
;    :givee (keyword givee)
;    :giver (keyword giver)))
;(s/fdef make-gift-pair
;        :args (s/cat :givee string? :giver string?)
;        :ret :unq/gift-pair)
;
;(defn make-player
;  "Returns a player hash map given a player name (string) and a gift history
;  (vector of gift pairs)"
;  [p-name g-hist]
;  (hash-map
;    :name p-name
;    :gift-history g-hist))
;(s/fdef make-player
;        :args (s/cat :p-name ::dom/name :g-hist :unq/gift-history)
;        :ret :unq/player)
;
;(defn make-player-map
;  "Returns a hash map linking a player symbol (key) to a player hash map (value)
;  given a player symbol, name, initial givee and initial giver - all strings"
;  [[s n ge gr]]
;  (let [gp (make-gift-pair ge gr)
;        plr (make-player n (vector gp))]
;    (hash-map
;      (keyword s) plr)))
;(s/fdef make-player-map
;        :args (s/cat :arg1 ::dom/plr-map-vec)
;        :ret ::dom/plr-map)
;
;(defn make-players-map
;  "Returns a hash map of multiple players given a players-vector"
;  [players-vector]
;  (into {} (map make-player-map players-vector)))
;(s/fdef make-players-map
;        :args (s/cat :players-vector ::dom/plrs-vector)
;        :ret ::dom/plr-map)
;
;(defn get-player-in-roster
;  "Returns a player given a players map and a player symbol"
;  [plrs-map plr-sym]
;  (get plrs-map plr-sym))
;(s/fdef get-player-in-roster
;        :args (s/cat :plrs-map ::dom/plr-map :plr-sym keyword?)
;        :ret :unq/player)
;
;(defn get-gift-history-in-player
;  "Returns a gift history given a player"
;  [plr]
;  (get plr :gift-history))
;(s/fdef get-gift-history-in-player
;        :args (s/cat :plr :unq/player)
;        :ret :unq/gift-history)
;
;(defn get-gift-pair-in-gift-history
;  "Returns a gift pair given a gift history and a gift year"
;  [g-hist g-year]
;  (get g-hist g-year))
;(s/fdef get-gift-pair-in-gift-history
;        :args (s/and
;                (s/cat :g-hist :unq/gift-history
;                       :g-yearX (s/and int? #(> % -1)))
;                #(<= (:g-yearX %) (count (:g-hist %))))
;        :ret :unq/gift-pair)
;
;(defn get-gift-pair-in-roster
;  "Returns a gift pair given a player map, a player symbol and a gift year"
;  [plrs-map plr-sym g-year]
;  (let [plr (get-player-in-roster plrs-map plr-sym)
;        gh (get-gift-history-in-player plr)]
;    (get-gift-pair-in-gift-history gh g-year)))
;(s/fdef get-gift-pair-in-roster
;        :args (s/cat :plrs-map ::dom/plr-map
;                     :plr-sym keyword?
;                     :g-year (s/and int? #(> % -1)))
;        :ret :unq/gift-pair)
;
;(defn get-givee-in-gift-pair
;  "Returns a givee given a gift pair"
;  [g-pair]
;  (get g-pair :givee))
;(s/fdef get-givee-in-gift-pair
;        :args (s/cat :g-pair :unq/gift-pair)
;        :ret ::dom/givee)
;
;(defn get-giver-in-gift-pair
;  "Returns a giver given a gift pair"
;  [g-pair]
;  (get g-pair :giver))
;(s/fdef get-giver-in-gift-pair
;        :args (s/cat :g-pair :unq/gift-pair)
;        :ret ::dom/giver)
;
;(defn set-gift-history-in-player
;  "Sets a gift history into the provided player"
;  [g-hist plr]
;  (assoc plr :gift-history g-hist))
;(s/fdef set-gift-history-in-player
;        :args (s/cat :g-hist :unq/gift-history
;                     :plr :unq/player)
;        :ret :unq/player)
;
;(defn set-gift-pair-in-gift-history
;  "Returns a gift history with the provided gift pair at the supplied year"
;  [g-hist g-year g-pair]
;  (assoc g-hist g-year g-pair))
;(s/fdef set-gift-pair-in-gift-history
;        :args (s/and
;                (s/cat :g-hist :unq/gift-history
;                       :g-year (s/and int? #(> % -1))
;                       :g-pair :unq/gift-pair)
;                #(<= (:g-year %) (count (:g-hist %))))
;        :ret :unq/gift-history)
;
;(defn set-gift-pair-in-roster
;  "Returns a player roster with a gift pair set for the player and year"
;  [plrs-map plr-sym g-year g-pair]
;  (let [plr (get-player-in-roster plrs-map plr-sym)
;        gh (get-gift-history-in-player plr)
;        ngh (set-gift-pair-in-gift-history gh g-year g-pair)
;        nplr (set-gift-history-in-player ngh plr)]
;    (assoc plrs-map plr-sym nplr)))
;(s/fdef set-gift-pair-in-roster
;        :args (s/cat :plrs-map ::dom/plr-map
;                     :plr-sym keyword?
;                     :g-year (s/and int? #(> % -1))
;                     :g-pair :unq/gift-pair)
;        :ret ::dom/plr-map)
;
;(defn check-give
;  "Returns true if a players map contains a valid giver,
;  givee and year"
;  [plrs-map giver g-year givee]
;  (let [plr (get-player-in-roster plrs-map giver)
;        gh (get-gift-history-in-player plr)
;        h-len (count gh)]
;    (and (contains? plrs-map giver)
;         (contains? plrs-map givee)
;         (<= (+ g-year 1) h-len))))
;(s/fdef check-give
;        :args (s/cat :plrs-map ::dom/plr-map
;                     :giver keyword?
;                     :g-year (s/and int? #(> % -1))
;                     :givee keyword?)
;        :ret boolean?)
;
;(defn add-year-in-player
;  "Adds a new placeholder year to the end of a player's gift history"
;  [plr]
;  (let [gh (get-gift-history-in-player plr)
;        ngh (conj gh {:giver :none, :givee :none})]
;    (set-gift-history-in-player ngh plr)))
;(s/fdef add-year-in-player
;        :args (s/cat :plr :unq/player)
;        :ret :unq/player)
;
;(defn add-year-in-roster
;  "Add a year for each player in roster"
;  [plrs-map]
;  (into {}
;        (for [[k v] plrs-map]
;          [k (add-year-in-player v)])))
;(s/fdef add-year-in-roster
;        :args (s/cat :plrs-map ::dom/plr-map)
;        :ret ::dom/plr-map)
;
;(st/instrument)
