(ns eccentric-j.site.base
  (:require [hiccup.page :refer [html5]]
            [clojure.string :refer [join]]))

(defn rems
  [px]
  (double (/ px 16)))

(defn drip
  [{:keys [top left right bottom width height class max-height]} & args]
  [:i.drip {:style (str (when right (str "right:" (rems right) "rem;"))
                        (when left (str "left:" (rems left) "rem;"))
                        (when top (str "top:" (rems top) "rem;"))
                        (when bottom (str "bottom:" (rems bottom) "rem;"))
                        (when max-height (str "max-height:" (rems max-height) "rem;"))
                        "width:" (rems width) "rem;"
                        "height:" (rems height) "rem;")
            :class (if class class "")}])

(defn profile
  [{:keys [title]}]
  [:section.profile.text-center.mb-5
   [:h1.profile__title [:a {:href "/"} title]]
   [:ul.profile__trades [:li.profile__trade "Artist"]
                        [:li.profile__trade "Designer"]
                        [:li.profile__trade "Engineer"]]])

(defn navbar
  []
  [:section.navbar.mb-3
   [:nav.navbar__nav.borders
     (drip {:width 5
            :right 5
            :height 5
            :max-height 30
            :class "bg-slate"})
     (drip {:width 3
            :right 12
            :height 8
            :max-height 48
            :class "bg-slate"})
     (drip {:width 8
            :right 20
            :height 4
            :max-height 60
            :class "bg-slate"})
     [:ul.navbar__list
      [:li.navbar__item [:a {:href "/"} "Blog"]]
      [:li.navbar__item [:a {:href "/about.html"} "About"]]
      [:li.navbar__item [:a {:href "/projects.html"} "Projects"]]]]])

(defn media-links
  []
  [:ul.media-links.d-flex.flex-row.justify-content-between
   [:li.media-links__item [:a {:href "/feed.rss"}
                           [:i.icon.fa.fa-rss]
                           [:span.media-links__label "RSS"]]]
   [:li.media-links__item [:a {:href "/atom.xml"}
                           [:i.icon.fa.fa-rss-square]
                           [:span.media-links__label "Atom"]]]
   [:li.media-links__item [:a {:href "https://github.com/eccentric-j"}
                           [:i.icon.fa.fa-github]
                           [:span.media-links__label "Github"]]]
   [:li.media-links__item [:a {:href "mailto:jayzawrotny@gmail.com"}
                           [:i.icon.fa.fa-envelope]
                           [:span.media-links__label "Email"]]]])

(defn render [& {:keys [title subtitle content]}]
  (html5 {:lang "en" :itemtype "http://schema.org/Blog"}
    [:head
      [:title (str (when subtitle (str subtitle " | ")) title)]
      [:meta {:charset "utf-8"}]
      [:meta {:http-equiv "X-UA-Compatible" :content "IE=edge,chrome=1"}]
      [:meta {:name "viewport" :content "width=device-width, initial-scale=1.0, user-scalable=no"}]
      [:link {:rel "stylesheet"
              :href "//stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css"}]
      [:link {:rel "stylesheet"
              :href "//maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css"}]
      [:link {:rel "stylesheet"
              :href "//cdnjs.cloudflare.com/ajax/libs/highlight.js/9.13.1/styles/atom-one-dark-reasonable.min.css"}]
      [:link {:rel "stylesheet"
              :href "//fonts.googleapis.com/css?family=Seaweed+Script|Anonymous+Pro"}]
      [:link {:rel "stylesheet"
              :href "/css/style.css"}]]
    [:body [:div.page
            (drip {:right 40
                   :width 10
                   :height 10})
            (drip {:right 66
                   :width 8
                   :height 8})
            (drip {:right 52
                   :width 5
                   :height 10})
            [:div.container.inline-block
             [:header.container
              [:div.row
               [:div.main.col-md-8
                content]
               [:aside.sidebar.col-lg-3
                (profile {:title title})
                (navbar)
                (media-links)]]]]]

           [:script {:src "//code.jquery.com/jquery-3.3.1.slim.min.js"}]
           [:script {:src "//stackpath.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js"}]
           [:script {:src "//cdnjs.cloudflare.com/ajax/libs/highlight.js/9.13.1/highlight.min.js"}]
           [:script {:src "//cdnjs.cloudflare.com/ajax/libs/highlight.js/9.13.1/languages/clojure.min.js"}]
           [:script {:src "/js/app.js"}]
           [:script "hljs.initHighlightingOnLoad();"]]))
