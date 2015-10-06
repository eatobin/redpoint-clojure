(ns clojure-redpoint.rules
  (:require [clojure-redpoint.roster :refer :all]
            [clojure-redpoint.hats :refer :all]))

(defn givee-not-self [gr ge]
  (not= gr ge))

(defn givee-not-recip [gr ge y]
  (let [ge-giving-to (get-givee-code ge y)]
    (not= gr ge-giving-to)))

(defn givee-not-repeat [gr ge y]
  (let [past4 (range (- y 1) (- y 4) -1)
        past (filter #(>= % 0) past4)
        ge-in-yr (get-givee-code gr y)]
    ge-in-yr))

;def self.givee_not_repeat(giver_code, givee_code, roster, this_year)
;result = true
;counter = this_year - 1
;while (counter >= 0) && (counter >= (this_year - 4))
;givee_in_year = roster.get_givee_code(giver_code, counter)
;if givee_code == givee_in_year
;result = false
;end
;counter = counter - 1
;end
;result
;end
