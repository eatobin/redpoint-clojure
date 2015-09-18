(ns clojure-redpoint.roster-test
  (:require [clojure.test :refer :all]
            [clojure-redpoint.roster :refer :all]
            [clojure-csv.core :as csv]))

(defn setup []
  (spit "roster-test.txt" "Blackhawks, 2010
TroBro, Troy Brouwer, DavBol, JoeQue
JoeQue, Joel Quenneville, TroBro, AndLad
AdaBur, Adam Burish, DunKei, JonToe\n"))

(make-roster "roster-test.txt")

(defn teardown [])

(defn each-fixture [f]
  (setup)
  (f)
  (teardown))

(use-fixtures :each each-fixture)

(deftest make-roster-test
  (is (= 5
         (count (deref roster))))
  (is (= [:team-name "Blackhawks"]
         (first (deref roster)))))

(deftest get-player-name-test
  (is (= "Adam Burish"
         (get-player-name :AdaBur))))

;(deftest add-person-2-test
;  (is (= [{:name "Person One", :max-books 2}
;          {:name "Person Two", :max-books 6}
;          {:name "Wilma" :max-books 5}]
;         (add-person "Wilma" 5))))
;
;(deftest get-person-test
;  (add-person "Fred")
;  (is (= {:max-books 3 :name "Fred"}
;         (get-person "Fred"))))
;
;(deftest get-person-2-test
;  (is (= nil
;         (get-person "Fred"))))
;
;(deftest person-to-string-test
;  (add-person "Fred Flintstone" 7)
;  (is (= "Fred Flintstone (7 books)\n"
;         (person-to-string "Fred Flintstone"))))
;
;(deftest write-people-test
;  (reset! people [{:name "Fred" :max-books 3}])
;  (write-people)
;  (is (= [{:name "Fred" :max-books 3}]
;         (into [] (yaml/parse-string
;                    (slurp "people.yaml"))))))
