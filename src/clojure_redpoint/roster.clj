(ns clojure-redpoint.roster
  (:require [clojure.java.io :as io])
  (:require [clojure-csv.core :as csv]))

(def team-name "placeholder")
(def first-year 2014)

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

(def parsed (csv/parse-csv (clojure.string/replace (slurp "blackhawks2010.txt") #", " ",")))
(second parsed)
;;=> ["TroBro" "Troy Brouwer" "DavBol" "JoeQue"]
(keyword "TroBro")
;;=> :TroBro

(let [[s n j] ["EriTob" "Eric Tobin" "junk"]]
(vector (keyword s) n (keyword j)))

(def v ["EriTob" "Eric Tobin" "junk"])

(let [[s n j] v]
(vector (keyword s) n (keyword j)))
