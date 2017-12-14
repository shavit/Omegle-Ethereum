(ns omegle.events
  (:require [re-frame.core :refer [reg-event-db reg-event-fx inject-cofx
              path trim-v after debug]]
            [omegle.db :refer [default-db omegle->local-store]]
            [cljs.spec.alpha :as spec]))
