(ns idle-parens.about
  (:require [hiccup.page :refer [html5]]
            [idle-parens.base :as base]))

(defn render
  [{global-meta :meta posts :entries}]
  (clojure.pprint/pprint posts)
  (base/render
   :current :about
   :title (:site-title global-meta)
   :subtitle "^meta/about"
   :content [:div.about
             [:h1 "About Eccentric J"]
             [:p.text-copy
              "I've been designing and developing personal and commercial projects for about a decade now. I got started by learning how to hack online computer games by rewriting values to known memory addresses, since then I've found my passion in web app development. Currently, I'm employed as a lead engineer at "
              [:a {:href "https://venuebook.com"}
               "VenueBook"]
              " where I am responsible for software architecture, development, and product design."]
             [:p.callout
              "At VenueBook we are looking to hire a senior Clojure developer and a senior Python\\Devops engineer. If you are interested or know someone who might be: Please reach out to me."]
             [:h2 "Interests"]
             [:ul.interests
              [:li.interest
               [:h3.interest__title "Atmospheric Black Metal"]
               [:div.interest__media
                [:iframe {:width 560
                          :height 315
                          :src "https://www.youtube.com/embed/videoseries?list=UUDLkzWN1rHY4eYkGnVruHVw"
                          :frameborder 0
                          :allow "autoplay; encrypted-media"
                          :allowfullscreen true}]]
               [:p.text-copy.interest__copy
                "Ah metal. Such a rich genre with so much creative potential. Of all the subgenres I find myself drawn most to the alluring spell of atomspheric black metal. A vast subgenre with a rich technicality to the instrumentation, complex layers of melody and spirit, and evocative themes. The biting edge of black metal vocals layered on top is like some kind of blood-soaked cherry on a deliciously ominous cupcake of abject pleasure."]]

              [:li.interest
               [:h3.interest__title "Reading"]
               [:div.row
                [:div.interest__media.col-4
                 [:a.display-block
                  {:href "https://www.braveclojure.com/clojure-for-the-brave-and-true/"}
                  [:img.interest__book.d-block
                   {:src "/img/cbt.png"
                    :alt "Clojure for the Brave and True"}]]]
                [:p.text-copy.interest__copy.col.mt-4
                 "Books are really important to me, they&rsquo;re how I got into programming. I try to read every day when I can: Articles, books, comics, and even text-books. At the time of writing I've been on a sci-fi kick, please send any recommendations you may have."]]]

              [:li.interest
               [:h3.interest__title "Video Games"]
               [:div.interest__media
                [:iframe {:width 560
                          :height 315
                          :src "https://www.youtube.com/embed/uYZ8mrnb_wg"
                          :frameborder 0
                          :allow "autoplay; encrypted-media"
                          :allowfullscreen true}]]
               [:p.text-copy.interest__copy
                "Video games are the best. I love exploring imaginary worlds while practicing creative problem solving within inviting narratives. It's a beautiful thing when done well but for Hyper Light Drifter featured above? Doubly so."]]

              [:li.interest
               [:h3.interest__title "Bullet Journaling"]
               [:div.interest__media
                [:img
                 {:src "/img/eccentric-j-bujo.jpg"
                  :alt "Eccentric J's 2019 Bullet Journal: January Month Spread"}]]
               [:p.text-copy.interest__copy
                "Introduced to the idea from a friend, I&rsquo;ve been bullet-journaling for going on 3 years now. It helps me build good habits and track my daily tasks while also encouraging balance between work, goals, and personal life."]]

              [:li.interest
               [:h3.interest__title "Art"]
               [:div.interest__media
                [:iframe#instagram-media.instagram-media-rendered
                 {:allowfullscreen "true",
                  :style
                  "background: white none repeat scroll 0% 0%; max-width: 720px; width: calc(100% - 2px); border-radius: 3px; border: 1px solid rgb(219, 219, 219); box-shadow: none; display: block; margin: 0px 0px 12px; min-width: 326px; padding: 0px;"
                  :src
                  "https://www.instagram.com/p/BGvfID7rHwm/embed/?cr=1&v=12&wp=720"
                  :frameborder "0"
                  :allowtransparency "true"
                  :data-instgrm-payload-id "instagram-media-payload-0"
                  :scrolling "no"
                  :height "700"}]]
               [:p.text-copy.interest__copy
                "An interest I accquired later in life. I enjoy discovering artists through social media, going to art gallery openings in NYC, drawing youtube channels, and the occasional doodle. Admittedly, I'm not very good at drawing but I do enjoy learning it in waves every once in a while."]]]]))
