(ns clojure-redpoint.hat)

(def pucks-givee (atom []))
(def pucks-giver (atom []))
(def discards-givee (atom []))
(def discards-giver (atom []))

(defn make-hats [r]
  (reset! pucks-givee (into [] (keys (deref r))))
  (reset! pucks-giver (into [] (keys (deref r))))
  (reset! discards-givee [])
  (reset! discards-giver []))

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

;; (make-hat (atom {:RinSta {:name "Ringo Starr", :gift-history [{:giver :GeoHar, :givee :JohLen}]}, :JohLen {:name "John Lennon", :gift-history [{:giver :RinSta, :givee :PauMcc}]}, :GeoHar {:name "George Harrison", :gift-history [{:giver :PauMcc, :givee :RinSta}]}, :PauMcc {:name "Paul McCartney", :gift-history [{:giver :JohLen, :givee :GeoHar}]}}))
