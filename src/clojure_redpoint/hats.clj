(ns clojure-redpoint.hats
  (:require [clojure-redpoint.roster :as ros]
            [clojure.spec.alpha :as s]
            [orchestra.spec.test :as ostest]
            [clojure.repl :refer :all]))

(s/def ::hat (s/coll-of keyword? :kind vector?))

(defn make-hat [plrs-map]
  (into [] (keys plrs-map)))
(s/fdef make-hat
        :args (s/cat :plrs-map ::ros/plr-map)
        :ret ::hat)

(defn remove-puck [hat plr-sym]
  (into [] (filter #(not= % plr-sym) hat)))
(s/fdef remove-puck
        :args (s/cat :hat ::hat :plr-sym keyword?)
        :ret ::hat)

(defn discard-puck-givee [discards givee]
  (conj discards givee))
(s/fdef discard-puck-givee
        :args (s/cat :discards ::hat :givee ::ros/givee)
        :ret ::hat)

(defn return-discards [ge-hat discards]
  (into ge-hat discards))
(s/fdef return-discards
        :args (s/cat :ge-hat ::hat :discards ::hat)
        :ret ::hat)

(defn empty-discards [_]
  [])
(s/fdef empty-discards
        :args (s/cat :unused-value any?)
        :ret ::hat)

(s/conform ::hat
           (empty-discards []))

(ostest/instrument)
