(ns eccentric-j.site.paginate
  (:require [hiccup.page :refer [html5]]
            [eccentric-j.site.base :as base]))


(defn render [{global-meta :meta posts :entries entry :entry}]
  (base/render
   :title (:site-title global-meta)
   :subtitle (:tag entry)
   :content [:ul.items.columns.small-12
             (for [post posts]
               [:li (:title post)])]))
