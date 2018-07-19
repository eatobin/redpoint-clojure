(ns clojure-redpoint.roster-utility
  (:require [clojure.string :as cs]
            [clojure-csv.core :as csv]
            [clojure.spec.alpha :as s]
            [orchestra.spec.test :as st]))

(s/def ::roster-seq (s/coll-of vector? :kind seq?))

(defn make-roster-seq
  "Returns a lazy roster-seq"
  [roster-string]
  (if (or (= roster-string "") (nil? roster-string))
    nil
    (let [de-spaced (cs/replace roster-string #", " ",")]
      (csv/parse-csv de-spaced))))
(s/fdef make-roster-seq
        :args (s/or :input-str (s/cat :roster-string string?)
                    :input-nil (s/cat :roster-string nil?))
        :ret (s/or :output-seq ::roster-seq
                   :output-nil nil?))

(defn- extract-roster-info-vector [roster-string]
  (first (make-roster-seq roster-string)))
(s/fdef extract-roster-info-vector
        :args (s/cat :roster-string string?)
        :ret (s/or :found ::roster-info-vector
                   :not-found nil?))

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
