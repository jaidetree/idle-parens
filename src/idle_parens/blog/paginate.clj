(ns idle-parens.blog.paginate
  (:require [hiccup.page :refer [html5]]
            [idle-parens.base :as base]))

(defn render [{global-meta :meta posts :entries entry :entry}]
  (base/render
   :title (:site-title global-meta)
   :subtitle (format "idle-parens/blog/page-%s" (:page entry))
   :current :archive
   :content [:ul.items.columns.small-12
             (for [post posts]
               [:li (:title post)])]))
