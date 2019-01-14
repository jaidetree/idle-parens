(ns eccentric-j.site.assortment
  (:require [hiccup.page :refer [html5]]
            [eccentric-j.site.base :as base]))

(defn render [{global-meta :meta posts :entries entry :entry}]
  (base/render
   :title (:site-title global-meta)
   :subtitle (:keyword entry)
   :content [[:h1 (str "Page" (:page entry))]
             [:ul.items.columns.small-12
              (for [post posts]
                [:li (:title post)])]]))
