(ns idle-parens.formats
  (:import [java.time Instant]
           [java.time.format DateTimeFormatter]))

(defn format-date
  [inst]
  (-> (DateTimeFormatter/ofPattern "LLL d, YYYY h:m a")
      ; (.withZone (ZoneId/systemDefault))
      (.format inst)))

; DateTimeFormatter formatter =
;   DateTimeFormatter.ofPattern("yyyyMMddHHmmss").withZone(ZoneId.systemDefault());
; return formatter.format(instant);
