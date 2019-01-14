(ns eccentric-j.site.about
  (:require [hiccup.page :refer [html5]]
            [eccentric-j.site.base :as base]))

(defn render [{global-meta :meta posts :entries}]
  (base/render
   :title (:site-title global-meta)
   :subtitle "About"
   :content [:p "This is a demonstration of a static page, for content that won't change"]))
