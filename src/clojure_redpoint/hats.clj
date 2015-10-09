(ns clojure-redpoint.hats)

(def givee-hat (atom []))
(def giver-hat (atom []))
(def discards (atom []))

(defn make-hats [r]
  (reset! givee-hat (into [] (keys (deref r))))
  (reset! giver-hat (into [] (keys (deref r))))
  (reset! discards []))

(defn draw-puck-givee []
  (rand-nth (deref givee-hat)))

(defn draw-puck-giver []
  (rand-nth (deref giver-hat)))

(defn remove-puck-givee [p]
  (if (some #{p} (deref givee-hat))
    (reset! givee-hat (into [] (remove #{p} (deref givee-hat))))))

(defn remove-puck-giver [p]
  (if (some #{p} (deref giver-hat))
    (reset! giver-hat (into [] (remove #{p} (deref giver-hat))))))

(defn discard-puck [p]
  (if (remove-puck-givee p)
    (swap! discards conj p)))

(defn return-discards []
  (if (> (count (deref discards)) 0)
    (do
      (swap! givee-hat into (deref discards))
      (reset! discards []))))

;; (make-hats (atom {:RinSta {:name "Ringo Starr", :gift-history [{:giver :GeoHar, :givee :JohLen}]}, :JohLen {:name "John Lennon", :gift-history [{:giver :RinSta, :givee :PauMcc}]}, :GeoHar {:name "George Harrison", :gift-history [{:giver :PauMcc, :givee :RinSta}]}, :PauMcc {:name "Paul McCartney", :gift-history [{:giver :JohLen, :givee :GeoHar}]}}))
