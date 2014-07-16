(ns {{name}}.test.handler
  (:use midje.sweet)
  (:require [{{name}}.handler :refer :all]
            [ring.mock.request :as mock]))

(facts "Testing basic compojure app"
  (fact "main route"
        (let [response (app (mock/request :get "/"))]
          (:status response) => 200
          (:body response) => "<!DOCTYPE html>\n<html><head><title>Welcome to {{name}}</title><link href=\"/css/screen.css\" rel=\"stylesheet\" type=\"text/css\"></head><body><h1>Hello World!</h1></body></html>"))
  (fact "not-found route"
        (let [response (app (mock/request :get "/invalid"))]
          (:status response) => 404)))
