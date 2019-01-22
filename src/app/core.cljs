(ns app.core
  (:require [goog.events :as events]
            [clojure.string :refer [join split]]))

(defn query-all
  [selector]
  (->> selector
       (.querySelectorAll js/document)
       (.from js/Array)
       (js->clj)))

(defn scrollable-elements
  []
  (query-all ".scrolling"))

(defn log
  [& args]
  (apply println args))

(defn window-rect
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

(defn el-client-rect
  [el]
  (let [client (.getBoundingClientRect el)]
    {:top (.-top client)
     :bottom (.-bottom client)}))

(defn el-visible?
  [frame el]
  (let [el-rect (el-client-rect el)]
    (and (<= (:top el-rect) (:height frame))
         (>= (:bottom el-rect) 0))))

(defn update-visibility
  [frame el]
  {:visible? (el-visible? frame el)
   :el el})

(defn classes->set
  [el]
  (set (split (.-className el) #" ")))

(defn update-scroll-classes
  [visible? classes]
  (cond visible?                    (conj classes "within-view")
        (contains? classes "reset") (remove #{"within-view"} classes)
        :else                       classes))

(defn update-scrolling-status
  [scroll-updates]
  (doseq [{:keys [el visible?]} scroll-updates]
    (let [classes (update-scroll-classes visible? (classes->set el))]
      (set! (.-className el)
            (join " " classes)))))

(defn update-scroll-elements
  [& _]
  (let [frame (window-rect js/window)]
    (->> (scrollable-elements)
         (map #(update-visibility frame %))
         (update-scrolling-status))))

(events/listen js/window "scroll" update-scroll-elements)
(events/listenOnce js/window "DOMContentLoaded" update-scroll-elements)

(defn drip
  [{:keys [width]}]
  (doto (.createElement js/document "div")
        (.setAttribute "class" "drip")
        (.setAttribute "style" (str "width:" width "px;height:" (+ 5 (rand-int 10)) "px"))))

(defn parse-max-height
  [el]
  (let [max-height (.-maxHeight (.-style el))]
   (when (not-empty max-height)
      (->> max-height
           (re-find #"\d+")
           (float)
           (* 16)))))

(defn calc-min-height
  [max-height]
  (int (* 0.3 max-height)))

(defn animate-drips
  []
  (let [drips (query-all ".drip")]
    (doseq [drip drips]
      (let [max-height (or (parse-max-height drip) 100)
            min-height (calc-min-height max-height)]
        (set! (.-height (.-style drip)) (str (+ (rand-int max-height) min-height) "px"))))))

(defn init
  []
  ; (.appendChild (.-body js/document) (drip {:width 5}))
  (js/setTimeout animate-drips 1000))
