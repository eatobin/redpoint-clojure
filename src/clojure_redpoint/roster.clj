(ns clojure-redpoint.roster
  (:require [clojure-csv.core :as csv]))

;"blackhawks2010.txt"
;"roster-test.txt"

(def roster (atom {}))

(defn make-map [v]
  (if (= 2 (count v))
    (let [[tn fy] v]
      (hash-map :team-name tn :first-year (read-string fy)))
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

(defn add-history [p y ge gr]
  (swap! roster assoc-in
         [p :gift-history y]
         {:givee ge :giver gr}))
