(ns clojure-redpoint.roster
  (:require [clojure-csv.core :as csv]))

;(def team-name "placeholder")
;(def first-year 2014)

(def roster-list {:TroBro
                  {:name "Troy Brouwer"
                   :gift-history
                         [{:givee :DavBol
                           :giver :JoeQue}]}
                  :JoeQue
                  {:name "Joel Quenneville"
                   :gift-history
                         [{:givee :TroBro
                           :giver :AndLad}]}})

(get-in {:ericky
         {:name    "Eric"
          :history [[:scott :brenda] [:spot :chula]]}
         :kareny
         {:name    "Karen"
          :history [[:Sally :Maggie] [:puff :fluffy]]}}
        [:kareny :history 1 0])

(assoc-in {:ericky
           {:name    "Eric"
            :history [[:scott :brenda] [:spot :chula]]}
           :kareny
           {:name    "Karen"
            :history [[:Sally :Maggie] [:puff :fluffy]]}}
          [:kareny :history 2] [:neru :jacket])

(get-in roster-list [:TroBro :name])
(get-in roster-list [:TroBro :gift-history 0])
(get-in roster-list [:TroBro :gift-history 0 :giver])
(assoc-in roster-list [:TroBro :gift-history 1]
          {:givee :givee3 :giver :giver3})

(def parsed (csv/parse-csv (clojure.string/replace
                             (slurp "blackhawks2010.txt") #", " ",")))
(second parsed)
;;=> ["TroBro" "Troy Brouwer" "DavBol" "JoeQue"]
(keyword "TroBro")
;;=> :TroBro

(let [[s n j] ["EriTob" "Eric Tobin" "junk"]]
  (vector (keyword s) n (keyword j)))

(def v ["EriTob" "Eric Tobin" "junk"])
(def v1 ["EriTob" "Eric Tobin"])

(let [[s n j] v]
  (vector (keyword s) n (keyword j)))

(defn add-person9 [v]
  (if (= 2 (count v))
    (let [[a b] v]
      (vector (keyword a) b))
    (let [[s n j] v]
      (vector (keyword s) n (keyword j)))))

(add-person9 v1)
(add-person9 v)

(defn make-roster [v]
  (if (= 2 (count v))
    (let [[tn fy] v]
      (hash-map :team-name tn :first-year (read-string fy)))
    (let [[s n ge gr] v]
      (hash-map (keyword s)
                (hash-map :name n
                          :gift-history (vector (hash-map
                                                  :givee (keyword ge)
                                                  :giver (keyword gr))))))))

((hash-map (keyword "TroBro") (vector 1 2)) :TroBro)
((hash-map (keyword "TroBro")
           (hash-map :name "Troy Brouwer"
                     :gift-history (vector (hash-map
                                             :givee (keyword "DavBol")
                                             :giver (keyword "JoeQue"))))) :TroBro)

(hash-map :gift-history (vector (hash-map :givee (keyword "DavBol") :giver (keyword "JoeQue"))))

(def best-shot (hash-map (keyword "TroBro")
                         (hash-map :name "Troy Brouwer"
                                   :gift-history (vector (hash-map
                                                           :givee (keyword "DavBol")
                                                           :giver (keyword "JoeQue"))))))

(get-in best-shot
        [:TroBro :name])
(get-in best-shot
        [:TroBro :gift-history 0 :givee])
(get-in best-shot
        [:TroBro :gift-history 0 :giver])

(make-roster (first parsed))
(make-roster (second parsed))
(make-roster (last parsed))
