---
type: post
title: "Clojure like it's PHP: Use Clojure on Shared Web Hosts"
description: "Run Clojure even on the cheapest shared web hosting using Babashka"
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


A few days back in the Doom Emacs Discord, I daydreamed about a Clojure implementation that compiles to PHP. I felt nostalgic for the days of writing code and uploading it to the server well before containerization, continuous integration, and complex build scripts became the norm. Clojure, much like working with Ruby or Python, are slightly different beasts that require more powerful hosting capabilities and runtimes to support web apps. Deploying and running those web servers cost more time, money, and often both. Thankfully, Taco and Rushsteve responded and pointed out that Clojure could serve requests as a cgi-bin script running through Babashka. Sounding almost too good to be true, I had to give this a shot!

## It's Alive!

After a bit of trial and error plus some help from Didibus in the Clojurian's Slack, I got a cgi-script running a Clojure file through Babashka! A few refinements and a sample db later: https://cgi.eccentric-j.com/metal.clj. This article breaks down the process to share what I learned and answer some predicted questions you may have.

<blockquote class="disclaimer">

  **Disclaimer**: The credit for  this idea goes to Taco ([Elais Player](https://elais.codes/)) and [rushsteve1](https://rushsteve1.us) from the Doom Emacs Discord. Also big thanks to Borkdude ([Michiel Borkent](https://michielborkent.nl)) for creating [Babashka](https://github.com/babashka/babashka) and providing support. Also thanks to [Didibus](https://www.rubberducking.com/) of the Clojurians' Slack for helping me figure out how to run my first test Clojure cgi script.

</blockquote>

## In a Nutshell

In general the setup is as follows:

1. User visits example.com/cgi-bin/demo.clj
2. The webserver (typically Apache) runs the demo.clj file
3. demo.clj runs through the Babashka binary
4. Headers and HTML printed to stdout form the response to the browser

![Flow Diagram of Using Clojure with Babashka for CGI-bin script requests on a shared web server](/img/blog/7-clj-cgi-flow.jpg)

## Why not just use PHP on shared servers?

I would love to come at this with a perfectly logical and objective reason why Clojure is ultimately a better choice on shared hosts via CGI-script, but I can't. What I can tell you is that I enjoy working with Clojure a lot more than PHP currently. I also feel Clojure third-party code is pretty consistent in style and design where as PHP has almost unlimited paradigms and styles to choose from resulting in wildly contrasting code. Another bonus is that as of [Babashka 0.2.8](https://github.com/babashka/babashka/blob/master/CHANGELOG.md#v028), which came out the day I gave this a shot, it now includes hiccup which makes crafting HTML feel like a velvety butter dream wrapped in brackets.

That said, this is still pretty experimental so it may take some more learning before this becomes viable for a production-grade project, but I do think as-is it offers a great opportunity to hack together useful web resources using Clojure.

## What are we going to build?

This guide explains how to build a basic page that fetches bands sorted by rank. Because I lack both taste and maturity, I've chosen a sample of some of my favorite metal bands. It should be pretty self explanatory to customize it to your liking if you find even the references unpalatable.

The purpose of this simple project is to demonstrate querying a database, installing pods, using shared libraries, and creating a properly formatted CGI-bin response to return to browsers.

[![Screenshot of the Example Project this Guide Details](/img/blog/6-clj-cgi-example.jpg)](https://cgi.eccentric-j.com/metal.clj)

https://cgi.eccentric-j.com/metal.clj [[Source on Github](https://github.com/eccentric-j/clj-cgi-example)]



## Requirements

1. A host that supports cgi-scripts
2. The Babashka static binary
3. The Babashka PostgreSQL pod static binary
4. Jars or a single uberjar of Clojure libraries
5. Create a postgres database and example table
6. A Clojure script to return an HTML response

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

Shared hosts are often the cheapest hosts with a large quantity of resources. With a single account you can host multiple sites and databases. All that's required is finding a shared web-host that offers custom cgi-scripts and perhaps PostgreSQL to follow along with this guide. A Virtual-Private-Server (VPS) could work as well but may take more configuration and more care towards security.

I am using https://bluehost.com, that said I got the account 3 years ago in 2018 so be sure the shared hosting service you select does support cgi-scripts as offerings may change over time.

#### Apache Handlers

Depending on the shared host chosen, you may have the option of configuring Apache Handlers. When configured, requesting a .clj file runs it as a cgi-script. If this option is not available, you will likely have to place your entry script in your `cgi-bin` directory. If using Bluehost, look for cPanel > Advanced > Apache Handlers.

Alternatively you can create a .htaccess file like the following in your site's directory:

```
AddHandler cgi-script .clj
```

### Download & Install Babashka Static Binary

[Babashka](https://github.com/babashka/babashka), created by Borkdude (Michiel Borkent), is the binary to run Clojure files as a script. For example, `bb -f fetch-contacts.clj`. The Babashka binary contains the Clojure Java core library, Borkdude&rsquo;s [sci](https://github.com/borkdude/sci) (Small Clojure Interpreter) library, a CLI, and some common, convenient third-party libraries all compiled into a native binary using [Graal VM](https://www.graalvm.org/). This allows Babashka to run a Clojure file fresh in < 10ms which is perfect for our webserver scripting purposes!

1. Visit the releases page https://github.com/babashka/babashka/releases
2. Download the static linux zip or whichever one that closely matches your shared hosting service. At the time of writing, I downloaded `babashka-0.2.8-linux-static-amd64.zip`.
3. Once downloaded, extract it on your local computer
4. Upload the bb binary somewhere like `~/bin/bb` on your shared host.

It's a good idea to keep the binary directly out of reach of your public_html folder so attackers can't remotely run code against it. Make sure the permissions are `755` so that it can execute from public web requests. Test it by running `bb` in a remote shell, if you get the Babashka Clojure REPL it works! If not check your PATH environment variable or try using an absolute path.

### Download & Install Babashka PostgreSQL Pod

This sample project connects to a Postgres database to fetch the ranked bands. The common Java Database Connection (JDBC) library does not support Babashka out of the box due to its dependency on the Java Virtual Machine (JVM) runtime. Fortunately, Babashka offers pods which allow programs written in different languages to communicate with Babashka scripts. When using Babashka on your local machine for instance, the pods API automatically downloads and loads the target pod. On my shared host, that functionality did not work as it was downloading and building binaries that depended on specific build tools on the server. To solve this, download the static binary of the pod and upload it to the server.

This step is similar to installing Babashka.

1. Visit https://github.com/babashka/babashka-sql-pods/releases
2. Download the latest `pod-babashka-postgresql-x.y.z-linux-static-amd64.zip`
3. Extract the zip on your local machine
4. Upload the binary to the lib directory we are going to use like `~/lib/pod-babashka-postgresl`.

Ensure it has `755` permissions so it runs publicly through the cgi-script.

### Create a postgres database with an example table

This article presumes your shared host is offering PostgreSQL databases and that you're familiar with creating them through cPanel. Consult your shared hosts documentation for instructions. If your shared host does not offer PG databases, you may be able to use a different pod.

After setting up your database, run the following SQL query:

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

Ensure the user you created has at least read access to the table. If managed through cPanel it's easy to miss that. After you create a user, add it to the db.

## Adding libraries like HoneySQL

To query the bands from the db, we could write a raw SQL query string but where is the fun in that? Clojure offers HoneySQL, a library for writing SQL queries as structured data. If using our own VM, local computer, or VPS, we could use a tool like `clj` and `deps.edn` to automatically download and install the libraries we need. Depending on your shared host, that's not going to work. Fear not though for all is not lost! We can specify libraries in a local deps.edn and use Clojure's `clj` CLI with Babashka to create an uberjar.

1. Create a `deps.edn` similar to the following:

    ```clojure
    {:paths []
    :deps {honeysql/honeysql {:mvn/version "1.0.444"}}
    :aliases {:remove-clojure {:classpath-overrides {src nil
                                                    org.clojure/clojure nil
                                                    org.clojure/spec.alpha nil
                                                    org.clojure/core.specs.alpha nil}}}}
    ```

    The `clojure` CLI can read this file and download the libraries we want to work with. The alias is important so that Babashka can filter out redundant libraries to include in the uberjar.

2. Run this Babashka command to create an uberjar:

    ```bash
    bb -cp $(clojure -Aremove-clojure -Spath) --uberjar honeysql.jar

    ```

3. Upload this file to your lib directory as suggested when uploading the PostgreSQL pod such as `~/lib/honeysql.jar`.

We are going to add files from that folder to Babashka's classpaths. This approach creates a shared folder of libraries and pods multiple projects can use. If you would prefer to keep libraries separate you could create an uberjar with all your code and libraries bundled together. I intend on making multiple projects with this technique so I'm going with the shared lib directory and creating individual jars for now.


## The Clojure Files

The first script configures our project environment, imports our code, and prints headers and the HTML body. This file may accessed directly from a browser.

I have named this file `metal.clj`.


```clojure
#!/bin/env /home1/<username>/bin/bb                   ;; (1)

(ns cgi.metal
  (:require
   [babashka.classpath :refer [add-classpath]]
   [babashka.pods :as pods]
   [clojure.java.shell :refer [sh]]
   [clojure.string :as s]
   [hiccup2.core :refer [html]]))

;; Dynamic Libs
(def LIB-PATH "/home1/<username>/lib/")
(def CWD (or (System/getenv "DOCUMENT_ROOT") (System/getenv "PWD")))

(defn lib
  "
  Create an absolute path to a jar file in sibling lib directory
  Takes a string filename like \"honeysql.jar\"
  Returns a string like \"/path/to/dir/lib/honeysql.jar\".
  "
  [path]
  (str LIB-PATH path))

;; Add jars and current directory to classpath to import library code

(add-classpath (s/join "::" [CWD                      ;; (2)
                             (lib "gaka.jar")
                             (lib "honeysql.jar")]))
(pods/load-pod (lib "pod-babashka-postgres"))

;; Require our main page code
;; Must come after updating the class path

(require
 '[metal.core :as metal])                             ;; (3)

;; CGI scripts must print headers then body

(println "Content-type:text/html\r\n")                ;; (4)
(println (str (html metal/content)))                  ;; (5)

(System/exit 0)
```

1. Be sure to replace `<username>` with your shared hosting username. Run `pwd` or `whoami` in a terminal if available for quick reference.
2. Add paths to the library jars we wish to use that we uploaded to the `lib` directory outside of `public_html`. Replace `<username>` with your actual username from step 1.
3. With the classpaths updated, import the adjacent source files for our app logic.
4. CGI scripts must print the headers before the body response. If it's not formatted correctly visiting the page will render a 500 server error response.

Now lets create a source file that actually renders some content! For this project I've named it `metal/core.clj` because it's both a common Clojure idiom, and a sub-genre of metal music.


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
    ]]])
