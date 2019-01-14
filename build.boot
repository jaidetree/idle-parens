(set-env!
  :source-paths #{"src"}
  :resource-paths #{"resources"}
  :dependencies '[[perun "0.4.2-SNAPSHOT"]
                  [hiccup "1.0.5" :exclusions [org.clojure/clojure]]
                  [pandeiro/boot-http "0.6.3-SNAPSHOT"]])

(require '[boot.core :as boot]
         '[clojure.string :as str]
         '[io.perun :as perun]
         '[pandeiro.boot-http :refer [serve]])

(deftask clean
  [])

(deftask build
  "Build test blog. This task is just for testing different plugins together."
  []
  (comp
        (perun/global-metadata)
        (perun/markdown :md-exts {:all true})
        (perun/draft)
        (perun/print-meta)
        (perun/slug)
        (perun/ttr)
        (perun/word-count)
        (perun/build-date)
        (perun/gravatar :source-key :author-email :target-key :author-gravatar)
        (perun/render :renderer 'eccentric-j.site.post/render)
        (perun/collection :renderer 'eccentric-j.site.index/render :page "index.html")
        (perun/tags :renderer 'eccentric-j.site.tags/render)
        (perun/paginate :renderer 'eccentric-j.site.paginate/render)
        ; (perun/assortment :renderer 'eccentric-j.site.assortment/render
        ;                   :grouper (fn [entries]
        ;                              (->> entries
        ;                                   (mapcat (fn [entry]
        ;                                             (if-let [kws (:keywords entry)]
        ;                                               (map #(-> [% entry]) (str/split kws #"\s*,\s*"))
        ;                                               [])))
        ;                                   (reduce (fn [result [kw entry]]
        ;                                             (let [path (str kw ".html")]
        ;                                               (-> result
        ;                                                   (update-in [path :entries] conj entry)
        ;                                                   (assoc-in [path :entry :keyword] kw))))
        ;                                           {}))))
        (perun/static :renderer 'eccentric-j.site.about/render :page "about.html")
        (perun/inject-scripts :scripts #{"start.js"})
        (perun/sitemap)
        (perun/rss :description "Hashobject blog")
        (perun/atom-feed)
        (target :no-clean true)
        (notify)))

(deftask dev
  []
  (comp (watch)
        (build)
        (serve :resource-root "public" :port 9000)))
