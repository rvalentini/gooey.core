(ns ^:figwheel-hooks gooey.core
  (:require
   [goog.dom :as gdom]
   [reagent.core :as reagent]))


(defn blur-filter []
[:svg {:xmlns "http://www.w3.org/2000/svg" :version "1.1"}
 [:defs
  [:filter {:id "blur"}
   [:feGaussianBlur {:in "SourceGraphic" :stdDeviation "7" :result "shadow"}]
   [:feOffset {:in "shadow" :dx "3" :dy "4" :result "shadow"}]
   [:feColorMatrix {:in "shadow" :type "matrix" :values "0 0 0 0 0  0 0 0 0 0  0 0 0 0 0  0 0 0 0.6 0" :result "shadow"}]
   [:feBlend {:in "SourceGraphic" :in2 "shadow"}]
   ]]])


(defn goo-filter []
  [:svg {:xmlns "http://www.w3.org/2000/svg" :version "1.1"}
   [:defs
    [:filter {:id "goo"}
      [:feGaussianBlur {:in "SourceGraphic" :stdDeviation "10" :result "blur"}]
      [:feColorMatrix {:in "blur" :type "matrix" :values "1 0 0 0 0  0 1 0 0 0  0 0 1 0 0  0 0 0 19 -9" :result "goo"}]
      [:feComposite {:in "SourceGraphic" :in2 "goo" :operator "atop"}]]]])


;; define your app data so that it doesn't get over-written on reload

(defn get-app-element []
  (gdom/getElement "app"))

(defn hello-world []
  [:div
   [:div {:id "container"}
    [:div {:class "square"}]
    [:div {:class "square"}]]
    ;[:div {:class "square"}]
   (blur-filter)
   (goo-filter)
   ])

(defn mount [el]
  (reagent/render-component [hello-world] el))

(defn mount-app-element []
  (when-let [el (get-app-element)]
    (mount el)))

;; conditionally start your application based on the presence of an "app" element
;; this is particularly helpful for testing this ns without launching the app
(mount-app-element)

;; specify reload hook with ^;after-load metadata
(defn ^:after-load on-reload []
  (mount-app-element)
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)
