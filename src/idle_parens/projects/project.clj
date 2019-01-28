(ns idle-parens.projects.project
  (:require
   [clojure.java.io :as io]
   [clojure.string :refer [join trim]]
   [idle-parens.base :as base]
   [idle-parens.formats :refer [format-date format-time]]))

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

(defn project-github
  [github-str]
  (when github-str
    (let [url (str "https://github.com/" github-str)]
      [:span.project__field.brand.fa-github
       [:a.project__meta-link
        {:href url}
        github-str]])))

(defn project-website
  [website-url]
  (when website-url
    [:span.project__field.icon.fa-external-link-alt
     [:a.project__meta-link
      {:href website-url}
      website-url]]))

(defn render-entry
  [{global-meta :meta projects :entries project :entry} & {:keys [context]}]
  (let [img (project-img (:slug project))]
    [:article.project
     {:class (trim
              (join " "
                    [(str "project--slug_" (:slug project))
                     (:class project "")]))}
     [:header.project__header
      [:div.project__hero
       [:img.project__img
        {:src (str "/img/projects/" (:slug project) ".png")
         :alt (str "Teaser image of " (:title project))}]]
      [:div.project__info
       [:h1.project__title.borders.padding
        [:a.project__link
         {:href (:permalink project)}
         (:title project)]]
       [:div.project__meta
        [:time.project__field.icon.fa-calendar-day
         {:datetime (str (:date-published project))}
         (str "Released " (format-date (:date-published project)))]
        (project-github (:github project))
        (project-website (:website project))
        [:div.project__langs
         [:ul.tags
          (for [lang (:languages project)]
            [:li.tag lang])]]]
       [:p.project__blurb.text-copy
        (:description project)]]]
     [:div.post__content.project__content
      (:content project)]]))

(defn render [{global-meta :meta posts :entries post :entry :as data}]
  (base/render
   :title (:site-title global-meta)
   :subtitle (:title post)
   :page-title "active-parens/projects"
   :current :projects
   :content (render-entry data
                          :context :project)))
