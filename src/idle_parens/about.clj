(ns idle-parens.about
  (:require [hiccup.page :refer [html5]]
            [idle-parens.base :as base]
            [idle-parens.formats :refer [slugify]]))

(defn interest
  [& {:keys [title media content columns]}]
  (let [slug (slugify title)]
    [:li.interest
     [:header.interest__header
      [:h4.interest__title
       [:button.interest__toggle.toggle
        {:type "button"
         :data-toggle "collapse"
         :data-target (str "#" slug)
         :aria-expanded "false"
         :aria-controls slug}
        title
        [:span.toggle__icon
         [:span.d-none "Toggle Interest"]]]]]
     [:div.interest__content.collapse
      {:class (when columns "row") :id slug}
      (when media
        [:div.interest__media
         {:class (when columns "col-4")}
         media])
      [:p.text-copy.interest__copy
       {:class (when columns "col mt-4")}
       content]]]))

(defn skill
  [& {:keys [title value years content keywords]}]
  (let [slug (slugify title)]
   [:li.skill
    [:header.skill__header
     [:h4.skill__title
      [:button.skill__toggle.toggle
       {:type "button"
        :data-toggle "collapse"
        :data-target (str "#" slug)
        :aria-expanded "false"
        :aria-controls slug}
       title
       [:span.toggle__icon
        [:span.d-none "Toggle Interest"]]]]
     [:div.skill__progress
      [:button.skill__value
       {:style (str "width:" value "%;")
        :type "button"}
       [:span.skill__label
        (str value "%")]]]]
    [:div.skill__content.collapse
     {:id slug}
     [:ul.skill__keywords
      (for [kw keywords]
        [:li.skill__keyword kw])]
     [:p.text-copy.skill__copy
      content]]]))

(defn interests
  []
  [:section.section]
   [:h2 "Interests"]
   [:ul.interests.borders
    (interest
     :title "Atmospheric Black Metal"
     :media [:iframe {:width 560
                      :height 315
                      :src "https://www.youtube.com/embed/videoseries?list=UUDLkzWN1rHY4eYkGnVruHVw"
                      :frameborder 0
                      :allow "autoplay; encrypted-media"
                      :allowfullscreen true}]
     :content "Ah metal. Such a rich genre with so much creative potential. Of all the subgenres I find myself drawn most to the alluring spell of atomspheric black metal. A vast subgenre with a rich technicality to the instrumentation, complex layers of melody and spirit, and evocative themes. The biting edge of black metal vocals layered on top is like some kind of blood-soaked cherry on a deliciously ominous cupcake of abject pleasure.")

    (interest
     :title "Reading"
     :columns true
     :media
      [:a.display-block
       {:href "https://www.braveclojure.com/clojure-for-the-brave-and-true/"}
       [:img.interest__book.d-block
        {:src "/img/cbt.png"
         :alt "Clojure for the Brave and True"}]]
     :content "Books are really important to me, they&rsquo;re how I got into programming. I try to read every day when I can: Articles, books, comics, and even text-books. At the time of writing I've been on a sci-fi kick, please send any recommendations you may have.")

    (interest
     :title "Video Games"
     :media [:iframe {:width 560
                      :height 315
                      :src "https://www.youtube.com/embed/uYZ8mrnb_wg"
                      :frameborder 0
                      :allow "autoplay; encrypted-media"
                      :allowfullscreen true}]
     :content "Video games are the best. I love exploring imaginary worlds while practicing creative problem solving within inviting narratives. It's a beautiful thing when done well but for Hyper Light Drifter featured above? Doubly so.")

    (interest
     :title "Bullet Journaling"
     :media [:a
             {:href "https://bulletjournal.com/"
              :target "_blank"}
             [:img
              {:src "/img/eccentric-j-bujo.jpg"
               :alt "Eccentric J's 2019 Bullet Journal: January Month Spread"}]]
     :content "Introduced to the idea from a friend, I&rsquo;ve been bullet-journaling for going on 3 years now. It helps me build good habits and track my daily tasks while also encouraging balance between work, goals, and personal life.")

    (interest
     :title "Art"
     :media [:iframe.instagram-media-rendered
             {:allowfullscreen "true",
              :style
              "background: white none repeat scroll 0% 0%; max-width: 720px; width: calc(100% - 2px); border-radius: 3px; border: 1px solid rgb(219, 219, 219); box-shadow: none; display: block; margin: 0px 0px 12px; min-width: 326px; padding: 0px;"
              :src
              "https://www.instagram.com/p/BGvfID7rHwm/embed/?cr=1&v=12&wp=720"
              :frameborder "0"
              :allowtransparency "true"
              :data-instgrm-payload-id "instagram-media-payload-0"
              :scrolling "no"
              :height "700"}]
     :content "An interest I accquired later in life. I enjoy discovering artists through social media, going to art gallery openings in NYC, drawing youtube channels, and the occasional doodle. Admittedly, I'm not very good at drawing but I do enjoy learning it in waves every once in a while.")])

