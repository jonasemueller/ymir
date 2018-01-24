(ns example.ymir.alpha.scroll)

;; Copied from https://gist.github.com/jasich/21ab25db923e85e1252bed13cf65f0d8
;; Copyright Jason Sich

(defn cur-doc-top []
  (+ (.. js/document -body -scrollTop) (.. js/document -documentElement -scrollTop)))

(defn element-top [elem top]
  (if (.-offsetParent elem)
    (let [client-top (or (.-clientTop elem) 0)
          offset-top (.-offsetTop elem)]
      (+ top client-top offset-top (element-top (.-offsetParent elem) top)))
    top))

(defn to-id
  ([elem-id]
   (to-id elem-id 500 15))
  ([elem-id speed moving-frequency]
   (let [elem (.getElementById js/document elem-id)
         hop-count (/ speed moving-frequency)
         doc-top (cur-doc-top)
         gap (/ (- (element-top elem 0) doc-top) hop-count)]
     (doseq [i (range 1 (inc hop-count))]
       (let [hop-top-pos (* gap i)
             move-to (+ hop-top-pos doc-top)
             timeout (* moving-frequency i)]
         (.setTimeout js/window (fn []
                                  (.scrollTo js/window 0 move-to))
                      timeout))))))

(defn jump-to-top []
  (.scrollTo js/window 0 0))
