(ns idle-parens.blog.paginate
  (:require [hiccup.page :refer [html5]]
            [idle-parens.base :as base]
            [idle-parens.blog.post :refer [render-entry]]))

(defn render [{global-meta :meta posts :entries entry :entry :as data}]
  (clojure.pprint/pprint entry)
  (base/render
   :title (:site-title global-meta)
   :subtitle (format "idle-parens.blog/page-%s" (:page entry))
   :current :archive
   :content [:div.blog
             [:ul.posts
              (for [post posts]
                  [:li.posts__item
                   (render-entry (assoc data :entry post) :description)])]
             [:div.pagination
              (when (= (:page entry) 1)
                 [:a.pagination__link
                  {:href (str "/")}
                  [:i.fa.fa-long-arrow-alt-left.mr-1]
                  "View newest posts"])
              (when (:prev-page entry)
                 [:a.pagination__link
                  {:href (str "/blog/" (:prev-page entry))}
                  [:i.fa.fa-long-arrow-alt-left.mr-1]
                  "View newer posts"])
              (when (:next-page entry)
                 [:a.pagination__link
                  {:href (str "/blog/" (:next-page entry))}
                  "View older posts"
                  [:i.fa.fa-long-arrow-alt-right.ml-1]])]]))
