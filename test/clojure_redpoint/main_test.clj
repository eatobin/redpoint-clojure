(ns clojure-redpoint.main-test
  (:require [clojure.test :refer :all]
            [clojure-redpoint.main :refer :all]
            [clojure-redpoint.roster :refer :all]
            [clojure-redpoint.roster-utility :refer :all]
            [clojure-redpoint.hats :refer :all]
            [clojure-redpoint.rules :refer :all]))


(with-redefs [exit-now! (constantly "we exit here")]
  (is (= "we exit here" (code that calls exit))))




(deftest start-new-year-test
  (reset! a-g-year 0)
  (reset! a-giver :none)
  (reset! a-givee :none)
  (let [roster-list (make-roster-seq
                      (read-file-into-string "blackhawks2010.txt"))]
    (reset! a-plrs-map (make-players-map roster-list))
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
           (first (deref a-plrs-map))))))

(deftest select-new-giver-test
  (reset! a-g-year 0)
  (reset! a-giver :none)
  (reset! a-givee :none)
  (let [roster-list (make-roster-seq
                      (read-file-into-string "blackhawks2010.txt"))]
    (reset! a-plrs-map (make-players-map roster-list))
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
  (let [roster-list (make-roster-seq
                      (read-file-into-string "blackhawks2010.txt"))]
    (reset! a-plrs-map (make-players-map roster-list))
    (start-new-year)
    (let [temp-ge (deref a-givee)]
      (givee-is-success)
      (is (= temp-ge
             (get-givee-in-roster (deref a-plrs-map) (deref a-giver) (deref a-g-year))))
      (is (= (deref a-giver)
             (get-giver-in-roster (deref a-plrs-map) temp-ge (deref a-g-year))))
      (is (= nil
             (some #{temp-ge} (deref a-ge-hat)))))))

(deftest givee-is-failure-test
  (reset! a-g-year 0)
  (reset! a-giver :none)
  (reset! a-givee :none)
  (let [roster-list (make-roster-seq
                      (read-file-into-string "blackhawks2010.txt"))]
    (reset! a-plrs-map (make-players-map roster-list))
    (start-new-year)
    (let [temp-ge (deref a-givee)]
      (givee-is-failure)
      (is (= temp-ge
             (some #{temp-ge} (deref a-discards))))
      (is (= nil
             (some #{temp-ge} (deref a-ge-hat)))))))
