(ns clojure-redpoint.hats)

(def givee-hat (atom []))
(def giver-hat (atom []))
(def discards (atom []))

(defn make-hats [r]
  (reset! givee-hat (into [] (keys (deref r))))
  (reset! giver-hat (into [] (keys (deref r))))
  (reset! discards []))

(defn draw-puck-givee []
  (when (not= 0 (count (deref givee-hat)))
    (rand-nth (deref givee-hat))))

(defn draw-puck-giver []
  (when (not= 0 (count (deref giver-hat)))
    (rand-nth (deref giver-hat))))

(defn remove-puck-givee [p]
  (when (some #{p} (deref givee-hat))
    (reset! givee-hat (into [] (remove #{p} (deref givee-hat))))))

(defn remove-puck-giver [p]
  (when (some #{p} (deref giver-hat))
    (reset! giver-hat (into [] (remove #{p} (deref giver-hat))))))

(defn discard-puck [p]
  (when (remove-puck-givee p)
    (swap! discards conj p)))

(defn return-discards []
  (when (> (count (deref discards)) 0)
    (do
      (swap! givee-hat into (deref discards))
      (reset! discards []))))
