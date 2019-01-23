(ns app.util)

(defn log
  [& args]
  (apply println args))


(defn query-all
  [selector]
  (->> selector
       (.querySelectorAll js/document)
       (.from js/Array)
       (js->clj)))
