(ns clojure-redpoint.roster
  (:require [clojure-csv.core :as csv]))

;"blackhawks2010.txt"
;"beatles2014.txt"
;"partial-beatles2014.txt"

(def team-name)
(def first-year)
(def roster (atom {}))

(defn make-map [v]
  (if (= 4 (count v))
    (let [[s n ge gr] v]
      (hash-map (keyword s)
                (hash-map :name n
                          :gift-history (vector (hash-map
                                                  :givee (keyword ge)
                                                  :giver (keyword gr))))))))

(defn make-roster [f]
  (let [slurped (slurp f)
        de-spaced (clojure.string/replace slurped #", " ",")
        parsed (csv/parse-csv de-spaced)]
    (def team-name ((first (vec parsed)) 0))
    (def first-year (read-string ((first (vec parsed)) 1)))
    (reset! roster
            (into {} (map make-map parsed)))))

(defn get-player-name [p]
  (get-in (deref roster)
          [p :name]))

(defn get-givee-code [p y]
  (get-in (deref roster)
          [p :gift-history y :givee]))

(defn set-givee-code [p y ge]
  (if (and (contains? (deref roster) p)
           (<= (+ y 1) (count
                         (get-in (deref roster)
                                 [p :gift-history]))))
    (let [gr (get-in (deref roster)
                     [p :gift-history y :giver])]
      (swap! roster assoc-in
             [p :gift-history y]
             {:giver gr :givee ge}))))

(defn get-giver-code [p y]
  (get-in (deref roster)
          [p :gift-history y :giver]))

(defn set-giver-code [p y gr]
  (if (and (contains? (deref roster) p)
           (<= (+ y 1) (count
                         (get-in (deref roster)
                                 [p :gift-history]))))
    (let [ge (get-in (deref roster)
                     [p :gift-history y :givee])]
      (swap! roster assoc-in
             [p :gift-history y]
             {:giver gr :givee ge}))))

(defn add-new-year []
  (doseq [p (keys (deref roster))]
    (swap! roster update-in
           [p :gift-history]
           conj
           {:givee :none :giver :none})))

(defn print-string-giving-roster [gift-year]
  (let [no-givee (atom [])
        no-giver (atom [])
        roster-string (atom [])]
    (swap! roster-string conj team-name " - Year " (+ first-year gift-year) " Gifts:\n\n")
    (doseq [p (keys (into (sorted-map) (deref roster)))]
      (let [player-name (get-player-name p)
            givee-code (get-givee-code p gift-year)
            giver-code (get-giver-code p gift-year)]
        (if (= givee-code :none)
          (swap! no-givee conj p)
          (swap! roster-string conj player-name " is buying for " (get-player-name givee-code) "\n"))
        (if (= giver-code :none)
          (swap! no-giver conj p))))
    (if-not (and (empty? (deref no-givee))
                 (empty? (deref no-giver)))
      (do
        (swap! roster-string conj "\nThere is a logic error in this year's pairings.\nDo you see it?\nIf not... call me and I'll explain!\n\n")
        (doseq [p (deref no-givee)]
          (swap! roster-string conj (get-player-name p) " is giving to no one.\n"))
        (doseq [p (deref no-giver)]
          (swap! roster-string conj (get-player-name p) " is receiving from no one.\n"))))
    (apply str (deref roster-string))))
