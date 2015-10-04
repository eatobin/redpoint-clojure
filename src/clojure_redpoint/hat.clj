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
  (if (some #{p} (deref pucks))
    (reset! pucks (into [] (remove #{p} (deref pucks))))))

(defn discard-puck [p]
  (if (remove-puck p)
    (swap! discards conj p)))

(defn return-discards []
  (if (> (count (deref discards)) 0)
    (do
      (swap! pucks into (deref discards))
      (reset! discards []))))