```

Lastly, lets create a `metal/style.clj` with a rems helper function.

```clojure
(ns metal.style)

(defn rems
  [px]
  (str (float (/ px 16)) "rem"))
```

## Testing & Debugging

When everything works, it just works. Visit the `https://yourhost.domain/metal.clj` script and the page should render. If there is even a slight error you are likely going to get a generic 500 error response. Your host might be logging these around your home folder, or you may find a logging page in the hosting cPanel. This will be your first goto to figuring out what went wrong. The most common error you will likely run into is not setting the permission of your public Clojure scripts to `755`. Fix that by running a bash command like `chmod +x metal.clj` on the server.

If logging is not revealing the problem, another way to go about debugging is to run the script through the shell on your host. Simply ssh into your host and run `./metal.clj`. It should output the desired HTML response. This will produce much more helpful output if a runtime error occurs. Be aware though that different environment variables change depending on if the script is running through the cgi-bin from the browser request or locally from a terminal.

If you recall, we are setting the CWD conditionally:

```clojure
(def CWD (or (System/getenv "DOCUMENT_ROOT") (System/getenv "PWD")))
```

The script supports running it as a cgi-script and locally within a shell.

## Tradeoffs

- Installing other libraries is not as streamlined as with the JVM or ClojureScript, you will likely need to manually uberjar and upload them.
- Some JVM specific libraries may not work with Babashka which then requires pods to provide separate CLIs Babashka can communicate with.
- This is not the most performant option in the world, it's pretty quick, especially compared to typical JVM startup times but there are few more layers in play here, but about the same as a typical PHP setup. Additionally shared hosting is not the fastest route anyway.


