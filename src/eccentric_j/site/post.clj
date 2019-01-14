(ns eccentric-j.site.post
  (:require [eccentric-j.site.base :as base]))

(defn render [{global-meta :meta posts :entries post :entry}]
  (base/render
   :title (:site-title global-meta)
   :subtitle (:title post)
   :content [:div [:h1 (:title post)]
                  [:div (:content post)]]))
