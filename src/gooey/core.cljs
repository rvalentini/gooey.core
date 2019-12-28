(ns ^:figwheel-hooks gooey.core
  (:require
   [goog.dom :as gdom]
   [reagent.core :as reagent]
   [hipo.core :as hipo]))


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


(defn add-circle []
  (.log js/console "I GOT CALLED!")
  (let [container (.getElementById js/document "container")
        circles (.-childNodes container)
        new-circle (hipo/create [:div {:class "circle"
                                :style {:left "40%"
                                        :top  "40%"}}])]

    (set! (.-top (.-style new-circle)) (str (rand-position) "%"))
    (set! (.-left (.-style new-circle)) (str (rand-position) "%"))
    (.appendChild container new-circle)

    (.forEach circles (fn [circle]
                        (let [left (.-left (.getBoundingClientRect circle))
                              top (.-top (.getBoundingClientRect circle))]
                          #_(.appendChild container (hipo/create [:div {:class "circle"
                                                                      :style {:left left
                                                                              :top  top}}]))))))


  )


;; define your app data so that it doesn't get over-written on reload

(defn get-app-element []
  (gdom/getElement "app"))

(defn hello-world []
  [:div
   [:div {:id "container"}
    [:div {:class "circle" :style {:left "60%"
                                   :top  "60%"}}]
    #_[:div {:class "circle"}]]
   ;[:div {:class "circle"}]
   (blur-filter)
   (goo-filter)]


  )

(defn mount [el]
  (reagent/render-component [hello-world] el))

(defn mount-app-element []
  (when-let [el (get-app-element)]
    (mount el)))

;; conditionally start your application based on the presence of an "app" element
;; this is particularly helpful for testing this ns without launching the app
(mount-app-element)

(.setInterval js/window add-circle 2000)


;; specify reload hook with ^;after-load metadata
(defn ^:after-load on-reload []
  (mount-app-element)
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)
