(ns cljsapp.test-me
  (:require [re-frame.core :as rf]
            [reagent.dom :as rdom] 
            ["@rjsf/validator-ajv8" :default validator]
            ["@rjsf/core" :refer [withTheme]]
            ["@rjsf/mui" :refer [Theme]]
            ["@mui/material/styles" :refer [ThemeProvider createTheme]]
            ["@mui/material/CssBaseline" :default CssBaseline]
            [cljsapp.tablec :as table]))


 (def initial-form-data {:number 2.14
                         :integer 45
                         :numberEnum 1
                         :numberEnumRadio 2
                         :integerRange -13
                         :integerRangeSteps 60
                         })

  (rf/reg-event-db
   :initialize
   (fn [_ _]
     {:form-data initial-form-data
      :results []}))
  
 
  
  (rf/reg-sub
   :form-data
   (fn [db _]
     (:form-data db)))
  
   (rf/reg-sub
   :results
   (fn [db _]
     (:results db)))


 (rf/reg-event-db
 :add-table-result
 (fn [db [_ result]]
   (update db :results conj result)
   ))
  
  (rf/reg-event-db
   :delete-table-result
   (fn [db [_ index-to-delete]]
     (update db :results
             (fn [results]
               (vec (keep-indexed (fn [idx item]
                                    (when (not= idx index-to-delete)
                                      item))
                                  results))))))


  
  (def CustomForm (withTheme Theme))
  
  (def customTheme
    (createTheme
     (clj->js
      {:palette
       {:mode "dark"
        :primary         {:main "#8624e1"}
        :grey            {:main "#2b2828"}
        :arm-vessel-blue {:main "#00b0f0"
                          :contrastText "#000"}}
       
       :components
       {:MuiGrid
        {:styleOverrides
         {:root
          {:width "100%"}}
         }}})))

  (def ui-schema
    {:integer {:ui:widget "updown"}
     :numberEnumRadio {:ui:widget "radio"
                       :ui:options {:inline true}}
     :integerRange {:ui:widget "range"}
     :integerRangeSteps {:ui:widget "range"}
     })

 
(def numbers
  {:title "Number fields & widgets"
   :type "object"
   :properties
   {:number {:title "Number"
             :type "number"},
    :integer {:title "Integer"
              :type "integer"},
    :numberEnum {:type "number"
                 :title "Number enum"
                 :enum [1, 2, 3]},
    :numberEnumRadio {:type "number"
                      :title "Number enum"
                      :enum [1, 2, 3]}
    :integerRange {:title "Integer range"
                   :type "integer"
                   :minimum -50
                   :maximum 70},
    :integerRangeSteps {:title "Integer range (by 10)"
                        :type "integer"
                        :minimum 50
                        :maximum 100
                        :multipleOf 10}}})
  
(defn numbers-component []
  (let [form-data @(rf/subscribe [:form-data])]
    (fn []
      [:> CustomForm
       {:schema numbers
        :uiSchema ui-schema
        :formData form-data 
        :validator validator
        :onSubmit (fn [e]
                         (let [form-data (js->clj (.-formData e) :keywordize-keys true)]
                           (rf/dispatch [:add-table-result form-data])))}])))


;; Testing results (the first test)
;; (defn result-table []
;;   (let [results @(rf/subscribe [:results])]
;;     (.log js/console "Passing the results check" results)
    
;;     [:table
     
;;      [:tbody
;;       (for [result results]
;;         (let [unique-id (gensym)]
;;         [:tr {:key unique-id}
;;          [:td {:class "margin:1em; color:yellow;"} (:number result)]
;;          [:td {:class "margin:1em; color:yellow;"} (:integer result)]
;;          [:td {:class "margin:1em; color:yellow;"} (:numberEnum result)]
;;          [:td {:class "margin:1em; color:yellow;"} (:integerRange result)]
;;          [:td {:class "margin:1em; color:yellow;"} (:integerRangeSteps result)]
;;          [:td {:class "margin:1em; color:yellow;"} (:numberEnumRadio result)]
        
;;          ]))
      
;;       ]]))
  
  (defn counter-component []
    [:> ThemeProvider {:theme customTheme}
     [:> CssBaseline]
     [:div {:class "container-wrapper"}
      [:div {:class "headline-box"}
       [:h1 {:class "headline"} "My-JSON-Schema Example"]]
      [:div [numbers-component]
      ;;  [result-table]
       [table/basic-table]]]])

(defn ^:export init []
  (rf/dispatch-sync [:initialize])
  (rdom/render [counter-component] (.getElementById js/document "app")))