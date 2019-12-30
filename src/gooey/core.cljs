(ns ^:figwheel-hooks gooey.core
  (:require
   [goog.dom :as gdom]
   [reagent.core :as reagent]
   [hipo.core :as hipo]))


(def spread-direction ["right" "left" "top" "bottom" "left-top" "right-top" "left-bottom" "right-bottom"])

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


(defn rand-position []
  (-> (rand-int 100)
      (max 10)
      (min 90)))


(defn pixel-to-percent [top left]
  (let [height (.-innerHeight js/window)
        width (.-innerWidth js/window)
        height-percent (str (* 100 (/ (+ top 50) height)) "%")
        width-percent (str (* 100 (/ (+ left 50) width)) "%")]
    [height-percent width-percent]))


(defn is-out-of-bounds? [spread-direction top left]
  (let [height (.-innerHeight js/window)
        width (.-innerWidth js/window)
        height-percent (* 100 (/ (+ top 50) height))
        width-percent (* 100 (/ (+ left 50) width))]
    (case spread-direction
      "right" (> width-percent 90)
      "left" (< width-percent 10)
      "top" (< height-percent 10)
      "bottom" (> height-percent 90)
      "left-top" (or (< width-percent 10) (< height-percent 10))
      "right-top" (or (> width-percent 90) (< height-percent 10))
      "left-bottom" (or (< width-percent 10) (> height-percent 90))
      "right-bottom" (or (> width-percent 90) (> height-percent 90))
      :unknown)))


(defn random-spread-animation [top left]
  (->> spread-direction
       (filter #(not (is-out-of-bounds? % top left)))
       (rand-nth)
       (str "spread-")))


(defn add-circle []
  (let [container (.getElementById js/document "container")
        circles (.-childNodes container)]
    (.forEach circles (fn [div]
                        (let [circle (.-firstChild div)
                              left (.-left (.getBoundingClientRect circle))
                              top (.-top (.getBoundingClientRect circle))
                              [top-percent left-percent] (pixel-to-percent top left)
                              random-animation (random-spread-animation top left)
                              random-delay (str (rand-int 3000) "ms")
                              new-circle (hipo/create
                                           [:div {:class "shake"}
                                            [:div {:class (str "circle " random-animation)}]])]
                          (set! (.-top (.-style new-circle)) top-percent)
                          (set! (.-left (.-style new-circle)) left-percent)
                          (set! (.-animationDelay (.-style (.-firstChild new-circle))) random-delay)
                          (set! (.-animationDelay (.-style new-circle)) random-delay)
                          (.appendChild container new-circle))))))


;; define your app data so that it doesn't get over-written on reload

(defn get-app-element []
  (gdom/getElement "app"))

(defn bubble-spread []
  [:div
   [:div {:id "container"}
    (take 1 (repeatedly #(identity [:div
                           [:div {:class "circle"
                                  :style {:left (str "50" "%")
                                          :top  (str "50" "%")}}]])))]
   (blur-filter)
   (goo-filter)])

(defn mount [el]
  (reagent/render-component [bubble-spread] el))

(defn mount-app-element []
  (when-let [el (get-app-element)]
    (mount el)))

;; conditionally start your application based on the presence of an "app" element
;; this is particularly helpful for testing this ns without launching the app
(mount-app-element)

(.setInterval js/window add-circle 3200)


;; specify reload hook with ^;after-load metadata
(defn ^:after-load on-reload []
  (mount-app-element)
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)
