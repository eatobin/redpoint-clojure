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

(defn make-roster-seq
  "Returns a lazy roster-seq - or nil on error"
  [roster-string]
  (if (or (= roster-string "") (nil? roster-string))
    nil
    (let [de-spaced (cs/replace roster-string #", " ",")]
      (csv/parse-csv de-spaced))))
;(s/fdef make-roster-seq
;        :args (s/or :input-str (s/cat :roster-string string?)
;                    :input-nil (s/cat :roster-string nil?))
;        :ret (s/or :output-seq ::roster-seq
;                   :output-nil nil?))
(s/fdef make-roster-seq
        :args (s/cat :roster-string (s/nilable string?))
        :ret (s/or :output-seq ::roster-seq
                   :output-nil nil?))

(defn extract-roster-info-vector [roster-string]
  "Returns a vector containing the roster name and year - or nil on error"
  (if (or (= roster-string "") (nil? roster-string))
    nil
    (first (make-roster-seq roster-string))))
(s/fdef extract-roster-info-vector
        :args (s/or :input-str (s/cat :roster-string string?)
                    :input-nil (s/cat :roster-string nil?))
        :ret (s/or :output-vec ::roster-info-vector
                   :output-nil nil?))

(defn extract-players-list [roster-string]
  "Returns a list of vectors - each vector a player symbol, player name, first
  givee and first giver - or nil on error"
  (if (or (= roster-string "") (nil? roster-string))
    nil
    (into () (rest (make-roster-seq roster-string)))))
(s/fdef extract-players-list
        :args (s/or :input-str (s/cat :roster-string string?)
                    :input-nil (s/cat :roster-string nil?))
        :ret (s/or :output-list ::plrs-list
                   :output-nil nil?))

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