## Predicted Questions and Answers

### Do I have to use the full path to the bb binary in the shebang?

Yes. When running the script through the cgi it is not running through a shell user's profile which has the PATH configured to access the `bb` binary. There may be a way to make the entrypoint script generic though. While I chose to use the Clojure file as the page a user can access from the web, you could instead write a generic babashka or bash script that calculates paths from the current directory, env var `DOCUMENT_ROOT`, and dynamically loads the file or runs it as a subordinate shell process. This felt like a rabbit hole to me though. If a pattern emerges after creating more sites, I'll likely refactor that.

### A Pod does not have a static binary

The short answer is to build the static binary yourself on your local machine. The long answer is you can use the Dockerfile and scripts in https://github.com/eccentric-j/clj-cgi-example to build the static binary.

Also please consider making a pull-request (PR) to create a static binary automatically on each release. For an example of how to do that check out my PR for the babashka-sql-pods https://github.com/babashka/babashka-sql-pods/pull/21 for an example. Just remember to update the generate_circleci.clj file and not the .circle/config.yml file like I did. :sweat_smile:

### Is there a faster way to create a separate JAR for each file I need?

In my github repo for the example project, there is a script for bundling a library as a jar and moving it into the current directory. Run it on your local machine and make sure you installed Babashka on your local as well.

