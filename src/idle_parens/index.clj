(ns idle-parens.index
  (:require [idle-parens.base :as base]))

(defn render [{global-meta :meta posts :entries post :entry}]
  (base/render
   :title (:site-title global-meta)
   :subtitle "idle-parens/blog"
   :current :blog
   :content [:div.blog
             [:ul.items.columns.small-12
              (for [post posts]
                [:li
                 [:a {:href (:permalink post)}(:title post)]])]]))
