(ns idle-parens.tasks.clean
  (:require [boot.core :refer [deftask] :as boot]
            [clojure.java.io :as io]
            [io.perun.core :refer [report-info]]))

(defn match-any
  [patterns s]
  (some #(re-find % s) patterns))

(defn matcher
  [regexps default]
  (if (some? regexps)
    #(match-any regexps (str %))
    (constantly default)))

(defn delete-files!
  "Recursively deletes all files in a folder
  Takes a included predicate, an excluded predicate, and an io/file dir.
  Returns nil."
  [included excluded dir]
  (let [files (->> dir
                   (file-seq)
                   (drop 1)
                   (remove excluded)
                   (filter included))]
    (doseq [file files]
      (when (.isDirectory file)
        (delete-files! included excluded file))
      (.delete file))))

(deftask clean
  [d dir     DIR      str         "Directory to clean defaults to target/public"
   e exclude PATTERNS #{regex}    "Regexps to exclude from cleaning"
   i include PATTERNS #{regex}    "Regexps to include for cleaning"]
  (let [dir (or dir "target/public")]
    (boot/with-pass-thru _
      (delete-files! (matcher include true)
                     (matcher exclude false)
                     (io/file dir))
      (report-info "clean!" "Cleaned %s" dir))))
