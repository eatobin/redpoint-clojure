(ns clojure-redpoint.domain
  (:require [clojure.spec.alpha :as s]
            [clojure.string :as str]))



(s/def ::hat (s/coll-of keyword? :kind vector?))
