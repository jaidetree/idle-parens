(ns idle-parens.projects.index
  (:require
   [idle-parens.base :as base]
   [idle-parens.projects.project :refer [render-entry]]
   [idle-parens.formats :refer [format-date format-time]]
   [clojure.java.io :as io]
   [clojure.string :refer [join trim]]))

(defn img-exists?
  [path]
  (-> (str "resources/public" path)
      (io/file)
      (.exists)))

(defn project-img
  [slug]
  (let [path (str "/img/projects/" slug ".png")]
    (when (img-exists? path)
      path)))


(defn render-blurb
  [project]
  (let [img (project-img (:slug project))]
    [:article.project.project--page_index
     {:class (trim
              (join " "
                    [(str "project--slug_" (:slug project))
                     (:class project "")]))}
     [:div.row.borders.padding
      [:div.project
       {:class (when img "col-5")}
       [:a.project__link
        {:href (:permalink project)}
        [:img.project__img
         {:src (str "/img/projects/" (:slug project) ".png")
          :alt (str "Teaser image of " (:title project))}]]]
      [:div.project
       {:class (if img "col-7" "col-12")}
       [:h1.project__title
        [:a.project__link
         {:href (:permalink project)}
         (:title project)]]
       [:div.project__meta
        [:time.project__field.icon.fa-calendar-day
         {:datetime (str (:date-published project))}
         (str "Released " (format-date (:date-published project)))]]
       [:p.project__blurb.text-copy
        (:description project)]]]]))


(defn render
  [{global-meta :meta projects :entries :as data}]
  (base/render
   :title (:site-title global-meta)
   :subtitle "active-parens/projects"
   :current :projects
   :content [:div.projects
             [:ul.projects__list
              (for [project projects]
                [:li.projects__item
                 (render-blurb project)])]]))
