(ns eccentric-j.site.base
  (:require [hiccup.page :refer [html5]]))

(defn render [& {:keys [title subtitle content]}]
  (html5 {:lang "en" :itemtype "http://schema.org/Blog"}
    [:head
      [:title (when subtitle (str subtitle " | ")) title]
      [:meta {:charset "utf-8"}]
      [:meta {:http-equiv "X-UA-Compatible" :content "IE=edge,chrome=1"}]
      [:meta {:name "viewport" :content "width=device-width, initial-scale=1.0, user-scalable=no"}]
      [:link {:rel "stylesheet"
              :href "//maxcdn.bootstrapcdn.com/bootstrap/4.1.1/css/bootstrap.min.css"}]
      [:link {:rel "stylesheet"
              :href "//cdnjs.cloudflare.com/ajax/libs/highlight.js/9.13.1/styles/atom-one-dark-reasonable.min.css"}]
      [:link {:rel "stylesheet"
              :href "//maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css"}]
      [:link {:rel "stylesheet"
              :href "/css/style.css"}]]
    [:body [:nav
            [:ul
             [:li [:a {:href "/about.html"} "About Page"]]
             [:li [:a {:href "/feed.rss"} "RSS"]]
             [:li [:a {:href "/atom.xml"} "Atom Feed"]]]]
           content
           [:script {:src "//maxcdn.bootstrapcdn.com/bootstrap/4.1.1/js/bootstrap.min.js"}]
           [:script {:src "//cdnjs.cloudflare.com/ajax/libs/highlight.js/9.13.1/highlight.min.js"}]
           [:script {:src "//cdnjs.cloudflare.com/ajax/libs/highlight.js/9.13.1/languages/clojure.min.js"}]
           [:script "hljs.initHighlightingOnLoad();"]]))
