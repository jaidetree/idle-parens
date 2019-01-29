(ns idle-parens.not-found
  (:require [idle-parens.base :as base]))

(defn render
  [{global-meta :meta posts :entries}]
  (base/render
   :current :about
   :title "404 Not Found"
   :subtitle "^error/404-not-found"
   :content [:div.error-404
             [:div.cat-track
              [:i.fa.fa-cat.cat
               {:style "color: #ff00ed;font-size: 4rem;display:inline-block;"}]]
             [:h1.borders.padding.error-404__title "What did you just do?! :scream:"]
             [:p "Just kidding. :smiling_imp: This is probably my fault to be honest. If you&rsquo;re seeing this page often please reach out so I can take a look."]]))
