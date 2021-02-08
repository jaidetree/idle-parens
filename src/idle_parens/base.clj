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
  [{:keys [title class]}]
  [:section.profile.text-center
   {:class class}
   [:h1.profile__title [:a {:href "/"}
                        [:img.profile__logo {:src "/img/eccentric-j-logo.svg"
                                             :alt "Eccentric J's Logo"}]
                        [:span.color-bone
                         "eccentric"]
                        " J"]]
   [:ul.profile__trades [:li.profile__trade "Engineer"]
                        [:li.profile__trade "Designer"]
                        [:li.profile__trade "Artist"]]
   [:blockquote.profile__quote
    "Idle parentheses form the devil's playlists."]])

(defn navbar
  [{:keys [current]}]
  [:section.navbar
   [:nav.navbar__nav
     [:div.not-mobile
      (drip {:width 5
             :right 5
             :height 5
             :max-height 30
             :class "bg-teal"
             :top 0})
      (drip {:width 3
             :right 12
             :height 8
             :max-height 48
             :class "bg-teal"
             :top 0})
      (drip {:width 8
             :right 20
             :height 4
             :max-height 60
             :class "bg-teal"
             :top 0})]
     [:ul.navbar__list
      [:li.navbar__item {:class (when (or (= current :blog)
                                          (= current :post))
                                 "navbar__active")}
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
   [:li.media-links__item [:a.icon.fa-rss. {:href "/feed.rss"}
                           [:span.media-links__label "RSS"]]]
   [:li.media-links__item [:a.icon.fa-rss-square. {:href "/atom.xml"}
                           [:span.media-links__label "Atom"]]]
   [:li.media-links__item [:a.brand.fa-github-square {:href "https://github.com/eccentric-j"}
                           [:span.media-links__label "Github"]]]
   [:li.media-links__item [:a.icon.fa-envelope {:href "mailto:jayzawrotny@gmail.com"}
                           [:span.media-links__label "Email"]]]])

(defn head
  [title meta]
  [:head [:title title]
   [:meta {:charset "utf-8"}]
   [:meta {:http-equiv "X-UA-Compatible" :content "IE=edge,chrome=1"}]
   [:meta {:name "viewport" :content "width=device-width, initial-scale=1.0, user-scalable=no"}]
   [:meta {:property "og:image"
           :content "/img/eccentric-j-social.png"}]
   [:meta {:property "og:image:width"
           :content "279"}]
   [:meta {:property "og:image:height"
           :content "279"}]
   [:meta {:name "msapplication-TileColor" :content "#68F3CB"}]
   [:meta {:name "msapplication-TileImage" :content "/ms-icon-144x144.png"}]
   [:meta {:name "theme-color" :content "#68F3CB"}]
   (when meta
     (list
      [:meta {:property "og:title"
              :content (:title meta)}]
      [:meta {:property "og:description"
              :content (:description meta)}]
      [:meta {:property "og:url"
              :content (:url meta)}]))
   [:link {:rel "apple-touch-icon"
           :sizes "57x57"
           :href "/apple-icon-57x57.png"}]
   [:link {:rel "apple-touch-icon"
           :sizes "60x60"
           :href "/apple-icon-60x60.png"}]
   [:link {:rel "apple-touch-icon"
           :sizes "72x72"
           :href "/apple-icon-72x72.png"}]
   [:link {:rel "apple-touch-icon"
           :sizes "76x76"
           :href "/apple-icon-76x76.png"}]
   [:link {:rel "apple-touch-icon"
           :sizes "114x114"
           :href "/apple-icon-114x114.png"}]
   [:link {:rel "apple-touch-icon"
           :sizes "120x120"
           :href "/apple-icon-120x120.png"}]
   [:link {:rel "apple-touch-icon"
           :sizes "144x144"
           :href "/apple-icon-144x144.png"}]
   [:link {:rel "apple-touch-icon"
           :sizes "152x152"
           :href "/apple-icon-152x152.png"}]
   [:link {:rel "apple-touch-icon"
           :sizes "180x180"
           :href "/apple-icon-180x180.png"}]
   [:link {:rel "icon"
           :type "image/png"
           :sizes "192x192"
           :href "/android-icon-192x192.png"}]
   [:link {:rel "icon"
           :type "image/png"
           :sizes "32x32"
           :href "/android-icon-32x32.png"}]
   [:link {:rel "icon"
           :type "image/png"
           :sizes "96x96"
           :href "/android-icon-96x96.png"}]
   [:link {:rel "icon"
           :type "image/png"
           :sizes "16x16"
           :href "/android-icon-16x16.png"}]
   [:link {:rel "manifest"
           :href "/manifest.json"}]
   [:link {:rel "apple-touch-icon"
           :sizes "180x180"
           :href "/apple-touch-icon.png"}]
   [:link {:rel "icon"
           :type "image/png"
           :sizes "32x32"
           :href "/favicon-32x32.png"}]
   [:link {:rel "icon"
           :type "image/png"
           :sizes "16x16"
           :href "/favicon-16x16.png"}]
   [:link {:rel "manifest"
           :href "/site.webmanifest"}]
   [:link {:rel "mask-icon"
           :href "/safari-pinned-tab.svg"
           :color "#5bbad5"}]
   [:link {:rel "stylesheet"
           :href "//stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css"}]
   [:link {:rel "stylesheet"
           :href "https://use.fontawesome.com/releases/v5.6.3/css/all.css"}]
   [:link {:rel "stylesheet"
           :href "//cdnjs.cloudflare.com/ajax/libs/highlight.js/9.13.1/styles/atelier-cave-dark.min.css"}]
   [:link {:rel "stylesheet"
           :href "//fonts.googleapis.com/css?family=Seaweed+Script|Anonymous+Pro|Source+Sans+Pro"}]
   [:link {:rel "stylesheet"
           :href "/css/style.css"}]
   [:link {:rel "stylesheet"
           :media "screen and (max-width: 929px)"
           :href "/css/mobile.css"}]
   [:link {:rel "stylesheet"
           :media "screen and (min-width: 768px) and (max-width: 929px)"
           :href "/css/tablet.css"}]
   [:link {:rel "stylesheet"
           :media "screen and (min-width: 930px)"
           :href "/css/desktop.css"}]])

(defn format-path
  [title]
  (let [parts (split title #"[/\.]")
        dirs (butlast parts)
        name (last parts)]
    (concat
     (interpose [:span.page__sep "."]
                (map #(vector :span.page__path %) dirs))
     [[:span.page__sep "/"]
      [:span.page__path name]])))

(comment
 (clojure.pprint/pprint
   (format-path "idle-parens.blog/post")))

(defn render [& {:keys [title subtitle page-title content current meta] :as opts}]
  (let [home? (= current :blog)]
    (html5 {:lang "en" :itemtype "http://schema.org/Blog"}
      (head (str (when (and subtitle (not home?)) (str subtitle " | ")) title)
            (get opts :meta {}))
      [:body [:div.page
              (drip {:right 30
                     :width 10
                     :height 10
                     :class "bg-teal"})
              (drip {:right 56
                     :width 8
                     :height 8
                     :class "bg-teal"})
              (drip {:right 42
                     :width 5
                     :height 10
                     :class "bg-teal"})
              [:div.layout
               [:div.main
                [:header.page__header
                  [:h1.page__title
                   (format-path (or page-title subtitle ""))]]
                [:div.content
                  content]]
               [:aside.sidebar
                [:div.sidebar__inner
                 (profile {:title title})
                 (navbar {:current current})
                 (media-links)]]]]

             [:script {:src "//code.jquery.com/jquery-3.3.1.slim.min.js"}]
             [:script {:src "//stackpath.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js"}]
             [:script {:src "//cdnjs.cloudflare.com/ajax/libs/highlight.js/9.13.1/highlight.min.js"}]
             [:script {:src "//cdnjs.cloudflare.com/ajax/libs/highlight.js/9.13.1/languages/clojure.min.js"}]
             [:script {:src "/js/eccentric-app.js"}]
             [:script "hljs.initHighlightingOnLoad();"]])))
