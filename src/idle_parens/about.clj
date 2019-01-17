(ns idle-parens.about
  (:require [hiccup.page :refer [html5]]
            [idle-parens.base :as base]))

(defn render [{global-meta :meta posts :entries}]
  (base/render
   :current :about
   :title (:site-title global-meta)
   :subtitle "^meta/about"
   :content [:p "This is a demonstration of a static page, for content that won't change"]))
