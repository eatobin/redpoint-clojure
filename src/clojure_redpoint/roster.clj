(ns clojure-redpoint.roster
  (:require [clojure-csv.core :as csv]))

(def file "blackhawks2010.txt")

(def parsed-team
  (let [slurped (slurp file)
        de-spaced (clojure.string/replace slurped #", " ",")]
    (csv/parse-csv de-spaced)))

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

(def roster (into {} (map make-map parsed-team)))

(defn add-history [p y ge gr]
  (assoc-in roster [p :gift-history y]
            {:givee ge :giver gr}))
