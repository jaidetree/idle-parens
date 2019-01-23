(ns app.core
  (:require [goog.events :as events]
            [app.fx.drip :refer [animate-drips]]
            [app.ui.scroll :refer [update-scroll-elements!]]
            [app.util :refer [log query-all]]))

(defn init
  []
  (js/setTimeout animate-drips 1000)
  (events/listen js/window "scroll" update-scroll-elements!)
  (events/listenOnce js/window "DOMContentLoaded" update-scroll-elements!))
