(ns cljsapp.tablec 
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            ["@mui/material" :refer [Table TableBody TableCell TableContainer TableHead TableRow Paper]]))
  

;;   original react table static data
;; ---------------------------------------
;;   (defn createData [name calories fat carbs protein]
;;     {:name name
;;      :calories calories
;;      :fat fat
;;      :carbs carbs
;;      :protein protein})
  
;;   (def rows
;;     [(createData "Frozen yoghurt" 159 6.0 24 4.0)
;;      (createData "Ice cream sandwich" 237 9.0 37 4.3)
;;      (createData "Eclair" 262 16.0 24 6.0)
;;      (createData "Cupcake" 305 3.7 67 4.3)
;;      (createData "Gingerbread" 356 16.0 49 3.9)
;;      (createData "Fanapoli" 12 16.0 12 12.9)])
  
  (defn basic-table []
    (let [results @(rf/subscribe [:results])]
      
    [:> TableContainer {:component Paper :sx {:margin-top "2.5em"}} ; Use Paper as the component
     [:h3 {:class "sub-headline"} "Submission results"]
     [:> Table {:sx {:minWidth 650} :aria-label "simple table"}
      [:> TableHead
       [:> TableRow
        [:> TableCell "Unique-ID"]
        [:> TableCell {:align "right"} "Number"]
        [:> TableCell {:align "right"} "Integer"] ; Non-breaking space
        [:> TableCell {:align "right"} "numberEnum"]
        [:> TableCell {:align "right"} "NumberEnumradio"]
        [:> TableCell {:align "right"} "IntegerRange"]
        [:> TableCell {:align "right"} "IntegerRangeSteps"]]]
      [:> TableBody
       (for [result results]
         (let [unique-id (gensym)]
           [:> TableRow
            {:key unique-id
             :sx #js {":&last-child td, &:last-child th" #js {:border 0}}}
            [:> TableCell {:component "th" 
                           :scope "row" 
                           :sx {:bgcolor "#8624e125"  
                                 :color "#a75bee"}}   
             unique-id]
            [:> TableCell {:align "right"} (:number result)]
            [:> TableCell {:align "right"} (:integer result)]
            [:> TableCell {:align "right"} (:numberEnum result)]
            [:> TableCell {:align "right"} (:numberEnumRadio result)]
            [:> TableCell {:align "right"} (:integerRange result)]
            [:> TableCell {:align "right"} (:integerRangeSteps result)]
             ]
            ))]]]))

