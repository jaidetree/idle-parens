(ns idle-parens.blog.paginate
  (:require [hiccup.page :refer [html5]]
            [idle-parens.base :as base]
            [idle-parens.blog.post :refer [render-entry]]))

(defn render [{global-meta :meta posts :entries entry :entry :as data}]
  (base/render
   :title (:site-title global-meta)
   :subtitle (format "idle-parens.blog/page-%s" (:page entry))
   :current :archive
   :content [:div.blog
             [:ul.posts
              (for [post posts]
                  [:li.posts__item
                   (render-entry (assoc data :entry post) :description)])]
             (when (:next-page entry)
               (:next-page entry))]))
