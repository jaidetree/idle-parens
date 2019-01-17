(set-env!
  :source-paths #{"src"}
  :resource-paths #{"resources"}
  :dependencies '[[perun "0.4.2-SNAPSHOT"]
                  [hiccup "1.0.5" :exclusions [org.clojure/clojure]]
                  [org.clojure/clojurescript "1.10.439"]
                  [pandeiro/boot-http "0.6.3-SNAPSHOT"]
                  [deraen/boot-livereload "0.2.1"]
                  [deraen/boot-sass "0.3.1"]
                  [adzerk/boot-cljs "2.1.5" :scope "test"]])

(require '[boot.core :as boot]
         '[clojure.string :as str]
         '[clojure.java.io :as io]
         '[io.perun :as perun]
         '[io.perun.core :refer [report-info]]
         '[pandeiro.boot-http :refer [serve]]
         '[deraen.boot-livereload :refer [livereload]]
         '[deraen.boot-sass :refer [sass]]
         '[adzerk.boot-cljs :refer [cljs]])

(defn match-any
  [patterns s]
  (some #(re-find % s) patterns))

(defn matcher
  [regexps default]
  (if (some? regexps)
    #(match-any regexps (str %))
    (constantly default)))

(defn pipeline
  [& steps]
  (->> steps
       (keep identity)
       (apply comp)))

(defn delete-files!
  "Recursively deletes all files in a folder
  Takes a included predicate, an excluded predicate, and an io/file dir.
  Returns nil."
  [included excluded dir]
  (let [files (->> dir
                   (file-seq)
                   (drop 1)
                   (remove excluded)
                   (filter included))]
    (doseq [file files]
      (when (.isDirectory file)
        (delete-files! included excluded file))
      (.delete file))))

(deftask clean
  [d dir     DIR      str         "Directory to clean defaults to target/public"
   e exclude PATTERNS #{regex}    "Regexps to exclude from cleaning"
   i include PATTERNS #{regex}    "Regexps to include for cleaning"]
  (let [dir (or dir "target/public")]
    (boot/with-pass-thru _
      (delete-files! (matcher include true)
                     (matcher exclude false)
                     (io/file dir))
      (report-info "clean!" "Cleaned %s" dir))))

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
    (perun/render :renderer 'idle-parens.post/render :filterer blog?)
    (perun/collection :renderer 'idle-parens.index/render :page "index.html" :filterer blog?)
    (perun/tags :renderer 'idle-parens.tags/render :filterer blog?)
    (perun/paginate :renderer 'idle-parens.paginate/render :filterer blog? :out-dir "public/blog")))

(defn project-pages
  [prod?]
  (pipeline
    (perun/render :renderer 'idle-parens.post/render :filterer project?)
    (perun/collection :renderer 'idle-parens.index/render :page "projects.html" :filterer project?)))
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
    (static-pages prod?)))

(deftask build
  "Build the blog source and output to target/public"
  [e build-env BUILD-ENV kw    "Environment keyword like :dev or :production"]
  (let [prod? (= build-env :prod)]
   (pipeline
     (generate-site :prod? prod?)
     (seo-files prod?)
     (cljs :ids ["prod"])
     (sass :output-style :compressed :source-map false)
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
    (livereload :snippet true)
    (target :no-clean true)
    (serve :dir "target/dev" :port 9000)))
