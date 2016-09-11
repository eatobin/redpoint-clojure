(ns clojure-redpoint.hats
  (:require [clojure-redpoint.roster :refer :all]))

(defn make-hat [plrs-map]
  (into [] (keys plrs-map)))

(defn remove-puck-givee [givee ge-hat]
  (into [] (remove #{givee} ge-hat)))

(defn remove-puck-giver [giver gr-hat]
  (into [] (remove #{giver} gr-hat)))

(defn discard-puck-givee [givee discards]
  (conj discards givee))

(defn return-discards [discards ge-hat]
  (into ge-hat discards))

(defn empty-discards [discards]
  [])
