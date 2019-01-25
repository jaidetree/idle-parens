(ns idle-parens.projects.project
  (:require [idle-parens.base :as base]
            [idle-parens.formats :refer [format-date format-time]]))

(defn render-entry
  [{global-meta :meta projects :entries project :entry} & {:keys [context]}]
  (:title project))

(defn render [{global-meta :meta posts :entries post :entry :as data}]
  (base/render
   :title (:site-title global-meta)
   :subtitle (:title post)
   :page-title "active-parens/projects"
   :current :projects
   :content (render-entry data
                          :context :project)))
