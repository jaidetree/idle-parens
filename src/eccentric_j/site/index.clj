(ns eccentric-j.site.index
  (:require [eccentric-j.site.base :as base]))

(defn render [{global-meta :meta posts :entries post :entry}]
  (base/render
   :title (:site-title global-meta)
   :current :blog
   :content [:div.blog
             [:h1.page__title "Blog"]
             [:ul.items.columns.small-12
              (for [post posts]
                [:li
                 [:a {:href (:permalink post)}(:title post)]])]]))
