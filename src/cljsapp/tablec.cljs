(ns cljsapp.tablec 
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            ["@mui/material" :refer [Table TableBody TableCell TableContainer TableHead TableRow Paper Button Stack]]
            ["@mui/icons-material/Delete" :refer  [DeleteIcon]]
            ))
  




(defn basic-table []
  (let [results @(rf/subscribe [:results])]
    [:> TableContainer {:component Paper :sx {:margin-top "2.5em"}}
     [:h3 {:class "sub-headline"} "Submission results"]
     [:> Table {:sx {:minWidth 650} :aria-label "simple table"}
      [:> TableHead
       [:> TableRow
        [:> TableCell "Index"]
        [:> TableCell {:align "right"} "Number"]
        [:> TableCell {:align "right"} "Integer"]
        [:> TableCell {:align "right"} "numberEnum"]
        [:> TableCell {:align "right"} "NumberEnumradio"]
        [:> TableCell {:align "right"} "IntegerRange"]
        [:> TableCell {:align "right"} "IntegerRangeSteps"]
        [:> TableCell {:align "right"} "Actions"]]]
      [:> TableBody
       (map-indexed 
        (fn [idx result]
          [:> TableRow
           {:key idx
            :sx #js {":&last-child td, &:last-child th" #js {:border 0}}}
           [:> TableCell {:component "th"
                          :scope "row"
                          :sx {:bgcolor "#8624e125"
                               :color "#a75bee"}}
            (inc idx)]
           [:> TableCell {:align "right"} (:number result)]
           [:> TableCell {:align "right"} (:integer result)]
           [:> TableCell {:align "right"} (:numberEnum result)]
           [:> TableCell {:align "right"} (:numberEnumRadio result)]
           [:> TableCell {:align "right"} (:integerRange result)]
           [:> TableCell {:align "right"} (:integerRangeSteps result)]
           [:> TableCell {:align "right"} 
            [:> Stack
             [:> Button
              {:variant "contained"
               :color "error"
               :on-click #(rf/dispatch [:delete-table-result idx])}
              "Delete"]]]])
        results)]]])) 

