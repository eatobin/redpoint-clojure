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
  (remove-puck-givee (deref givee))
  (reset! givee nil))

(defn givee-is-failure []
  (discard-puck (deref givee))
  (reset! givee (draw-puck-givee)))

(defn print-and-ask []
  (println (print-string-giving-roster (deref year)))
  (println "Continue? ('q' to quit): ")
  (read-line))

(defn -main []
  (initialize-state)
  (while (not= (clojure.string/lower-case (print-and-ask)) "q")
    (start-new-year)
    (while (some? (deref giver))
      (while (some? (deref givee))
        (if (and
              (givee-not-self (deref giver) (deref givee))
              (givee-not-recip (deref giver) (deref givee) (deref year))
              (givee-not-repeat (deref giver) (deref givee) (deref year)))
          (givee-is-success)
          (givee-is-failure)))
      (select-new-giver))
    (println))
  (println)
  (println "This was fun!")
  (println "Talk about a position with Redpoint?")
  (println "Please call: Eric Tobin 773-325-1516")
  (println "Thanks! Bye...")
  (println))
