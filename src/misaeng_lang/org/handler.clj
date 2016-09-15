(ns misaeng-lang.org.handler
  (:require [미생.기본 :refer :all]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [hiccup.page :refer [html5 include-css include-js]]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]))

(defn layout
  "HTML 기본 레이아웃"
  [& contents]
  (html5 [:head
          [:meta {:charset "utf-8"}]
          [:meta {:name "viewport" :content "width=device-width, initial-scale=1"}]
          [:title "미생 - 실험적 한글 프로그래밍 언어"]
          (map include-css
               ["https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"
                "https://maxcdn.bootstrapcdn.com/font-awesome/4.6.3/css/font-awesome.min.css"
                "http://cdnjs.cloudflare.com/ajax/libs/highlight.js/9.6.0/styles/default.min.css"
                "css/misaeng.css"])]
         (-> [:body]
             (into contents)
             (into (map include-js ["https://code.jquery.com/jquery-3.1.0.min.js"
                                    "https://d3js.org/d3.v4.min.js"
                                    "http://cdnjs.cloudflare.com/ajax/libs/highlight.js/9.6.0/highlight.min.js"
                                    "js/misang.js"])))))

(defn page
  [& contents]
  (layout [:nav [:div "페이지 헤더"]]
          (into [:main] contents)
          [:footer "Copyrights 2016 Daehyun Kim"]))

(defroutes app-routes
  (GET "/" [] (page "Hello World"))
  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes site-defaults))
