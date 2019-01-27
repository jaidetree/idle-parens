(ns idle-parens.blog.index
  (:require [idle-parens.base :as base]
            [idle-parens.blog.post :refer [render-entry]]))

(defn render [{global-meta :meta posts :entries post :entry :as data}]
  (let [[first-post & posts] posts]
    (base/render
     :title (:site-title global-meta)
     :subtitle "idle-parens/blog"
     :current :blog
     :content [:div.blog
               [:ul.posts
                (when first-post
                  [:li.posts__item
                   (render-entry (assoc data :entry first-post) :content
                                 :context :featured)])
                (for [post (take 9 posts)]
                  [:li.posts__item
                   (render-entry (assoc data :entry post) :description)])]
               (when (>= (count posts) 9)
                 [:div.pagination
                  [:a.pagination__link
                   {:href (str "/blog/page-1.html")}
                   "View Older Posts"
                   [:i.fa.fa-long-arrow-alt-right.ml-1]]])])))
