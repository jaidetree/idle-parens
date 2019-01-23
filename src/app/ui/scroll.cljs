(ns app.ui.scroll
  (:require [clojure.string :refer [join split]]
            [app.util :refer [log query-all]]))

(defn scrollable-elements
  []
  (query-all ".scrolling"))

(defn window->rect
  [win]
  (let [doc (.-documentElement (.-document win))
        width (.-innerWidth win)
        height (.-innerHeight win)
        top (.-scrollTop doc)
        bottom (+ top height)]
    {:width width
     :height height
     :top top
     :bottom bottom}))

(defn el->rect
  [el]
  (let [client (.getBoundingClientRect el)]
    {:top (.-top client)
     :bottom (.-bottom client)}))

(defn el-visible?
  [frame el]
  (let [el-rect (el->rect el)]
    (and (<= (:top el-rect) (:height frame))
         (>= (:bottom el-rect) 0))))

(defn create-update
  [frame el]
  {:visible? (el-visible? frame el)
   :el el})

(defn split-classes
  [el]
  (set (split (.-className el) #" ")))

(defn update-classes
  [visible? classes]
  (cond visible?                    (conj classes "within-view")
        (contains? classes "reset") (remove #{"within-view"} classes)
        :else                       classes))

(defn apply-scroll-updates
  [scroll-updates]
  (doseq [{:keys [el visible?]} scroll-updates]
    (let [classes (update-classes visible? (split-classes el))]
      (set! (.-className el) (join " " classes)))))

(defn update-scroll-elements!
  "Looks for a list of elements with .scrolling and adds .within-view if
  el is partially visible. If .reset is present will remove .within-view when
  scrolled out of view."
  [& _]
  (let [frame (window->rect js/window)]
    (->> (scrollable-elements)
         (map #(create-update frame %))
         (apply-scroll-updates))))
