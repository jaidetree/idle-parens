(ns idle-parens.blog.post
  (:require [idle-parens.base :as base]
            [idle-parens.formats :refer [format-date format-time]]))

(defn get-author
  [authors email]
  (->> authors
       (filter #(= (:email %) email))
       (first)))

(defn resolve-author
  [authors post]
  (let [author (get-author authors (:author-email post))]
    (merge {:name (:author-name post)
            :email (:author-email post)
            :avatar-url (or (:author-avatar post) (:author-gravatar post))}
           author)))

(defn render-author
  [post author]
  [:section.author
   [:p.author__intro (str "About the author")]
   [:div.author__content
    [:h2.author__name
     (:name author)]
    [:a.author__website
     {:href (:url author) :target "_blank"}
     (:url author)]
    [:div.author__profile
     [:div.author__avatar
      [:img.author__img {:src (:avatar-url author)
                         :alt (str "Avatar of " (:name author))}]]
     [:div.author__story
      [:p.author__bio (:bio author)]]]
    [:ul.author__links
     [:li.author__links-item
      [:a.author__link.brand.fa-github
       {:href (str "https://github.com/" (:github author))}
       (:github author)]]
     [:li.author__links-item
      [:a.author__link.brand.fa-twitter
       {:href (str "https://twitter.com/" (:twitter author))}
       (str "@" (:twitter author))]]
     [:li.author__links-item
      [:a.author__link.brand.fa-instagram
       {:href (str "https://instagram.com/" (:instagram author))}
       (str "@" (:instagram author))]]]]])

(defn render-meta
  [post author full?]
  [:div.post__attrs
   [:time.post__meta.post__date-time
    {:datetime (:date-published post)}
    [:span.post__date.icon.fa-calendar-day
     (format-date (:date-published post))]
    [:span.post__time.icon.fa-clock
     (format-time (:date-published post))]]
   [:span.post__meta
     [:span.post__ttr.icon.fa-stopwatch
      (str (:ttr post) " min. read")]
     (when (or full?
               (not= (:author-email post) "jayzawrotny@gmail.com"))
       [:span.post__author.icon.fa-user
        (if (:url author)
          [:a {:href (:url author) :target "_blank"}
           (:name author)]
          [:a {:href (str "mailto:" (:author-email post) :target "_blank")}
           (:author post)])])]])

(defn render-header
  [post author full?]
  [:header.post__header
   [:a.post__permalink.not-mobile
    {:href (:permalink post)}
    [:i.fa.fa-link]]
   [:div.borders.post__headline
    [:h1.post__title
     [:a.post__link {:href (:permalink post)}
       (:title post)]]
    (when full?
     [:div.drips
      (base/drip
       {:width 5
        :right 24
        :height 5
        :max-height 60
        :class "bg-slate"
        :top "100%"})
      (base/drip
       {:width 3
        :right 19
        :height 8
        :max-height 50
        :class "bg-slate"
        :top "100%"})
      (base/drip
       {:width 8
        :right 10
        :height 8
        :max-height 40
        :class "bg-slate"
        :top "100%"})])]
   (render-meta post author full?)
   (when full?
     [:ul.post__meta.post__tags.d-inline-block
      (for [tag (:tags post)]
        [:li.post__tag.d-inline-block.icon.fa-hashtag
         [:a {:href (str "/blog/tags/" tag ".html")} tag]])])])

(defn render-entry
  [{global-meta :meta posts :entries post :entry} content & {:keys [context]}]
  (let [full? (= content :content)
        author (resolve-author (:authors global-meta) post)]
    [:article.post
     {:class (when full? "full")}
     (render-header post author full?)
     (when content
       [:section.post__content
         {:class (if full? "post__full" "post__summary")}
         (content post)])
     (when (and (= context :post) (:bio author))
       [:footer.post__footer
        (render-author post author)
        [:div.actions.mt-5.text-center
         [:span.icon.fa-chevron-circle-left
          [:a
           {:href (str "/")}
           "View more posts"]]]])]))

(defn render [{global-meta :meta posts :entries post :entry :as data}]
  (base/render
   :title (:site-title global-meta)
   :subtitle (:title post)
   :current :blog
   :page-title "idle-parens.blog/post"
   :meta {:title (:title post)
          :description (:description post)
          :url (:canonical-url post)}
   :content (render-entry data :content
                          :context :post)))
