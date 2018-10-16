(ns clojure-redpoint.main
  (:require [clojure.string :as cs]
            [clojure-redpoint.roster :refer :all]
            [clojure-redpoint.hats :refer :all]
            [clojure-redpoint.rules :refer :all]
            [clojure-redpoint.roster-string-check :refer [scrubbed-roster-string]]
            [clojure.string :as str]
            [clojure-csv.core :as csv]
            [clojure.java.io :as io])
  (:gen-class))

(def a-g-year (atom 0))
(def a-giver (atom :none))
(def a-givee (atom :none))
(def a-plrs-map (atom {}))
(def a-gr-hat (atom []))
(def a-ge-hat (atom []))
(def a-discards (atom []))

(defn exit-now! []
  (System/exit 99))

(defn scrubbed-or-quit
  "test"
  [file-path]
  (if (.exists (io/file file-path))
    (let [[scrubbed err] (scrubbed-roster-string
                           (slurp file-path))]
      (if (nil? err)
        scrubbed
        (do
          (println err)
          (println "Bye..")
          (exit-now!))))
    (do
      (println "The requested file does not exist..")
      (exit-now!))))

(defn draw-puck [hat]
  (when (not= 0 (count hat))
    (rand-nth hat)))

(defn start-new-year []
  (swap! a-g-year inc)
  (swap! a-plrs-map add-year-in-roster)
  (reset! a-gr-hat (make-hat (deref a-plrs-map)))
  (reset! a-ge-hat (make-hat (deref a-plrs-map)))
  (reset! a-giver (draw-puck (deref a-gr-hat)))
  (reset! a-givee (draw-puck (deref a-ge-hat)))
  (swap! a-discards empty-discards))

(defn select-new-giver []
  (swap! a-gr-hat remove-puck (deref a-giver))
  (swap! a-ge-hat return-discards (deref a-discards))
  (swap! a-discards empty-discards)
  (reset! a-giver (draw-puck (deref a-gr-hat)))
  (reset! a-givee (draw-puck (deref a-ge-hat))))

(defn givee-is-success []
  (swap! a-plrs-map set-givee-in-roster (deref a-giver) (deref a-g-year) (deref a-givee))
  (swap! a-plrs-map set-giver-in-roster (deref a-givee) (deref a-g-year) (deref a-giver))
  (swap! a-ge-hat remove-puck (deref a-givee))
  (reset! a-givee nil))

(defn givee-is-failure []
  (swap! a-ge-hat remove-puck (deref a-givee))
  (swap! a-discards discard-puck-givee (deref a-givee))
  (reset! a-givee (draw-puck (deref a-ge-hat))))

(defn errors? [players-map g-year]
  (seq (for [plr-sym (keys (into (sorted-map) players-map))
             :let [giver-code (get-giver-in-roster players-map plr-sym g-year)
                   givee-code (get-givee-in-roster players-map plr-sym g-year)]
             :when (or (= plr-sym giver-code) (= plr-sym givee-code))]
         [plr-sym])))

(defn print-results [players-map g-year]
  (doseq [plr-sym (keys (into (sorted-map) players-map))
          :let [player-name (get-player-name-in-roster players-map plr-sym)
                givee-code (get-givee-in-roster players-map plr-sym g-year)
                givee-name (get-player-name-in-roster players-map givee-code)
                giver-code (get-giver-in-roster players-map plr-sym g-year)]]
    (cond
      (= plr-sym giver-code) (println player-name "is receiving from no one - ERROR")
      (= plr-sym givee-code) (println player-name "is buying for no one - ERROR")
      :else (println player-name "is buying for" givee-name))))

(defn print-string-giving-roster [r-name r-year]
  (println)
  (println r-name "- Year" (+ r-year (deref a-g-year)) "Gifts:")
  (println)
  (when (errors? (deref a-plrs-map) (deref a-g-year))
    (println)
    (println "There is a logic error in this year's pairings.")
    (println "Do you see it?")
    (println "If not... call me and I'll explain!")
    (println))
  (print-results (deref a-plrs-map) (deref a-g-year)))










(defn print-and-ask [r-name r-year]
  (print-string-giving-roster r-name r-year)
  (do
    (println)
    (print "Continue? ('q' to quit): ")
    (flush)
    (read-line)))

(defn -main []
  (reset! a-g-year 0)
  (reset! a-giver :none)
  (reset! a-givee :none)
  (let [players-vector (make-players-vector
                         (scrubbed-or-quit "blackhawks2010.txt"))
        r-name (get-roster-name (scrubbed-or-quit "blackhawks2010.txt"))
        r-year (Integer/parseInt (get-roster-year (scrubbed-or-quit "blackhawks2010.txt")))]
    (reset! a-plrs-map (make-players-map players-vector))
    (reset! a-gr-hat [])
    (reset! a-ge-hat [])
    (reset! a-discards [])
    (while (not= (cs/lower-case (print-and-ask r-name r-year)) "q")
      (start-new-year)
      (while (some? (deref a-giver))
        (while (some? (deref a-givee))
          (if (and
                (givee-not-self? (deref a-giver) (deref a-givee))
                (givee-not-recip? (deref a-giver) (deref a-givee) (deref a-g-year) (deref a-plrs-map))
                (givee-not-repeat? (deref a-giver) (deref a-givee) (deref a-g-year) (deref a-plrs-map)))
            (givee-is-success)
            (givee-is-failure)))
        (select-new-giver)))
    (println)
    (println "This was fun!")
    (println "Talk about a position with Redpoint?")
    (println "Please call: Eric Tobin 773-325-1516")
    (println)))
