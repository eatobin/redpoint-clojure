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

;(defn select-new-giver []
;  (remove-puck-giver (deref giver))
;  (return-discards)
;  (reset! giver (draw-puck-giver))
;  (reset! givee (draw-puck-givee)))
;
;(defn givee-is-success []
;  (set-givee-code (deref giver)
;                  (deref year) (deref givee))
;  (set-giver-code (deref givee)
;                  (deref year) (deref giver))
;  (remove-puck-givee (deref givee))
;  (reset! givee nil))
;
;(defn givee-is-failure []
;  (discard-puck (deref givee))
;  (reset! givee (draw-puck-givee)))
;
;(defn print-and-ask []
;  (println (print-string-giving-roster (deref year)))
;  (println "Continue? ('q' to quit): ")
;  (read-line))
;
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
    (start-new-year)
    ;  (while (not= (cs/lower-case (print-and-ask)) "q")
    ;    (start-new-year)
    ;    (while (some? (deref giver))
    ;      (while (some? (deref givee))
    ;        (if (and
    ;              (givee-not-self? (deref giver) (deref givee))
    ;              (givee-not-recip? (deref giver) (deref givee) (deref year))
    ;              (givee-not-repeat? (deref giver) (deref givee) (deref year)))
    ;          (givee-is-success)
    ;          (givee-is-failure)))
    ;      (select-new-giver))
    ;    (println))
    ;  (println)
    ;  (println "This was fun!")
    ;  (println "Talk about a position with Redpoint?")
    ;  (println "Please call: Eric Tobin 773-325-1516")
    (println (deref a-plrs-map))
    (println)))


;(defn print-string-giving-roster [gift-year]
;  (let [no-givee (atom [])
;        no-giver (atom [])
;        roster-string (atom [])]
;    (swap! roster-string conj team-name " - Year " (+ first-year gift-year) " Gifts:\n\n")
;    (doseq [p (keys (into (sorted-map) (deref roster)))]
;      (let [player-name (get-player-name p)
;            givee-code (get-givee-code p gift-year)
;            giver-code (get-giver-code p gift-year)]
;        (if (= givee-code :none)
;          (swap! no-givee conj p)
;          (swap! roster-string conj player-name " is buying for " (get-player-name givee-code) "\n"))
;        (if (= giver-code :none)
;          (swap! no-giver conj p))))
;    (if-not (and (empty? (deref no-givee))
;                 (empty? (deref no-giver)))
;      (do
;        (swap! roster-string conj "\nThere is a logic error in this year's pairings.\nDo you see it?\nIf not... call me and I'll explain!\n\n")
;        (doseq [p (deref no-givee)]
;          (swap! roster-string conj (get-player-name p) " is buying for no one.\n"))
;        (doseq [p (deref no-giver)]
;          (swap! roster-string conj (get-player-name p) " is receiving from no one.\n"))))
;    (apply str (deref roster-string))))


;(deftest draw-puck-givee-test
;  (is (some?
;        (some #{(draw-puck-givee)} (deref givee-hat))))
;  (reset! givee-hat [])
;  (is (nil? (draw-puck-givee))))
