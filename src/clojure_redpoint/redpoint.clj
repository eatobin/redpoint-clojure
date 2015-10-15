(ns clojure-redpoint.redpoint
  (:require [clojure-redpoint.roster :refer :all]
            [clojure-redpoint.hats :refer :all]
            [clojure-redpoint.rules :refer :all])
  (:gen-class))

(def year (atom 0))
(def giver (atom :none))
(def givee (atom :none))

(defn initialize-state []
  (reset! year 0)
  (reset! giver :none)
  (reset! givee :none)
  (make-roster "blackhawks2010.txt"))

(defn year-runner []
  ;TODO
  )

(defn start-new-year []
  (swap! year inc)
  (add-new-year)
  (make-hats roster)
  (reset! giver (draw-puck-giver))
  (reset! givee (draw-puck-givee)))

(defn select-new-giver []
  (remove-puck-giver (deref giver))
  (return-discards)
  (reset! giver (draw-puck-giver))
  (reset! givee (draw-puck-givee)))

(defn givee-is-success []
  (set-givee-code (deref giver)
                  (deref year) (deref givee))
  (set-giver-code (deref givee)
                  (deref year) (deref giver))
  (remove-puck-givee (deref givee)))

(defn givee-is-failure []
  (discard-puck (deref givee))
  (draw-puck-givee))

;(defn ask []
;  (if (#(= % "q") (clojure.string/lower-case (read-line)))
;    true
;    (do
;      (println "That is not a valid response.\nPlease re-enter.")
;      (println)
;      (recur))))

(defn print-and-ask [year]
  (println (print-string-giving-roster year))
  (println "Continue? ('q' to quit): ")
  (read-line))
