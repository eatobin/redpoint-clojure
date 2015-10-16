(ns clojure-redpoint.redpoint
  (:require [clojure-redpoint.roster :refer :all]
            [clojure-redpoint.hats :refer :all]
            [clojure-redpoint.rules :refer :all])
  (:gen-class))

(def year (atom 0))
(def giver (atom :none))
(def givee (atom :none))

(defn initialize-state []
  (reset! year 0)
  (reset! giver :none)
  (reset! givee :none)
  (make-roster "blackhawks2010.txt"))

(defn start-new-year []
  (swap! year inc)
  (add-new-year)
  (make-hats roster)
  (reset! giver (draw-puck-giver))
  (reset! givee (draw-puck-givee)))

(defn select-new-giver []
  (remove-puck-giver (deref giver))
  (return-discards)
  (reset! giver (draw-puck-giver))
  (reset! givee (draw-puck-givee)))

(defn givee-is-success []
  (set-givee-code (deref giver)
                  (deref year) (deref givee))
  (set-giver-code (deref givee)
                  (deref year) (deref giver))
  (remove-puck-givee (deref givee)))

(defn givee-is-failure []
  (discard-puck (deref givee))
  (draw-puck-givee))

;(defn ask []
;  (if (#(not= % "q") (clojure.string/lower-case (print-and-ask)))
;    true
;    (do
;      (println "That is not a valid response.\nPlease re-enter.")
;      (println)
;      (recur))))

(defn print-and-ask []
  (println (print-string-giving-roster (deref year)))
  (println "Continue? ('q' to quit): ")
  (read-line))

(defn runner []
  (initialize-state)
  (while (not= (clojure.string/lower-case (print-and-ask)) "q"))
    (do (start-new-year)
        (while (some? (deref givee)))
        (while (some? (deref giver)))))

;(some? :foo) => true
;(some? nil) => false


;def runner
;  until print_and_ask(@year).downcase.eql?('q')
;    self.start_new_year
;    until @giver.nil?
;      until @givee.nil?
;        if Rules.givee_not_self(@giver, @givee) &&
;            Rules.givee_not_recip(@giver, @givee, @roster, @year) &&
;            Rules.givee_not_repeat(@giver, @givee, @roster, @year)
;          @givee = self.givee_is_success
;        else
;          @givee = self.givee_is_failure
;        end
;      end
;      self.select_new_giver
;    end
;    puts
;  end
;
;  puts
;  puts 'This was fun!'
;  puts 'Talk about a position with Redpoint?'
;  puts 'Please call: Eric Tobin 773-325-1516'
;  puts 'Thanks! Bye...'
;end
