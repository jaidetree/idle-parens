(ns idle-parens.base
  (:require [hiccup.page :refer [html5]]
            [clojure.string :refer [join split]]))

(defn rems
  [px]
  (if (string? px)
    px
    (str (double (/ px 16)) "rem")))

(defn drip
  [{:keys [top left right bottom width height class max-height]} & args]
  [:i.drip {:style (str (when right (str "right:" (rems right) ";"))
                        (when left (str "left:" (rems left) ";"))
                        (when top (str "top:" (rems top) ";"))
                        (when bottom (str "bottom:" (rems bottom) ";"))
                        (when max-height (str "max-height:" (rems max-height) ";"))
                        "width:" (rems width) ";"
                        "height:" (rems height) ";")
            :class (if class class "")}])

(defn profile
  [{:keys [title]}]
  [:section.profile.text-center.mb-5
   [:h1.profile__title [:a {:href "/"}
                        [:img.profile__logo {:src "/img/eccentric-j-logo.svg"
                                             :alt "Eccentric J's Logo"}]
                        title]]
   [:ul.profile__trades [:li.profile__trade "Artist"]
                        [:li.profile__trade "Designer"]
                        [:li.profile__trade "Engineer"]]
   [:blockquote.profile__quote "Idle parentheses become the devil's playlists."]])

(defn navbar
  [{:keys [current]}]
  [:section.navbar.mb-3
   [:nav.navbar__nav.borders
     (drip {:width 5
            :right 5
            :height 5
            :max-height 30
            :class "bg-slate"
            :top 0})
     (drip {:width 3
            :right 12
            :height 8
            :max-height 48
            :class "bg-slate"
            :top 0})
     (drip {:width 8
            :right 20
            :height 4
            :max-height 60
            :class "bg-slate"
            :top 0})
     [:ul.navbar__list
      [:li.navbar__item {:class (when (= current :blog) "navbar__active")}
                        [:a {:href "/"}
                            "idle-parens"
                            [:span.navbar__label "(blog)"]]]
      [:li.navbar__item {:class (when (= current :projects) "navbar__active")}
                        [:a {:href "/projects.html"}
                            "active-parens"
                            [:span.navbar__label "(projects)"]]]
      [:li.navbar__item {:class (when (= current :about) "navbar__active")}
                        [:a {:href "/about.html"}
                            "meta"
                            [:span.navbar__label "(about)"]]]]]])

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

(defn head
  [title]
  [:head [:title title]
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
                 :href "//fonts.googleapis.com/css?family=Seaweed+Script|Anonymous+Pro|Thasadith|Quicksand"}]
         [:link {:rel "stylesheet"
                 :href "/css/style.css"}]])

(defn render [& {:keys [title subtitle content current]}]
  (let [home? (= current :blog)]
    (html5 {:lang "en" :itemtype "http://schema.org/Blog"}
      (head (str (when (and subtitle (not home?)) (str subtitle " | ")) title))
      [:body [:div.page
              (drip {:right 40
                     :width 10
                     :height 10
                     :class "bg-teal"})
              (drip {:right 66
                     :width 8
                     :height 8
                     :class "bg-teal"})
              (drip {:right 52
                     :width 5
                     :height 10
                     :class "bg-teal"})
              (comment [:div.hero])
              [:div.container
                [:div.row
                 [:div.main.col-md-8
                  [:h1.page__title (for [part (split (or subtitle "") #"/")]
                                    [:span.page__path part])]
                  content]
                 [:aside.sidebar
                  (profile {:title title})
                  (navbar {:current current})
                  (media-links)]]]]

             [:script {:src "//code.jquery.com/jquery-3.3.1.slim.min.js"}]
             [:script {:src "//stackpath.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js"}]
             [:script {:src "//cdnjs.cloudflare.com/ajax/libs/highlight.js/9.13.1/highlight.min.js"}]
             [:script {:src "//cdnjs.cloudflare.com/ajax/libs/highlight.js/9.13.1/languages/clojure.min.js"}]
             [:script {:src "/js/eccentric-app.js"}]
             [:script "hljs.initHighlightingOnLoad();"]])))
