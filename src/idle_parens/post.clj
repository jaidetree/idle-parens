(ns idle-parens.post
  (:require [idle-parens.base :as base]))

(defn render-entry
  [{global-meta :meta posts :entries post :entry}]
  [:article.post
   [:header.post__header.bordered
    [:h1.post__title [:a.post__link {:href (:permalink post)}
                                    (:title post)]]]
   [:section.post__content
     (:content post)]])

(defn render [{global-meta :meta posts :entries post :entry :as data}]
  (base/render
   :title (:site-title global-meta)
   :subtitle (:title post)
   :content (render-entry data)))
