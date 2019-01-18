(ns idle-parens.post
  (:require [idle-parens.base :as base]
            [idle-parens.formats :refer [format-date format-time]]))

(defn render-entry
  [{global-meta :meta posts :entries post :entry} content]
  (let [full? (= content :content)]
    [:article.post
     [:header.post__header
      [:a.post__permalink {:href (:permalink post)}
       [:i.fa.fa-link]]
      [:h1.post__title.borders
       [:a.post__link {:href (:permalink post)}
         (:title post)]]
      [:div.post__attrs
       [:time.post__meta.post__date-time
        {:datetime (:date-published post)}
        [:span.post__date.icon.fa-calendar-day
         (format-date (:date-published post))]
        [:span.post__time.icon.fa-clock
         (format-time (:date-published post))]]
       [:span.post__meta.post__author.icon.fa-user
        [:a {:href (:author-url post) :target "_blank"}
         (:author post)]]]
      (when full?
        [:ul.post__meta.post__tags.d-inline-block
         (for [tag (:tags post)]
           [:li.post__tag.d-inline-block.icon.fa-hashtag
            [:a {:href (str "/blog/tags/" tag ".html")} tag]])])]
     (when content
       [:section.post__content
         {:class (if full? "post__full" "post__summary")}
         (content post)])
     [:footer.post__footer]]))

(defn render [{global-meta :meta posts :entries post :entry :as data}]
  (base/render
   :title (:site-title global-meta)
   :subtitle "idle-parens/blog"
   :content (render-entry data :content)))
