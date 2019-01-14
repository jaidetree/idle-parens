(ns eccentric-j.site.tags
  (:require [eccentric-j.site.base :as base]))


(defn render [{global-meta :meta posts :entries entry :entry}]
  (base/render
   :title (:site-title global-meta)
   :subtitle (:tag entry)
   :content [:div [:h1 (:title entry)]
                  [:ul.items.columns.small-12
                   (for [post posts]
                     [:li [:a {:href (:permalink post)}(:title post)]])]]))
