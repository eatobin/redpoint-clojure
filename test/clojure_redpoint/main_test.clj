(ns clojure-redpoint.main-test
  (:require [clojure.test :refer :all]
            [clojure-redpoint.main :refer :all]
            [clojure-redpoint.roster :refer :all]
            [clojure-redpoint.hats :refer :all]
            [clojure-redpoint.rules :refer :all]))

(def scrubbed "The Beatles,2014\nRinSta,Ringo Starr,JohLen,GeoHar\nJohLen,John Lennon,PauMcc,RinSta\nGeoHar,George Harrison,RinSta,PauMcc\nPauMcc,Paul McCartney,GeoHar,JohLen")

(deftest scrubbed-or-quit-test
  (is (= scrubbed
         (scrubbed-or-quit "beatles2014.txt")))
  (with-redefs [exit-now! (constantly "we exit here")]
    (is (= "we exit here" (scrubbed-or-quit "no-file.txt"))))
  (with-redefs [exit-now! (constantly "we exit here")]
    (is (= "we exit here" (scrubbed-or-quit "bad-length.txt"))))
  (with-redefs [exit-now! (constantly "we exit here")]
    (is (= "we exit here" (scrubbed-or-quit "bad-symbol.txt"))))
  (with-redefs [exit-now! (constantly "we exit here")]
    (is (= "we exit here" (scrubbed-or-quit "empty-first-line.txt"))))
  (with-redefs [exit-now! (constantly "we exit here")]
    (is (= "we exit here" (scrubbed-or-quit "missing-symbol.txt"))))
  (with-redefs [exit-now! (constantly "we exit here")]
    (is (= "we exit here" (scrubbed-or-quit "no-name.txt"))))
  (with-redefs [exit-now! (constantly "we exit here")]
    (is (= "we exit here" (scrubbed-or-quit "no-year.txt"))))
  (with-redefs [exit-now! (constantly "we exit here")]
    (is (= "we exit here" (scrubbed-or-quit "year-has-letter.txt"))))
  (with-redefs [exit-now! (constantly "we exit here")]
    (is (= "we exit here" (scrubbed-or-quit "year-too-big.txt"))))
  (with-redefs [exit-now! (constantly "we exit here")]
    (is (= "we exit here" (scrubbed-or-quit "year-too-small.txt")))))

(deftest start-new-year-test
  (reset! a-g-year 0)
  (reset! a-giver :none)
  (reset! a-givee :none)
  (let [players-vector (make-players-vector
                         (scrubbed-or-quit "blackhawks2010.txt"))]
    (reset! a-plrs-map (make-players-map players-vector))
    (start-new-year)
    (is (= 1
           (deref a-g-year)))
    (is (not= :none
              (deref a-giver)))
    (is (not= :none
              (deref a-givee)))
    (is (= [:AndLad {:name         "Andrew Ladd",
                     :gift-history [{:giver :KriVer, :givee :JoeQue}
                                    {:giver :none, :givee :none}]}]
           (first (deref a-plrs-map))))
    (is (empty? (deref a-discards)))))

(deftest select-new-giver-test
  (reset! a-g-year 0)
  (reset! a-giver :none)
  (reset! a-givee :none)
  (let [players-vector (make-players-vector
                         (scrubbed-or-quit "blackhawks2010.txt"))]
    (reset! a-plrs-map (make-players-map players-vector))
    (start-new-year)
    (swap! a-discards discard-puck-givee :AdaBur)
    (is (= 1
           (count (deref a-discards))))
    (select-new-giver)
    (is (= 17
           (count (deref a-gr-hat))))
    (is (= 0
           (count (deref a-discards))))))

(deftest givee-is-success-test
  (reset! a-g-year 0)
  (reset! a-giver :none)
  (reset! a-givee :none)
  (let [players-vector (make-players-vector
                         (scrubbed-or-quit "blackhawks2010.txt"))]
    (reset! a-plrs-map (make-players-map players-vector))
    (start-new-year)
    (let [temp-ge (deref a-givee)]
      (givee-is-success)
      (is (= temp-ge
             (get-givee-in-roster (deref a-plrs-map) (deref a-giver) (deref a-g-year))))
      (is (= (deref a-giver)
             (get-giver-in-roster (deref a-plrs-map) temp-ge (deref a-g-year))))
      (is (= nil
             (some #{temp-ge} (deref a-ge-hat)))))))

;(deftest givee-is-failure-test
;  (reset! a-g-year 0)
;  (reset! a-giver :none)
;  (reset! a-givee :none)
;  (let [roster-list (make-roster-seq
;                      (read-file-into-string "blackhawks2010.txt"))]
;    (reset! a-plrs-map (make-players-map roster-list))
;    (start-new-year)
;    (let [temp-ge (deref a-givee)]
;      (givee-is-failure)
;      (is (= temp-ge
;             (some #{temp-ge} (deref a-discards))))
;      (is (= nil
;             (some #{temp-ge} (deref a-ge-hat)))))))
