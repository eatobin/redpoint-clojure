(ns clojure-redpoint.hat
  (:require [clojure-redpoint.roster :refer :all]))

(defn chkr [top]
  (range top (- top 5) -1))
(chkr 2)

(filter #(>= % 0) (chkr 3))
(filter (fn [n] (>= n 0)) (chkr 3))
