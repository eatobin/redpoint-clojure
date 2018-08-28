(ns clojure-redpoint.domain
  (:require [clojure.spec.alpha :as s]
            [clojure.test.check.generators :as gen]))

(s/def ::roster-string (s/with-gen string?
                                   #(gen/fmap (fn [[name year]]
                                                (str name ", " year))
                                              (gen/tuple gen/string-alphanumeric gen/nat))))
(s/def ::roster-seq (s/coll-of vector? :kind seq?))
(s/def ::roster-info-vector (s/coll-of string? :kind vector?))
