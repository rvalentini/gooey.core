(ns gooey.svg-filter)


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