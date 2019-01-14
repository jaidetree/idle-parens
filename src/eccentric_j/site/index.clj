(ns eccentric-j.site.index
  (:require [eccentric-j.site.base :as base]))

(defn render [{global-meta :meta posts :entries}]
  (base/render
   :title (:site-total global-meta)
   :content [:ul.items.columns.small-12
             (for [post posts]
               [:li
                [:a {:href (:permalink post)}(:title post)]])]))
