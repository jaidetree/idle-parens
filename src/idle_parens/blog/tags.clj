(ns idle-parens.blog.tags
  (:require [idle-parens.base :as base]
            [idle-parens.blog.post :refer [render-entry]]))


(defn render [{global-meta :meta posts :entries entry :entry :as data}]
  (base/render
   :title (:site-title global-meta)
   :subtitle "idle-parens.blog/tags"
   :current :blog
   :content [:div.blog
             [:h2.mb-5 (str "Tag: " (:tag entry))]
             [:ul.posts
              (for [post posts]
                [:li.posts__item
                 (render-entry (assoc data :entry post) :description)])]]))
