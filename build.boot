(set-env!
  :source-paths #{"src"}
  :resource-paths #{"resources"}
  :dependencies '[[boot/core "2.8.2" :scope "provided"]
                  [perun "0.4.2-SNAPSHOT"]
                  [hiccup "1.0.5" :exclusions [org.clojure/clojure]]
                  [org.clojure/clojurescript "1.10.439"]
                  [pandeiro/boot-http "0.6.3-SNAPSHOT"]
                  [deraen/boot-livereload "0.2.1"]
                  [deraen/boot-sass "0.3.1"]
                  [adzerk/boot-cljs "2.1.5" :scope "test"]
                  [org.clojure/tools.namespace "0.3.0-alpha4"]])

(require '[boot.core :as boot]
         '[clojure.string :as str]
         '[io.perun :as perun]
         '[pandeiro.boot-http :refer [serve]]
         '[deraen.boot-livereload :refer [livereload]]
         '[deraen.boot-sass :refer [sass]]
         '[adzerk.boot-cljs :refer [cljs]]
         '[idle-parens.tasks.emoji :refer [emoji]]
         '[idle-parens.tasks.clean :refer [clean]])

(defn pipeline
  [& steps]
  (->> steps
       (keep identity)
       (apply comp)))

(defn html
  [prod?]
  (pipeline
    (perun/markdown :md-exts {:all true})
    (when prod? (perun/draft))))

(defn blog?
  [file]
  (re-find #"/blog/" (:path file)))

(defn project?
  [file]
  (re-find #"/projects/" (:path file)))

(defn blog-pages
  [prod?]
  (pipeline
    (perun/collection :renderer 'idle-parens.blog.index/render :page "index.html" :filterer blog?)
    (perun/render :renderer 'idle-parens.blog.post/render :filterer blog?)
    (perun/tags :renderer 'idle-parens.blog.tags/render :filterer blog? :out-dir "public/blog/tags")
    (perun/paginate :renderer 'idle-parens.blog.paginate/render :filterer blog? :out-dir "public/blog")))

(defn project-pages
  [prod?]
  (pipeline
    (perun/collection :renderer 'idle-parens.blog.index/render :page "projects.html" :filterer project?)
    (perun/render :renderer 'idle-parens.blog.post/render :filterer project?)))
    ; (perun/tags :renderer 'idle-parens.tags/render :filterer project?)))
    ; (perun/paginate :renderer 'idle-parens.paginate/render :filterer project?)))

(defn static-pages
  [prod?]
  (pipeline
    (perun/static :renderer 'idle-parens.about/render :page "about.html")))

(defn seo-files
  [prod?]
  (pipeline
    (perun/sitemap)
    (perun/rss :description "Eccentric J's Blog")
    (perun/atom-feed)))

(defn build-meta
  [prod?]
  (pipeline
    (perun/slug)
    (perun/ttr)
    (perun/word-count)
    (perun/build-date)
    (perun/gravatar :source-key :author-email :target-key :author-gravatar)))

(deftask generate-site
  [_ prod? bool "Production build?"]
  (pipeline
    (perun/global-metadata)
    (html prod?)
    (build-meta prod?)
    (blog-pages prod?)
    (project-pages prod?)
    (static-pages prod?)
    (emoji)))

(deftask build
  "Build the blog source and output to target/public"
  []
  (let [prod? true]
   (pipeline
     (generate-site :prod? prod?)
     (seo-files prod?)
     (cljs :ids ["prod"])
     (sass :output-style :compressed :source-map false)
     (sift :include [#".edn"] :invert true)
     (clean :exclude [#".git"])
     (target :no-clean true)
     (notify))))

(deftask dev
  []
  (pipeline
    (clean :dir "target/dev")
    (watch)
    (generate-site :prod? false)
    (cljs :ids ["dev"])
    (sass :output-style :expanded :source-map true)
    (sift :move {#"public" "dev"})
    (sift :include [#".edn"] :invert true)
    (livereload :snippet true)
    (target :no-clean true)
    (serve :dir "target/dev" :port 9000)))

(deftask nrepl
  []
  (set-env!
   :init-ns 'user
   :dependencies #(into % '[[proto-repl "0.3.1"]
                            [proto-repl-charts "0.3.2"]
                            [org.clojure/tools.namespace "0.2.11"]]))
  (repl))
