(ns ^:figwheel-no-load omegle.dev
  (:require
    [omegle.core :as core]
    [devtools.core :as devtools]))

(devtools/install!)

(enable-console-print!)

(core/init!)
