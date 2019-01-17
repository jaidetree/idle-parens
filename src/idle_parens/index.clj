(ns idle-parens.index
  (:require [idle-parens.base :as base]
            [idle-parens.post :refer [render-entry]]))

(defn render [{global-meta :meta posts :entries post :entry :as data}]
  (base/render
   :title (:site-title global-meta)
   :subtitle "idle-parens/blog"
   :current :blog
   :content [:div.blog
             [:ul.items.columns.small-12
              (for [post posts]
                [:li
                 (render-entry (assoc data :entry post))])]]))
