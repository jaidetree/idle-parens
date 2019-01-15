(ns app.core)

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
  (let [drips (js->clj (.from js/Array (.querySelectorAll js/document ".drip")))]
    (doseq [drip drips]
      (let [max-height (or (parse-max-height drip) 100)
            min-height (calc-min-height max-height)]
        (set! (.-height (.-style drip)) (str (+ (rand-int max-height) min-height) "px"))))))

(defn init
  []
  ; (.appendChild (.-body js/document) (drip {:width 5}))
  (js/setTimeout animate-drips 1000))
