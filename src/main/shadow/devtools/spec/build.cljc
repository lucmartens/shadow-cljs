(ns shadow.devtools.spec.build
  (:require [clojure.spec :as s]
            [shadow.devtools.spec.browser :as s-browser]
            [shadow.devtools.spec.script :as s-script]
            [shadow.devtools.spec.library :as s-library]
            ))

(s/def ::id
  keyword?)

(s/def ::target keyword?)

(defmulti target-spec :target)

(defmethod target-spec :browser [_]
  (s/keys
    :req-un
    [::s-browser/modules]
    :opt-un
    [::public-dir
     ::public-path]
    ))

(defmethod target-spec :node-script [_]
  (s/keys
    :req-un
    [::s-script/main
     ::s-script/output-to]
    :opt-un
    [::public-dir]
    ))

(defmethod target-spec :node-library [_]
  (s/keys
    :req-un
    [::s-script/output-to
     ::s-library/exports]
    :opt-un
    [::public-dir]
    ))

(s/def ::target-fn qualified-symbol?)

(defmulti custom-spec :target-fn :default ::default)

;; FIXME: should we be strict and force a spec for custom build configs?
(defmethod custom-spec ::default [_]
  (s/spec any?))

(defmethod target-spec :custom [_]
  (s/and
    (s/keys
      :req-un
      [::target-fn])
    (s/multi-spec custom-spec :target-fn)))

(s/def ::build
  (s/and
    (s/keys
      :req-un
      [::id
       ::target])
    (s/multi-spec target-spec :target)))