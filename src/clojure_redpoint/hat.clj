(ns clojure-redpoint.hat
  (:require [clojure-redpoint.roster :refer :all]))

(def pucks (atom []))

(def discards (atom []))

(defn make-hat [r]
  (reset! pucks (into [] (keys (deref r))))
  (reset! discards []))

(defn draw-puck []
  (rand-nth (deref pucks)))

(defn remove-puck [p]
  (reset! pucks (into [] (remove #{p} (deref pucks)))))

;(defn discard-puck [p]
;  (if (dissoc (deref pucks) p)
;         )

;def discard_puck(player_code)
;  if @pucks.delete(player_code)
;    @discards.push(player_code)
;  end
