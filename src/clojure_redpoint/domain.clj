(ns clojure-redpoint.domain
  (:require [clojure.spec.alpha :as s]))

(s/def ::givee keyword?)
(s/def ::giver keyword?)
