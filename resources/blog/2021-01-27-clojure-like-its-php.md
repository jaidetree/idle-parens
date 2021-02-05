---
type: post
title: "Clojure like it's PHP"
description: "Using babashka we can serve websites built with Clojure on the cheapest of shared hosting providers"
author: Eccentric J
author-email: jayzawrotny@gmail.com
location: New York, USA
date-created: 2021-01-27T20:00:00-4:00
date-modified: 2021-01-27T20:00:00-4:00
date-published: 2021-01-27T20:00:00-4:00
in-language: en
uuid: E6C63B20-E6DF-48B0-83A6-3506AB12A118
tags:
 - experiment
 - web
 - clojure
---


A few days back in the Doom Emacs Discord I shared a wish for a Clojure implementation that compiles to PHP. I am pining for the days of drafting something up, deploying it with a single transfer, then moving on like when managing multiple PHP sites on a bulk shared server. Clojure, much like working with Ruby or Python, are slightly different beasts that require more powerful hosting capabilities to support web apps which come at either the cost of more time, money, or both. Thankfully, Taco and Rushsteve of the Doom Emacs community pointed out that one could run Clojure with cgi-scripts running binaries built from the graalvm or through babashka. That would be a dream come true given that

## It's Alive!

After a bit of trial and error along with some help from Didibus in the Clojurian's Slack I got a cgi-script running a Clojure script with Babashka to return HTML to the browser! This article breaks down the process to share what I did and answer some questions you may have. A demo repo is in the works as well that can serve as a real-world example.

