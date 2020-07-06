(ns clojure-redpoint.core
  (:require [clojure.data.json :as json]
            [clojure-redpoint.roster :as ros]
            [clojure-redpoint.hats :as hat]
            [clojure-redpoint.rules :as rule]
            [clojure.string :as cs]
            [clojure.java.io :as io])
  (:gen-class))

(def a-g-year (atom 0))
(def a-giver (atom nil))
(def a-givee (atom nil))
(def a-players (atom {}))
(def a-gr-hat (atom #{}))
(def a-ge-hat (atom #{}))
(def a-discards (atom #{}))
(def a-roster-name (atom ""))
(def a-roster-year (atom 0))
(def file-path "resources/blackhawks.json")

(defn exit-now!
  []
  (System/exit 99))

(defn my-value-reader
  [key value]
  (if (or (= key :givee)
          (= key :giver))
    (keyword value)
    value))

(defn roster-or-quit
  "Return a roster or quit the program if the file does not exist
  or if the string cannot be scrubbed"
  [file-path]
  (if (.exists (io/file file-path))
    (let [roster (json/read (io/reader file-path)
                            :value-fn my-value-reader
                            :key-fn keyword)]
      (do
        (reset! a-roster-name (roster :roster-name))
        (reset! a-roster-year (roster :roster-year))
        (reset! a-players (roster :players))))
    (do
      (println "The requested file does not exist..")
      (exit-now!))))

(defn draw-puck
  [hat]
  (when (not (empty? hat))
    ((shuffle hat) 0)))

(defn start-new-year
  []
  (swap! a-g-year inc)
  (swap! a-players ros/add-year)
  (reset! a-gr-hat (hat/make-hat (deref a-players)))
  (reset! a-ge-hat (hat/make-hat (deref a-players)))
  (reset! a-giver (draw-puck (deref a-gr-hat)))
  (reset! a-givee (draw-puck (deref a-ge-hat)))
  (reset! a-discards #{}))

(defn select-new-giver
  []
  (swap! a-gr-hat hat/remove-puck (deref a-giver))
  (swap! a-ge-hat hat/return-discards (deref a-discards))
  (reset! a-discards #{})
  (reset! a-giver (draw-puck (deref a-gr-hat)))
  (reset! a-givee (draw-puck (deref a-ge-hat))))

(defn givee-is-success
  []
  (swap! a-players ros/update-givee (deref a-giver) (deref a-g-year) (deref a-givee))
  (swap! a-players ros/update-giver (deref a-givee) (deref a-g-year) (deref a-giver))
  (swap! a-ge-hat hat/remove-puck (deref a-givee))
  (reset! a-givee nil))

(defn givee-is-failure
  []
  (swap! a-ge-hat hat/remove-puck (deref a-givee))
  (swap! a-discards hat/discard-givee (deref a-givee))
  (reset! a-givee (draw-puck (deref a-ge-hat))))

(defn errors? []
  (seq (for [plr-sym (keys (into (sorted-map) (deref a-players)))
             :let [giver-code (ros/get-giver (deref a-players) plr-sym (deref a-g-year))
                   givee-code (ros/get-givee (deref a-players) plr-sym (deref a-g-year))]
             :when (or (= plr-sym giver-code) (= plr-sym givee-code))]
         plr-sym)))

(defn print-results []
  (doseq [plr-sym (keys (into (sorted-map) (deref a-players)))
          :let [player-name (ros/get-player-name (deref a-players) plr-sym)
                givee-code (ros/get-givee (deref a-players) plr-sym (deref a-g-year))
                givee-name (ros/get-player-name (deref a-players) givee-code)
                giver-code (ros/get-giver (deref a-players) plr-sym (deref a-g-year))]]
    (cond
      (and (= plr-sym givee-code) (= plr-sym giver-code)) (println player-name "is **buying** for nor **receiving** from anyone - **ERROR**")
      (= plr-sym giver-code) (println player-name "is **receiving** from no one - **ERROR**")
      (= plr-sym givee-code) (println player-name "is **buying** for no one - **ERROR**")
      :else (println player-name "is buying for" givee-name))))

(defn print-string-giving-roster [r-name r-year]
  (println)
  (println r-name "- Year" (+ r-year (deref a-g-year)) "Gifts:")
  (println)
  (when (errors?)
    (println)
    (println "There is a logic error in this year's pairings.")
    (println "Do you see it?")
    (println "If not... call me and I'll explain!")
    (println)
    (println))
  (print-results))

(defn print-and-ask [r-name r-year]
  (print-string-giving-roster r-name r-year)
  (do
    (println)
    (print "Continue? ('q' to quit): ")
    (flush)
    (read-line)))

(defn -main []
  (reset! a-g-year 0)
  (reset! a-giver nil)
  (reset! a-givee nil)
  (reset! a-gr-hat #{})
  (reset! a-ge-hat #{})
  (reset! a-discards #{})
  (roster-or-quit file-path)
  (let [r-name (deref a-roster-name)
        r-year (deref a-roster-year)]
    (while (not= (cs/lower-case (print-and-ask r-name r-year)) "q")
      (start-new-year)
      (while (some? (deref a-giver))
        (while (some? (deref a-givee))
          (if (and
                (rule/givee-not-self? (deref a-giver) (deref a-givee))
                (rule/givee-not-recip? (deref a-giver) (deref a-givee) (deref a-g-year) (deref a-players))
                (rule/givee-not-repeat? (deref a-giver) (deref a-givee) (deref a-g-year) (deref a-players)))
            (givee-is-success)
            (givee-is-failure)))
        (select-new-giver)))
    (println)
    (println "This was fun!")
    (println "Talk about a position with Redpoint?")
    (println "Please call: Eric Tobin 773-679-6617")
    (println)))
