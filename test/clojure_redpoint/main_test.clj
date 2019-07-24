(ns clojure-redpoint.main-test
  (:require [clojure.test :refer :all]
            [clojure-redpoint.main :as main]
            [clojure-redpoint.roster :as ros]
            [clojure-redpoint.hats :as hat]))

(def scrubbed "The Beatles,2014\nRinSta,Ringo Starr,JohLen,GeoHar\nJohLen,John Lennon,PauMcc,RinSta\nGeoHar,George Harrison,RinSta,PauMcc\nPauMcc,Paul McCartney,GeoHar,JohLen")

(deftest scrubbed-or-quit-test
  (is (= scrubbed
         (main/scrubbed-or-quit "beatles2014.txt")))
  (with-redefs [main/exit-now! (constantly "we exit here")]
    (is (= "we exit here" (main/scrubbed-or-quit "no-file.txt"))))
  (with-redefs [main/exit-now! (constantly "we exit here")]
    (is (= "we exit here" (main/scrubbed-or-quit "bad-length.txt"))))
  (with-redefs [main/exit-now! (constantly "we exit here")]
    (is (= "we exit here" (main/scrubbed-or-quit "bad-symbol.txt"))))
  (with-redefs [main/exit-now! (constantly "we exit here")]
    (is (= "we exit here" (main/scrubbed-or-quit "empty-first-line.txt"))))
  (with-redefs [main/exit-now! (constantly "we exit here")]
    (is (= "we exit here" (main/scrubbed-or-quit "missing-symbol.txt"))))
  (with-redefs [main/exit-now! (constantly "we exit here")]
    (is (= "we exit here" (main/scrubbed-or-quit "no-name.txt"))))
  (with-redefs [main/exit-now! (constantly "we exit here")]
    (is (= "we exit here" (main/scrubbed-or-quit "no-year.txt"))))
  (with-redefs [main/exit-now! (constantly "we exit here")]
    (is (= "we exit here" (main/scrubbed-or-quit "year-has-letter.txt"))))
  (with-redefs [main/exit-now! (constantly "we exit here")]
    (is (= "we exit here" (main/scrubbed-or-quit "year-too-big.txt"))))
  (with-redefs [main/exit-now! (constantly "we exit here")]
    (is (= "we exit here" (main/scrubbed-or-quit "year-too-small.txt")))))

(deftest start-new-year-test
  (reset! main/a-g-year 0)
  (reset! main/a-giver nil)
  (reset! main/a-givee nil)
  (let [players-vector (ros/make-players-vector
                        (main/scrubbed-or-quit "blackhawks2010.txt"))]
    (reset! main/a-plrs-map (ros/make-players-map players-vector))
    (main/start-new-year)
    (is (= 1
           (deref main/a-g-year)))
    (is (not= nil
              (deref main/a-giver)))
    (is (not= nil
              (deref main/a-givee)))
    (is (= [:AndLad {:name         "Andrew Ladd",
                     :gift-history [{:giver :KriVer, :givee :JoeQue}
                                    {:giver :AndLad, :givee :AndLad}]}]
           (first (deref main/a-plrs-map))))
    (is (empty? (deref main/a-discards)))))

(deftest select-new-giver-test
  (reset! main/a-g-year 0)
  (reset! main/a-giver nil)
  (reset! main/a-givee nil)
  (let [players-vector (ros/make-players-vector
                        (main/scrubbed-or-quit "blackhawks2010.txt"))]
    (reset! main/a-plrs-map (ros/make-players-map players-vector))
    (main/start-new-year)
    (swap! main/a-discards hat/discard-puck-givee :AdaBur)
    (is (= 1
           (count (deref main/a-discards))))
    (main/select-new-giver)
    (is (= 17
           (count (deref main/a-gr-hat))))
    (is (= 0
           (count (deref main/a-discards))))))

(deftest givee-is-success-test
  (reset! main/a-g-year 0)
  (reset! main/a-giver nil)
  (reset! main/a-givee nil)
  (let [players-vector (ros/make-players-vector
                        (main/scrubbed-or-quit "blackhawks2010.txt"))]
    (reset! main/a-plrs-map (ros/make-players-map players-vector))
    (main/start-new-year)
    (let [temp-ge (deref main/a-givee)]
      (main/givee-is-success)
      (is (= temp-ge
             (ros/get-givee-in-roster (deref main/a-plrs-map) (deref main/a-giver) (deref main/a-g-year))))
      (is (= (deref main/a-giver)
             (ros/get-giver-in-roster (deref main/a-plrs-map) temp-ge (deref main/a-g-year))))
      (is (= nil
             (some #{temp-ge} (deref main/a-ge-hat)))))))

(deftest givee-is-failure-test
  (reset! main/a-g-year 0)
  (reset! main/a-giver nil)
  (reset! main/a-givee nil)
  (let [players-vector (ros/make-players-vector
                        (main/scrubbed-or-quit "blackhawks2010.txt"))]
    (reset! main/a-plrs-map (ros/make-players-map players-vector))
    (main/start-new-year)
    (let [temp-ge (deref main/a-givee)]
      (main/givee-is-failure)
      (is (= temp-ge
             (some #{temp-ge} (deref main/a-discards))))
      (is (= nil
             (some #{temp-ge} (deref main/a-ge-hat)))))))
