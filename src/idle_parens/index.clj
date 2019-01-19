(ns idle-parens.index
  (:require [idle-parens.base :as base]
            [idle-parens.post :refer [render-entry]]))

(defn render [{global-meta :meta posts :entries post :entry :as data}]
  (let [[first-post & posts] posts]
    (base/render
     :title (:site-title global-meta)
     :subtitle "idle-parens/blog"
     :current :blog
     :content [:div.blog
               [:ul.posts
                [:li.posts__item
                 (render-entry (assoc data :entry first-post) :content)]
                (for [post posts]
                  [:li.posts__item
                   (render-entry (assoc data :entry post) :description)])]])))