1. Clone my example repo from https://github.com/eccentric-j/clj-cgi-example
2. Enter into the directory `cd clj-cgi-example`
3. Run the depjar script `./scripts/depjar.clj honeysql/honeysql 1.0.444 honeysql.jar`
4. Upload the newly created `honeysql.jar` to your server and you are good to go.

### How do I create config files that I can load in my Clojure scripts but not expose to the web?

There are many ways to solve this problem. For now I went with having a `prod.secret.edn` with my db credentials in a simple hash-map. Make sure to set the permissions to `600` so it's only readable and writable by the user. If an attacker attempts to visit this file it will return a 403 Forbidden error. Please keep in mind I'm by no means a security expert so you should do your own testing to pick the right solution for you.

### Can I use the Apache mod_rewrite to create friendly URLS?

Yes! Create a `.htaccess` similar to the following:

```
<IfModule mod_rewrite.c>
RewriteEngine On
RewriteBase "/"
RewriteCond "%{REQUEST_URI}" "!^/img"
RewriteRule ".*" "metal.clj" [L,PT]
</IfModule>
AddHandler cgi-script .clj
```

And any requests to anything other than images will rewrite to the metal.clj script. For instance visiting `https://cgi.eccentric-j.com` gets rerouted to `metal.clj`.

### When should I use a pod vs. uberjar ?

Uberjar is best when dealing with Babashka friendly Clojure libraries. Pods are CLIs that act as a service exchanging methods with Babashka which support different languages provided the API offers a certain format.

### Where can I learn more about Babashka?

Check out https://book.babashka.org for documentation around Babashka such as arguments and available APIs.

## Alternatives

One alternative I considered but have not explored is using Graal VM to compile a static binary that contains the entire web app and supporting libraries. The downside of this approach is that if any file changes, you will need to rebuild the binary and upload it to the server. Then you can access it directly via cgi-bin.

Github user alekcz ([Alexander Oloo](https://alexanderoloo.com/)) has begun drafting [PCP](https://github.com/alekcz/pcp): A Clojure processor that runs a SCGI server that can act as a replacement for a long-running php-fpm process. This may not be best suited for a shared hosting service but would definitely be good for a VPS hosting service. I look forward to seeing it progress!

Another possible option is Phel lang https://github.com/phel-lang/phel-lang. It's a lisp-family language that compiles to PHP directly inspired by Clojure and [Janet](https://janet-lang.org/). The downside is that Clojure comes with a mature body of help content, tutorials, libraries, and community for support where as Phel has only just sprouted.
