(ns redpoint.tester
  (:require [clojure.data.json :as json]))

(def bad-json "[ \"test\" :: 123 ]")
(def json-string-borrowers "[{\"max-books\":2, \"name\":\"Borrower2\"},{\"name\":\"Borrower1\",\"max-books\":1}]")
(def json-string-books "[{\"title\":\"Title1\",\"author\":\"Author1\",\"maybe-borrower\":{\"name\":\"Borrower1\",\"max-books\":1}},{\"title\":\"Title2\",\"author\":\"Author2\",\"maybe-borrower\":null}]")
(def file-path "resources-test/beatles.json")
(def bad-file-path "nope.json")

(try
  (slurp file-path)
  (catch Exception e
    (str (.getMessage e))))

(try
  (slurp bad-file-path)
  (catch Exception e
    (str (.getMessage e))))

(try
  (json/read-str bad-json
                 :key-fn keyword)
  (catch Exception e
    (str (.getMessage e))))

(try
  (json/read-str json-string-borrowers
                 :key-fn keyword)
  (catch Exception e
    (str (.getMessage e))))

(try
  (json/read-str json-string-books
                 :key-fn keyword)
  (catch Exception e
    (str (.getMessage e))))
