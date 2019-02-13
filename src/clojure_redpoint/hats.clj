(ns clojure-redpoint.hats
  (:require [clojure-redpoint.domain :as dom]
            [clojure.spec.alpha :as s]
            [orchestra.spec.test :as ostest]
            [clojure.repl :refer :all]))

(defn make-hat [plrs-map]
  (into [] (keys plrs-map)))
(s/fdef make-hat
        :args (s/cat :plrs-map ::dom/plr-map)
        :ret ::dom/hat)

(defn remove-puck [hat plr-sym]
  (into [] (filter #(not= % plr-sym) hat)))
(s/fdef remove-puck
        :args (s/cat :hat ::dom/hat :plr-sym keyword?)
        :ret ::dom/hat)

(defn discard-puck-givee [discards givee]
  (conj discards givee))
(s/fdef discard-puck-givee
        :args (s/cat :discards ::dom/hat :givee ::dom/givee)
        :ret ::dom/hat)

(defn return-discards [ge-hat discards]
  (into ge-hat discards))
(s/fdef return-discards
        :args (s/cat :ge-hat ::dom/hat :discards ::dom/hat)
        :ret ::dom/hat)

(defn empty-discards [_]
  [])
(s/fdef empty-discards
        :args (s/cat :unused-value any?)
        :ret ::dom/hat)

(s/conform ::dom/hat
           (empty-discards []))

(ostest/instrument)
