(ns idle-parens.post
  (:require [idle-parens.base :as base]
            [idle-parens.formats :refer [format-date]]))

(defn render-entry
  [{global-meta :meta posts :entries post :entry}]
  [:article.post
   [:header.post__header
    [:h1.post__title.borders
     [:a.post__link {:href (:permalink post)}
       (:title post)]]
    [:div.post__fields
     [:span.post__date
      [:i.icon.fa.fa-calendar-day]
      (format-date (:date-published post))]]]
   [:section.post__content
     (:content post)]])

(defn render [{global-meta :meta posts :entries post :entry :as data}]
  (base/render
   :title (:site-title global-meta)
   :subtitle (:title post)
   :content (render-entry data)))
