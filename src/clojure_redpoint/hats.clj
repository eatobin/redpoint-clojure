(ns clojure-redpoint.hats)

(def pucks-givee (atom []))
(def pucks-giver (atom []))
(def discards (atom []))

(defn make-hats [r]
  (reset! pucks-givee (into [] (keys (deref r))))
  (reset! pucks-giver (into [] (keys (deref r))))
  (reset! discards []))

(defn draw-puck-givee []
  (rand-nth (deref pucks-givee)))

(defn draw-puck-giver []
  (rand-nth (deref pucks-giver)))

(defn remove-puck-givee [p]
  (if (some #{p} (deref pucks-givee))
    (reset! pucks-givee (into [] (remove #{p} (deref pucks-givee))))))

(defn remove-puck-giver [p]
  (if (some #{p} (deref pucks-giver))
    (reset! pucks-giver (into [] (remove #{p} (deref pucks-giver))))))

(defn discard-puck [p]
  (if (remove-puck-givee p)
    (swap! discards conj p)))

(defn return-discards []
  (if (> (count (deref discards)) 0)
    (do
      (swap! pucks-givee into (deref discards))
      (reset! discards []))))

;; (make-hats (atom {:RinSta {:name "Ringo Starr", :gift-history [{:giver :GeoHar, :givee :JohLen}]}, :JohLen {:name "John Lennon", :gift-history [{:giver :RinSta, :givee :PauMcc}]}, :GeoHar {:name "George Harrison", :gift-history [{:giver :PauMcc, :givee :RinSta}]}, :PauMcc {:name "Paul McCartney", :gift-history [{:giver :JohLen, :givee :GeoHar}]}}))
