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

(defn year-runner []
  ;TODO
  )

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

(defn print-and-ask [year]
  (print-string-giving-roster (deref year))
  (lprint "\"Continue? ('q' to quit): \")))


;class Redpoint
;attr_reader(:year, :roster, :giver_hat, :givee_hat, :giver, :givee)
;
;def initialize
;@year = 0
;@roster = Roster.new('../../blackhawks2010.txt')
;end
;
;def runner
;until print_and_ask(@year).downcase.eql?('q')
;self.start_new_year
;until @giver.nil?
;until @givee.nil?
;if Rules.givee_not_self(@giver, @givee) &&
;Rules.givee_not_recip(@giver, @givee, @roster, @year) &&
;Rules.givee_not_repeat(@giver, @givee, @roster, @year)
;@givee = self.givee_is_success
;else
;@givee = self.givee_is_failure
;end
;end
;self.select_new_giver
;end
;puts
;end
;
;puts
;puts 'This was fun!'
;puts 'Talk about a position with Redpoint?'
;puts 'Please call: Eric Tobin 773-325-1516'
;puts 'Thanks! Bye...'
;end
;
;def start_new_year
;@year = @year + 1
;@roster.add_new_year
;@givee_hat = Hat.new(@roster)
;@giver_hat = Hat.new(@roster)
;@giver = @giver_hat.draw_puck
;@givee = @givee_hat.draw_puck
;end
;
;def select_new_giver
;@giver_hat.remove_puck(@giver)
;@givee_hat.return_discards
;@giver = giver_hat.draw_puck
;@givee = @givee_hat.draw_puck
;end
;
;def givee_is_success
;@roster.set_givee_code(@giver, @givee, @year)
;@roster.set_giver_code(@givee, @giver, @year)
;@givee_hat.remove_puck(@givee)
;nil
;end
;
;def givee_is_failure
;@givee_hat.discard_puck(@givee)
;@givee_hat.draw_puck
;end
;
;def print_and_ask(year)
;@roster.print_giving_roster(year)
;puts
;print("Continue? ('q' to quit): ")
;gets.chomp
;end
;end
