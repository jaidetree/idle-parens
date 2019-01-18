(ns idle-parens.formats
  (:require [clojure.string :refer [join lower-case]])
  (:import [java.text SimpleDateFormat]))

(defn format-date
  [inst]
  (-> (SimpleDateFormat. "MMMM d, yyyy")
      (.format inst)))

(defn format-time
  [inst]
  (-> (SimpleDateFormat. "hh:mm a")
      (.format inst)))

(defn slugify
  [s]
  (->> s
       (lower-case)
       (re-seq #"[a-z0-9]+")
       (join "-")))

(comment
 (slugify "hello_my_dirty_123--file.txt.whatever"))
