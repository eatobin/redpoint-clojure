(ns clojure-redpoint.main
  (:require [clojure.string :as cs]
            [clojure-redpoint.roster-utility :refer :all]
            [clojure-redpoint.roster :refer :all]
            [clojure-redpoint.hats :refer :all]
            [clojure-redpoint.rules :refer :all])
  (:gen-class))

(def a-g-year (atom 0))
(def a-giver (atom :none))
(def a-givee (atom :none))
(def a-plrs-map (atom {}))
(def a-gr-hat (atom []))
(def a-ge-hat (atom []))
(def a-discards (atom []))

(defn draw-puck-givee [ge-hat]
  (when (not= 0 (count ge-hat))
    (rand-nth ge-hat)))

(defn draw-puck-giver [gr-hat]
  (when (not= 0 (count gr-hat))
    (rand-nth gr-hat)))

(defn read-file-into-string [file-path]
  (slurp file-path))

(defn start-new-year []
  (swap! a-g-year inc)
  (swap! a-plrs-map add-year-in-roster)
  (reset! a-gr-hat (make-hat (deref a-plrs-map)))
  (reset! a-ge-hat (make-hat (deref a-plrs-map)))
  (reset! a-giver (draw-puck-giver (deref a-gr-hat)))
  (reset! a-givee (draw-puck-givee (deref a-ge-hat)))
  (swap! a-discards empty-discards))

(defn select-new-giver []
  (swap! a-gr-hat remove-puck-giver (deref a-giver))
  (swap! a-ge-hat return-discards (deref a-discards))
  (swap! a-discards empty-discards)
  (reset! a-giver (draw-puck-giver (deref a-gr-hat)))
  (reset! a-givee (draw-puck-givee (deref a-ge-hat))))

(defn givee-is-success []
  (swap! a-plrs-map set-givee-in-roster (deref a-giver) (deref a-g-year) (deref a-givee))
  (swap! a-plrs-map set-giver-in-roster (deref a-givee) (deref a-g-year) (deref a-giver))
  (swap! a-ge-hat remove-puck-givee (deref a-givee))
  (reset! a-givee nil))

(defn givee-is-failure []
  (swap! a-ge-hat remove-puck-givee (deref a-givee))
  (swap! a-discards discard-puck-givee (deref a-givee))
  (reset! a-givee (draw-puck-givee (deref a-ge-hat))))


(defn print-string-giving-roster1 [r-name r-year]
  (println)
  (println r-name "- Year" (+ r-year (deref a-g-year)) "Gifts:")
  (println)
  (doseq [plr-sym (keys (into (sorted-map) (deref a-plrs-map)))
          :let [player-name (get-player-name-in-roster (deref a-plrs-map) plr-sym)
                givee-code (get-givee-in-roster (deref a-plrs-map) plr-sym (deref a-g-year))
                givee-name (get-player-name-in-roster (deref a-plrs-map) givee-code)]
          :when (not= givee-code :none)]
    (println player-name "is buying for" givee-name)))




(defn print-string-giving-roster [r-name r-year]
  (let [no-givee (atom [])
        no-giver (atom [])
        roster-string (atom [])]
    (swap! roster-string conj "\n" r-name " - Year " (+ r-year (deref a-g-year)) " Gifts:\n\n")
    (doseq [plr-sym (keys (into (sorted-map) (deref a-plrs-map)))]
      (let [player-name (get-player-name-in-roster (deref a-plrs-map) plr-sym)
            givee-code (get-givee-in-roster (deref a-plrs-map) plr-sym (deref a-g-year))
            giver-code (get-giver-in-roster (deref a-plrs-map) plr-sym (deref a-g-year))]
        (if (= givee-code :none)
          (swap! no-givee conj plr-sym)
          (swap! roster-string conj player-name " is buying for " (get-player-name-in-roster (deref a-plrs-map) givee-code) "\n"))
        (if (= giver-code :none)
          (swap! no-giver conj plr-sym))))
    (if-not (and (empty? (deref no-givee))
                 (empty? (deref no-giver)))
      (do
        (swap! roster-string conj "\nThere is a logic error in this year's pairings.\nDo you see it?\nIf not... call me and I'll explain!\n\n")
        (doseq [plr-sym (deref no-givee)]
          (swap! roster-string conj (get-player-name-in-roster (deref a-plrs-map) plr-sym) " is buying for no one.\n"))
        (doseq [plr-sym (deref no-giver)]
          (swap! roster-string conj (get-player-name-in-roster (deref a-plrs-map) plr-sym) " is receiving from no one.\n"))))
    (apply str (deref roster-string))))

(defn print-and-ask [r-name r-year]
  (println (print-string-giving-roster1 r-name r-year))
  (println "Continue? ('q' to quit): ")
  (read-line))

(defn -main []
  (reset! a-g-year 0)
  (reset! a-giver :none)
  (reset! a-givee :none)
  (let [roster-list (make-roster-list
                      (read-file-into-string "blackhawks2010.txt"))
        r-name (get-roster-name roster-list)
        r-year (get-roster-year roster-list)]
    (reset! a-plrs-map (make-players-map roster-list))
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
        (select-new-giver))
      (println))
    (println)
    (println "This was fun!")
    (println "Talk about a position with Redpoint?")
    (println "Please call: Eric Tobin 773-325-1516")))
