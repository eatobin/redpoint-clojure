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

(with-open [rdr (io/reader "blackhawks2010.txt")]
  (doseq [line (line-seq rdr)]
    (println line)))

(def test-data "2012-02-17,31.20,31.32,30.95,31.25,70036500,31.25")
(first (csv/parse-csv test-data))

(with-open [rdr (io/reader "blackhawks2010.txt")]
  (doseq [line (line-seq rdr)]
    (println (first (csv/parse-csv line)))))

(with-open [rdr (io/reader "blackhawks2010.txt")]
  (doseq [line (line-seq rdr)
          contents (csv/parse-csv line)]
    (if (= 2 (count contents)) (println "KKKK"))))
