(ns clojure-redpoint.hats
  (:require [clojure-redpoint.roster :refer :all]))

(defn make-hat [plrs-map]
  (into [] (keys plrs-map)))

(defn remove-puck-givee [ge-hat givee]
  (into [] (remove #{givee} ge-hat)))

(defn remove-puck-giver [gr-hat giver]
  (into [] (remove #{giver} gr-hat)))

(defn discard-puck-givee [discards givee]
  (conj discards givee))

(defn return-discards [ge-hat discards]
  (into ge-hat discards))

(defn empty-discards [_]
  [])
