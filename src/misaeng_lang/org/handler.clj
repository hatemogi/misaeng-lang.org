(ns misaeng-lang.org.handler
  (:require [미생.기본 :refer :all]
            [clojure.java.io :as io]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [hiccup.page :refer [html5 include-css include-js]]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]))

(함수 레이아웃
  "HTML 기본 레이아웃"
  [& contents]
  (가정 [타이틀 "미생 - 다이나믹 한글 프로그래밍 언어"
         사이트명 "미생 - 다이나믹 한글 프로그래밍 언어"
         설명 "설명"]
    (html5 [:head
            [:meta {:charset "utf-8"}]
            [:meta {:content "IE=edge" :http-equiv "X-UA-Compatible"}]
            [:meta {:name "description" :content 설명}]
            [:meta {:name "author" :content "김대현"}]
            [:meta {:name "viewport" :content "width=device-width, initial-scale=1.0, minimum-scale=0.7"}]

            [:meta {:property "og:site_name" :content 사이트명}]
            [:meta {:property "og:url" :content "http://misang-lang.org/"}]
            [:meta {:property "og:title" :content 타이틀}]
            [:meta {:property "og:locale" :content "ko_KR"}]
            [:meta {:property "og:description" :content 설명}]
            [:meta {:property "fb:app_id" :content "786513651493090"}]
            [:meta {:property "og:image" :content ""}]
            [:meta {:property "og:image:type" :content "image/png"}]
            [:meta {:property "og:image:width" :content "1200"}]
            [:meta {:property "og:image:height" :content "630"}]

            [:meta {:name "twitter:card" :content "summary"}]
            [:meta {:name "twitter:site" :content "@misaeng-lang"}]
            [:meta {:name "twitter:title" :content 타이틀}]
            [:meta {:name "twitter:image" :content ""}]
            [:meta {:name "twitter:description" :content 설명}]

            [:title 타이틀]
            (map include-css
                 ["//maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"
                  "//maxcdn.bootstrapcdn.com/font-awesome/4.6.3/css/font-awesome.min.css"
                  "//cdnjs.cloudflare.com/ajax/libs/highlight.js/9.6.0/styles/default.min.css"
                  "/묶음.css"])]
           (-> [:body]
               (into contents)
               (into (map include-js ["//code.jquery.com/jquery-3.1.0.min.js"
                                      "//d3js.org/d3.v4.min.js"
                                      "//cdnjs.cloudflare.com/ajax/libs/highlight.js/9.6.0/highlight.min.js"
                                      "/묶음.js"]))))))

(함수- 리소스-합치는-미들웨어 [핸들러 합쳐질파일 & 합칠파일목록]
  (가정 [디렉터리 "resources/public/"
         바뀜? (constantly 참) ;; 현재 둘다 10ms이내에 끝남. 비교할 필요 없음.
         합침! (fn []
                 (println 합쳐질파일 "<-" 합칠파일목록)
                 (time
                  (with-open [쓸거 (io/writer (str 디렉터리 합쳐질파일))]
                    (run! (fn [파일]
                            (with-open [읽을거 (io/reader (str 디렉터리 파일))]
                              (io/copy 읽을거 쓸거)
                              (.write 쓸거 "\n")))
                          합칠파일목록))))]
    (fn [요청]
      (참이면 (바뀜?) (합침!))
      (핸들러 요청))))

(defn 페이지
  [& contents]
  (레이아웃 [:nav [:div "페이지 헤더"]]
            (into [:main] contents)
            [:footer "Copyrights 2016 Daehyun Kim"]))

(defn 첫페이지
  [요청]
  (페이지 [:div
           [:textarea#code]]))

(defroutes app-routes
  (GET "/" [] 첫페이지)
  (route/not-found "Not Found"))

(def app
  (-> app-routes
      (리소스-합치는-미들웨어 "묶음.css"
                              "cm/lib/codemirror.css"
                              "css/미생.css")
      (리소스-합치는-미들웨어 "묶음.js"
                              "js/jquery-3.1.0.min.js"
                              "cm/lib/codemirror.js"
                              "cm/mode/clojure/clojure.js"
                              "js/미생.js")
      (wrap-defaults (assoc-in site-defaults
                               [:responses :content-types]
                               {:mime-types {"md" "text/markdown"}}))))