<blockquote class="disclaimer">

  **Disclaimer**: The credit for coming up with this idea goes to Taco ([Elais Player](https://elais.codes/)) in the Doom Emacs Discord. Also thanks to [rushsteve1](https://rushsteve1.us) for talking through the approach with me as well as Didibus of the Clojurians' Slack for helping me get the first script running.

</blockquote>

## In a Nutshell

In general the setup is as follows:

1. User visits example.com/cgi-bin/demo.clj
2. The webserver (typically Apache) runs the demo.clj file
3. The demo.clj has a shebang to process it through the babashka binary
4. Return the string output consisting of the response headers and HTML body to the user's browser

## Why not just use PHP on shared servers?

I would love to come at this with a perfectly logical and objective reason why Clojure is ultimately a better choice on shared hosts via CGI-script, but I can't. What I can tell you is that I enjoy working with Clojure a lot more than PHP currently. I also feel Clojure third-party code is pretty consistent in style and design where as PHP has many options for how to accomplish any particular task with sometimes conflicting or wildly different approaches. Another bonus is that as of [Babashka 0.2.8](https://github.com/babashka/babashka/blob/master/CHANGELOG.md#v028), which came out the day I gave this a shot, now includes hiccup which is a great HTML templaing solution in Clojure.

That said this is still pretty experimental so it may take some more learning before this becomes viable for a production-grade project, but I do think as-is it offers a great opportunity to hack together useful web resources using Clojure.

## What are we going to build?

Below is a guide for creating a small webpage that displays the ranking, categories, and summaries of bands which also displays an embedded YouTube video. Because I lack taste, I've chosen to share some of my favorite metal bands. Feel free to follow along or customize it to what you enjoy if you're feeling confident.

The purpose of this project is to demonstrate pulling in babashka-friendly Clojure libraries like gaka and honeysql, as well as explore connecting to a Postgres database and rendering out a rich page.

[![Screenshot of the Example Project this Guide Details](/img/blog/6-clj-cgi-example.jpg)](https://cgi.eccentric-j.com/metal.clj)

https://cgi.eccentric-j.com/metal.clj

Additionally the source for the example on github is at:

https://github.com/eccentric-j/clj-cgi-example


## Requirements

1. A host that supports cgi-scripts
2. The babashka static binary
3. The babashka postgresql pod static binary
4. Create a postgres database and example table
5. A Clojure script to return an HTML response

### Directory Structure

```
~/public_html/clojure-web-examples
|
| lib/
| | honeysql.jar
| | gaka.jar
|
| metal/
| | core.clj
| | style.clj
|
| metal.clj
| prod.secrets.edn
```

### Hosting

What makes this approach appealing is that it can work on the cheapest of web hosting offerings: shared hosts. This mean you can buy one account and support multiple sites, and even share libraries and code between them. All that's required is finding a shared web-host that offers custom cgi-scripts. A Virtual-Private-Server (VPS) could work as well but may take more configuration and more care towards security.

I am using https://bluehost.com, that said I got the account 3 years ago 2018 so be sure the shared hosting service you select does support cgi-scripts.

#### Apache Handlers

Depending on the shared host chosen, you may have the option of updating the Apache Handlers. This allows us to specify that when requesting a .clj file to run it as a cgi-script. If this option is not available, you will likely have to place your entry script in your `cgi-bin` directory.

### Download & Install Babashka Static Binary

[Babashka](https://github.com/babashka/babashka), created by Borkdude (Michiel Borkent), is the binary executable that interprets Clojure code from a `.clj` or `.jar` file and runs it. For example, `bb fetch-contacts.clj`. The Babashka binary contains the Clojure Java core library, Borkdude&rsquo;s [sci](https://github.com/borkdude/sci) (Small Clojure Interpretor) library, a CLI, and some common, convenient third-party libraries all compiled into a native app using [Graal VM](https://www.graalvm.org/). This allows Babashka to run a Clojure file fresh in < 10ms which is perfect for our webserver scripting purposes!

Visit the releases page:
https://github.com/babashka/babashka/releases

Download the static linux zip or whichever one that closely matches your shared hosting service. At the time of writing, I downloaded `babashka-0.2.8-linux-static-amd64.zip`. Once downloaded, extract it on your local computer, then upload the bb binary somewhere like `~/bin/bb` on your shared host. It's a good idea to keep the binary directly out of reach of your public_html folder so remote scripts can't remotely run code against it. Make sure the permissions are 755 so that it can execute from public web requests.

### Download & Install Babashka Postgresql Pod

This sample project should also connect to a Postgres database. The common JDBC library itself does not support Babashka out of the box due to its dependency on the JVM specifics it's designed to run on. Fortunately, Babashka offers pods which allow programs written and compiled in different languages to communicate with babashka scripts. When using Babashka on your local machine for instance, the pods API automatically downloads and loads the desired pod. On my shared host, that functionality did not work as it was downloading and building binaries that depended on specific build tools on the server. Fortunately there is a workaround by uploading the static build!

This step is similar to installing Babashka, visit https://github.com/babashka/babashka-sql-pods/releases and download the latest `pod-babashka-postgresql-x.y.z-linux-static-amd64.zip`, extract it on your local machine, then upload the binary to the lib directory we are going to use like `/public_html/lib/pod-babashka-postgresl`. Once again ensure it has 755 permissions so it runs publicly through the cgi-script.

The upload path is important as that is where Babashka expects to load pods from.

### Create a postgres database with an example table

Lacking a strong idea for an example db I went with what I know: A sample table of metal bands and their subjective ranking. What is in this database really doesn&rsquo;t matter, we just need something to show our connections and queries work for this project.

Run the following SQL query:

```sql
SET client_encoding = 'UTF8';

CREATE TABLE metal_bands (
    id integer NOT NULL,
    name character varying(255) NOT NULL,
    genre character varying(100) NOT NULL,
    popularity integer,
    rank integer NOT NULL,
    recommended_album character varying(255),
    music_video text,
    comment text
);

COMMENT ON COLUMN metal_bands.popularity IS 'Out of 5';

CREATE SEQUENCE metal_bands_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;

ALTER SEQUENCE metal_bands_id_seq OWNED BY metal_bands.id;

SELECT pg_catalog.setval('metal_bands_id_seq', 9, true);

ALTER TABLE ONLY metal_bands ALTER COLUMN id SET DEFAULT nextval('metal_bands_id_seq'::regclass);

INSERT INTO metal_bands VALUES (1, 'Slipknot', 'nu-metal', 5, 1, 'Slipknot (1999)', 'https://www.youtube.com/watch?v=ZPUZwriSX4M', 'Much angsty and yet much iconic');
INSERT INTO metal_bands VALUES (8, 'Soilwork', 'melodic-death-metal', 3, 4, 'The Ride Majestic (2015)', 'https://www.youtube.com/watch?v=kme-sxY2pAQ', 'A departure from their previous sound but builds on their strengths with catchy cleans accented by heavy moments');
INSERT INTO metal_bands VALUES (4, 'Mastodon', 'progressive', 4, 9, 'Leviathan (2004)', 'https://www.youtube.com/watch?v=labytsb3gfI', 'Catchy hooks, dynamic melodies, energetic vocals accompanied by consistent concepts to their albums');
INSERT INTO metal_bands VALUES (2, 'Converge', 'post-hardcore', 3, 6, 'All We Love We Leave Behind (2012)', 'https://www.youtube.com/watch?v=CrcY9I-BbjM', 'Unique band with hardcore and punk roots');
INSERT INTO metal_bands VALUES (6, 'Lifelover', 'suicidal-depressive-black-metal', 1, 2, 'Dekadens (2009)', 'https://www.youtube.com/watch?v=uhqofz6xPCc', 'A hauntingly tragic sound that adds some lofi energy to the typical 11/10 black metal genre.');
INSERT INTO metal_bands VALUES (3, 'Igorrr', 'avant-garde/experimental', 2, 7, 'Hallelujah (2012)', 'https://www.youtube.com/watch?v=rEuMAqfuCrI', 'Combines sampling, classical music, and EDM into chaotic but beautiful compositions');
INSERT INTO metal_bands VALUES (7, 'Devil Master', 'black-metal/rock', 1, 8, 'Satan Spits on Children of Light (2019)', 'https://www.youtube.com/watch?v=bWIrLDKY_Ew', 'A very high-energy fusion of black metal, punk, and rock');
INSERT INTO metal_bands VALUES (9, 'Shade Empire', 'symphonic-black-metal', 2, 5, 'Omega Arcane (2013)', 'https://www.youtube.com/watch?v=rBqaWfnCRZE', 'Story driven black metal mixed with some striking symphonic elements creating a very beautiful and heavy wave of sound');
INSERT INTO metal_bands VALUES (5, 'Antigama', 'mathgrind', 2, 3, 'The Insolent (2016)', 'https://www.youtube.com/watch?v=-Qt4UwwbIZs', 'Quite possibly the most abrasive band on this list, it''s not meant to be comfortable you endure it and feel fortunate to have made it through');

ALTER TABLE ONLY metal_bands
    ADD CONSTRAINT metal_bands_pkey PRIMARY KEY (id);
```

## Adding libraries like HoneySQL

We could write a raw SQL query string but where is the fun in that? Fortunately Clojure offers HoneySQL, a library for expressing queries in the valid Clojure EDN format. In a JVM Clojure application we could use a tool like clj and deps.edn to automatically download and install the libraries we need. Depending on your shared host, that's not going to work. Fortuantely, all is not lost! We can specify libraries in a local deps.edn and use Clojure's `clj` CLI with Babashka to create an uberjar.

First, create a `deps.edn` similar to the following:

```clojure
{:paths []
 :deps {honeysql/honeysql {:mvn/version "1.0.444"}}
 :aliases {:remove-clojure {:classpath-overrides {src nil
                                                  org.clojure/clojure nil
                                                  org.clojure/spec.alpha nil
                                                  org.clojure/core.specs.alpha nil}}}}
```

The `clojure` CLI can read this file and download the libraries we want to work with. The alias is important so that Babashka can filter out redundant libraries to include in the uberjar.

Second, run this babashka command to create an uberjar:

```bash
bb -cp $(clojure -Aremove-clojure -Spath) --uberjar honeysql.jar

```

Upload this file somewhere wise, typically out of reach of web traffic. Keep note of the path as we'll need to add it to the classpaths in Babashka manually.


## Creating a Clojure script to run through CGI

First, we are going to create an entrypoint to configure our project environment. This will be the script that runs directly through babashka.

I have named this file `metal.clj` this is the file users will be requesting in their browsers.


```clojure
#!/bin/env /home1/<username>/bin/bb

(ns cgi.metal
  (:require
   [babashka.classpath :refer [add-classpath]]
   [babashka.pods :as pods]
   [clojure.java.shell :refer [sh]]
   [clojure.string :as s]
   [hiccup2.core :refer [html]]))

;; Dynamic Libs
(def CWD (or (System/getenv "DOCUMENT_ROOT") (System/getenv "PWD")))

(defn lib
  "
  Create an absolute path to a jar file in sibling lib directory
  Takes a string filename like \"honeysql.jar\"
  Returns a string like \"/path/to/dir/lib/honeysql.jar\".
  "
  [path]
  (str CWD "/lib/" path))

;; Add jars and current directory to classpath to import library code

(add-classpath (s/join "::" [CWD                      ;; (1)
                             (lib "gaka.jar")
                             (lib "honeysql.jar")]))
(pods/load-pod (lib "pod-babashka-postgres"))

;; Require our main page code
;; Must be placed here after updating the class path

(require
 '[metal.core :as metal])                             ;; (2)

;; CGI scripts must print headers then body

(println "Content-type:text/html\r\n")                ;; (3)
(println (str (html metal/content)))                  ;; (4)

(System/exit 1)
```

1. Given the restricted permissions and applications available in the host, automatically installing libraries will not work. To work around this I created jars of each library I want and upload them to a shared dir on the host so multiple projects can use them. Should you find a solution though please share it!

2. With the classpaths updated, we can now import nearby source files.

3. CGI scripts must return the headers before the body response. If it's not formatted correctly visiting the page will render a 500 server error response.

Now lets create a source file that actually renders some content! For this project I've named it `metal/core.clj` because it's both a common Clojure idiom, and a subgenre of metal music.


```clojure
(ns metal.core
  (:require
   [clojure.string :as s]
   [hiccup.util :refer [raw-string]]
   [honeysql.core :as sql]
   [pod.babashka.postgresql :as pg]
   [gaka.core :refer [css]]
   [metal.guide :as guide]
   [metal.style :refer [rems]]))

;; Load secrets for the db
(def secrets (read-string (slurp "prod.secret.edn")))

;; Connect to the database
(def db {:dbtype   "postgresql"
         :host     "localhost"
         :dbname   (:db/name secrets)
         :user     (:db/user secrets)
         :password (:db/password secrets)
         :port     5432})

;; Fetch bands from the database
(def bands (pg/execute! db (sql/format {:select [:*]
                                        :from [:metal_bands]
                                        :order-by [[:rank :desc]]})))

(defn embed-url
  "Transforms a public youtube URL to the embedded URL"
  [yt-url]
  (as-> yt-url $
    (s/split $ #"=")
    (drop 1 $)
    (s/join "" $)
    (str "https://youtube.com/embed/" $)))

(defn popularity
  [pop-rank]
  (str (s/join "" (repeat pop-rank "★"))
       (s/join "" (repeat (- 5 pop-rank) "☆"))))

(def style
  (css
      [:body
       {:padding 0
        :margin 0
        :font-family :sans-serif
        :background-color "#E5E5E5"}]
      [:h1
       {:font-size (rems 32)}]
      [:h2
       {:font-size (rems 24)
        :font-family "\"Metal Mania\", sans-serif"}]
      [:h3
       {:font-size (rems 20)}]
      [:h4
       {:font-size (rems 18)}]
      [:.example
       {:padding "2rem"
        :text-align :center
        :margin-bottom "4rem"}]
      [:.cards
       {:list-style "none"
        :display "flex"
        :flex-flow "row wrap"
        :align-items "stretch"
        :justify-content "space-evenly"
        :margin "0 auto"
        :padding "0"
        :max-width "1100px"}]
      [:.cards__item
       {:flex "0 0 320px"
        :margin "1rem"}]
      [:.rank
       {:position :absolute
        :right "-16px"
        :top "-16px"
        :z-index 100
        :border-radius "50%"
        :width "32px"
        :height "32px"
        :background "#fff"
        :line-height "32px"
        :text-align :center
        :font-size (rems 14)
        :font-style :italic
        :color "#666"}]
      [:.card
       {:background-color "#FFF"
        :box-sizing :border-box
        :width "320px"
        :height "100%"
        :display "block"
        :position :relative
        :text-align :left
        :border-bottom-left-radius "5px"
        :border-bottom-right-radius "5px"
        :box-shadow "0 0 10px 0 rgba(0, 0, 0, 0.2)"}]
      [:.card__body
       {:padding "1rem"}]
      [:.detail
       {:margin-bottom "1rem"}]
      [:.label
       {:display "block"
        :font-weight "bold"}]
      [:.comment
       {:font-size (rems 14)
        :line-height 1.4}]))

(def example
  [:section.example
   [:h1 "Running Clojure as a CGI-bin Script Example"]
   [:h2 "Example Project"]
   [:p "A Metal Subgenre Sample Platter"]
   [:ul.cards
    (for [band bands]
      [:li.cards__item
       [:div.card
        [:span.rank
         (inc (- (count bands) (:metal_bands/rank band)))]
        [:iframe
         {:width "320"
          :height "180"
          :src (embed-url (:metal_bands/music_video band))
          :frameborder "0"
          :allow "accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"
          :allowfullscreen true}]
        [:div.card__body
         [:h3 (:metal_bands/name band)]
         [:div.detail
          [:span.label "Genre"]
          [:span (:metal_bands/genre band)]]
         [:div.detail
          [:span.label "Popularity"]
          [:span (popularity (:metal_bands/popularity band))]]
         [:div.detail
          [:span.label "Recommended Album"]
          [:span
           {:style {:font-style "italic"}}
           (:metal_bands/recommended_album band)]]
         [:p.comment
          (:metal_bands/comment band)]]]])]])

(def content
  [:html
   [:head
    [:title "Live Clojure CGI Script Example"]
    [:meta {:charset "UTF-8"}]
    [:link {:rel "preconnect"
            :href "https://fonts.gstatic.com"}]
    [:link {:rel "stylesheet"
            :href "https://fonts.googleapis.com/css2?family=Metal+Mania&display=swap"}]
    [:link {:rel "stylesheet"
            :href "//cdnjs.cloudflare.com/ajax/libs/highlight.js/9.13.1/styles/atelier-cave-dark.min.css"}]
    [:style (raw-string style)]
   [:body
    [:div#page
     example]
    [:script {:src "//cdnjs.cloudflare.com/ajax/libs/highlight.js/9.13.1/highlight.min.js"}]
    [:script {:src "//cdnjs.cloudflare.com/ajax/libs/highlight.js/9.13.1/languages/clojure.min.js"}]
    [:script "hljs.initHighlightingOnLoad();"]
    ]])
```

## Testing & Debugging

When everything works, it really works. Visit the `https://yourhost.domain/metal.clj` script and the page should render. If there is even a slight error you are likely going to get a generic 500 error response. Your host might be logging these around your home folder, or you may find a logging page in the hosting cPanel. This will be your first goto to figuring out what went wrong. The most common error you will likely run into is not setting the permission of your public Clojure scripts to `755`. Fix that by running a bash command like `chmod +x metal.clj` on the server.

If logging is not revealing the problem, another way to go about debugging is to run the script through the shell on your host. Simply ssh into your host and run `bb ./metal.clj` and see if it outputs the desired HTML. This will produce much more helpful output if a runtime error occurs. Be aware though that different environment variables change depending on if the script is running through the cgi-bin from the browser request or locally from a terminal.

## Tradeoffs

- Installing other libraries is not as streamlined as with the JVM or ClojureScript, you will likely need to manually uberjar and upload them.
- Some JVM specific libraries may not work with Babashka which then requires pods to provide separate CLIs Babashka can communicate with.
- This is not the most performant option in the world, it's pretty quick, especially compared to typical JVM startup times but there are few more layers in play here, but about the same as a typical PHP setup. Additionally shared hosting is not the fastest route anyway.


## Predicted Questions and Answers


## Alternatives

One alternative I considered but have not explored is using Graal VM to compile a static binary that contains ethe entire web app script and supporting libraries. The downside of this approach is that if any file changes, you will need to rebuild the binary and upload it to the server. Then you can access it directly via cgi-bin.

Lastly the dream of Clojure compiled to PHP is closer to a reality than previously thought! Github user alekcz (Alexander Oloo) has begun drafting [PCP](https://github.com/alekcz/pcp): A Clojure processor that runs a SCGI server that can act as a replacement for a php-fpm process. This may not be best suited for a shared hosting service but would definitely be good for a VPS hosting service. I look forward to seeing more development of this library.

Another possible option is Phel lang https://github.com/phel-lang/phel-lang. It's a lisp-family language that compiles to PHP directly inspired by Clojure and [Janet](https://janet-lang.org/). The downside is that Clojure comes with a mature body of help content, tutorials, libraries, and community for support where as Phel has only just sprouted.
