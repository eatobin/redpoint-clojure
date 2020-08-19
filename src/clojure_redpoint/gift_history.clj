(ns clojure-redpoint.gift-history
  (:require [clojure.spec.alpha :as s]
            [orchestra.spec.test :as ostest]))

(s/def :unq/gift-history (s/coll-of :unq/gift-pair :kind vector?))



(ostest/instrument)