(defn skills
  []
  [:section.section
   [:h2 "Experience"]
   [:ul.skills.borders.scrolling
    (skill
     :title "Clojure"
     :value 70
     :years 1
     :keywords ["lein" "boot" "luminus"]
     :content "Currently my favorite language of choice. Clojure is productive, rewarding, and has a great community.")

    (skill
     :title "ClojureScript"
     :value 40
     :years 0.5
     :keywords ["figwheel-main" "lein-figwheel" "shadow-cljs"]
     :content "Currently learning the ropes. I&rsquo;m familiar enough in both Clojure and JavaScript to get work done in ClojureScript but I am not certain what &ldquo;idiomatic&rdquo; ClojureScript that scales looks like.")

    (skill
     :title "JavaScript"
     :value 90
     :years 10
     :keywords ["functional"
                "FRP"
                "React"
                "Redux"
                "RxJS"
                "Ramda"
                "Vue"
                "Angular"
                "Node"
                "Babel"
                "ES6"]
     :content "JavaScript is my strongest language to date. I have put more than a decade into learning it from the days of jQuery to creating robust web apps with React, Angular, or View. I do however much prefer React with Redux and RxJS over Angular or Vue, React follows the functional programming ideas that I think make software much more maintainable.")

    (skill
     :title "HTML\\CSS"
     :value 90
     :years 10
     :keywords ["HTML 5"
                "responsive"
                "grids"
                "bootstrap"
                "SMACSS"
                "atomic"
                "BEM"
                "SEO"]
     :content "One of my first, and strongest languages. I am experienced in writing reusable, semantic, standards-compliant, SEO-friendly HTML for both desktops and mobile devices.")

    (skill
     :title "Design"
     :value 80
     :years 8
     :keywords ["logos"
                "websites"
                "UI\\UX"
                "wireframes"
                "collateral"
                "Sketch"
                "Photoshop"
                "Illustrator"
                "Moqups"
                "Balsamiq"]
     :content "Design to me is less about the final presentation but more finding the right questions to ask and the right way to answer them through research, artistic expression, and empathy. I enjoy crafting work to create meaningful stories for people to explore, and I pride myself on doing so with a strong sense of craftsmanship leaving projects better than when I found it.")

    (skill
     :title "Python"
     :value 60
     :years 5
     :keywords ["django" "python 2.7" "flask" "OOP"]
     :content "Not my strongest or favorite language but it is a robust, widely used language that I am glad I took the time to get comfortable with.")

    (skill
     :title "Ruby"
     :value 30
     :years 1
     :keywords ["rails 3" "ruby 2"]
     :content "A brief stint before moving to Django and Python. The design of the language felt pleasant at first but the nuances became taxing to me early on.")

    (skill
     :title "PHP"
     :value 50
     :years 9
     :keywords ["php5"]
     :content "The first language I really took the time to learn. Looking back I can&rsquo;t say I made anything worthwhile in it but I did explore a lot of possibilities from database wrappers, template engines, to fully-featured frameworks and even a socket-based chat system.")]])

(defn education
  []
  [:div.section
   [:h2 "Education"]
   [:p.text-copy "I have a Bachelors in Digital Media from Kendall College of Art &amp; Design located in Grand Rapids, Michigan. I am a self-taught programmer but chose to study design in college to improve my weakest skill."]])

(defn render
  [{global-meta :meta posts :entries}]
  (base/render
   :current :about
   :title (:site-title global-meta)
   :subtitle "^meta/about"
   :content [:div.about
             [:h1.borders.padding "About Eccentric J"]
             [:p.text-copy
              "I've been designing and developing personal and commercial projects for about a decade now. I got started by learning how to hack online computer games by rewriting values to known memory addresses, since then I've found my passion in web app development. Currently, I live in New York City employed as a lead engineer at "
              [:a {:href "https://venuebook.com"}
               "VenueBook"]
              " where I am responsible for software architecture, development, and product design."]
             [:div.callout
              [:h3.borders.padding "Looking for Engineers"]
              [:p "VenueBook is hiring both a senior Clojure developer and a senior Python\\Devops engineer. Please reach out if you are interested or know someone who might be."]]
             (interests)
             (skills)
             (education)]))
