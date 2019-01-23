(ns idle-parens.tasks.emoji
  {:boot/export-tasks true}
  (:require [boot.core :as boot :refer [deftask]]
            [boot.pod :as pod]
            [clojure.java.io :as io]
            [clojure.string :as s]
            [io.perun :refer [content-task
                              content-paths
                              content-passthru]]
            [io.perun.core :refer [report-debug]]))

(defn format-hex
  [hex-str]
  (->> (s/split hex-str #"-")
       (map #(str "&#x" % ";"))
       (s/join "")))

(def emojis (->> "resources/emoji.edn"
                 (slurp)
                 (read-string)))

(defn str->emoji
  [shortname]
  (let [emoji (get emojis shortname shortname)]
    emoji))

(defn replace-emojis
  [line]
  (s/replace line #":[_a-z0-9]+:" str->emoji))

(defn emoji-html
  [{:keys [full-path]}]
  (with-open [rdr (io/reader full-path)]
    (->> rdr
         (line-seq)
         (map replace-emojis)
         (s/join "\n"))))

(defn process-emoji
  [{:keys [entry]}]
  (report-debug "emoji" "processing emojis" (:filename entry))
  (let [html (emoji-html entry)]
    (assoc entry :rendered html)))

(def +emoji-defaults+ {:out-dir "public"
                       :out-ext ".html"
                       :filterer identity
                       :meta {}})

(deftask emoji
  ""
  []
  (let [options (merge +emoji-defaults+ *opts*)]
    (content-task
     {:render-form-fn (fn [data] `(process-emoji ~data))
      :paths-fn #(content-paths % (assoc options :extensions [".html"]))
      :passthru-fn content-passthru
      :task-name "emoji"
      :tracer :idle-parens.tasks/emoji
      :rm-originals true})))

(comment
  (defn format-emoji-shortcodes
    [[hex {:keys [shortname]}]]
    (let [hex (format-hex (name hex))]
      [shortname hex]))

  (defn pprint-str
    [emojis]
    (with-out-str
     (clojure.pprint/pprint emojis)))

  (defn format-emoji-file
    [filename]
    (->> filename
         (slurp)
         (read-string)
         (map format-emoji-shortcodes)
         (into {})
         (pprint-str)
         (spit "emoji.edn"))))
