(ns app.core)

(defn drip
  [{:keys [width]}]
  (doto (.createElement js/document "div")
        (.setAttribute "class" "drip")
        (.setAttribute "style" (str "width:" width "px;height:" (+ 5 (rand-int 10)) "px"))))

(defn animate-drips
  []
  (let [drips (js->clj (.from js/Array (.querySelectorAll js/document ".drip")))]
    (doseq [drip drips]
      (set! (.-height (.-style drip)) (str (+ (rand-int 100) 30) "px")))))

(defn init
  []
  ; (.appendChild (.-body js/document) (drip {:width 5}))
  (js/setTimeout animate-drips 1000))
