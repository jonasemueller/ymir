(ns example.ymir.alpha.routes)

(def routes
  ["/" {"" ::index
        "about" ::about
        "paradigms/intro" ::paradigms-intro
        "paradigms/questions" ::paradigms-questions
        "paradigms/questions2" ::paradigms-questions2
        "paradigms/pdf" ::paradigms-pdf
        "paradigms/result" ::paradigms-result
        "duration/calculator" ::duration-calculator
        "duration/intro" ::duration-intro
        "notfound" ::page-not-found}])

(defn urls []
  (set (keys (second routes))))

(defn ids []
  (set (vals (second routes))))
