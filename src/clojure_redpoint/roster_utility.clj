(ns clojure-redpoint.roster-utility
  (:require [clojure.string :as cs]
            [clojure-csv.core :as csv]
            [clojure.spec.alpha :as s]
            [orchestra.spec.test :as st]
            [clojure.repl :refer :all]))

(s/def ::roster-seq (s/coll-of vector? :kind seq?))
(s/def ::roster-info-vector (s/coll-of string? :kind vector?))
(s/def ::plrs-list (s/coll-of vector? :kind list?))
(s/def ::givee keyword?)
(s/def ::giver keyword?)
(s/def :unq/gift-pair (s/keys :req-un [::givee ::giver]))
(s/def ::name string?)
(s/def :unq/gift-history (s/coll-of :unq/gift-pair :kind vector?))
(s/def :unq/player (s/keys :req-un [::name :unq/gift-history]))
(s/def ::plr-map-vec (s/tuple string? string? string? string?))
(s/def ::plr-map (s/map-of keyword? :unq/player))

(defn make-roster-seq
  "Returns a lazy roster-seq - or nil on empty string"
  [roster-string]
  (if (= roster-string "")
    nil
    (let [de-spaced (cs/replace roster-string #", " ",")]
      (csv/parse-csv de-spaced))))
(s/fdef make-roster-seq
        :args (s/cat :roster-string string?)
        :ret (s/or :output-seq ::roster-seq
                   :output-nil nil?))

(defn extract-roster-info-vector [roster-string]
  "Returns a vector containing the roster name and year - or nil on empty string"
  (if (= roster-string "")
    nil
    (first (make-roster-seq roster-string))))
(s/fdef extract-roster-info-vector
        :args (s/cat :roster-string string?)
        :ret (s/or :output-vec ::roster-info-vector
                   :output-nil nil?))

(defn extract-players-list [roster-string]
  "Returns a list of vectors - each vector a player symbol, player name, first
  givee and first giver - or nil on empty string"
  (if (= roster-string "")
    nil
    (into () (rest (make-roster-seq roster-string)))))
(s/fdef extract-players-list
        :args (s/cat :roster-string string?)
        :ret (s/or :output-list ::plrs-list
                   :output-nil nil?))

(defn make-gift-pair [givee giver]
  "Returns a gift pair hash map given givee and giver as strings"
  (hash-map
    :givee (keyword givee)
    :giver (keyword giver)))
(s/fdef make-gift-pair
        :args (s/cat :givee string? :giver string?)
        :ret :unq/gift-pair)

(defn make-player [p-name g-hist]
  "Returns a player hash map given a player name (string) and a gift history
  (vector of gift pairs)"
  (hash-map
    :name p-name
    :gift-history g-hist))
(s/fdef make-player
        :args (s/cat :p-name ::name :g-hist :unq/gift-history)
        :ret :unq/player)

(defn make-player-map [[s n ge gr]]
  (let [gp (make-gift-pair ge gr)
        plr (make-player n (vector gp))]
    (hash-map
      (keyword s) plr)))
(s/fdef make-player-map
        :args (s/cat :arg1 ::plr-map-vec)
        :ret ::plr-map)

(defn make-players-map [roster-string]
  (let [pl (extract-players-list roster-string)]
    (into {} (map make-player-map pl))))
(s/fdef make-players-map
        :args (s/cat :roster-string string?)
        :ret ::plr-map)

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
