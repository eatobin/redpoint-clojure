(ns clojure-redpoint.gift-history
  (:require [clojure.spec.alpha :as s]
            [orchestra.spec.test :as ostest]))

(s/def :unq/gift-history (s/coll-of :unq/gift-pair :kind vector?))

object GiftHistory extends DefaultJsonProtocol {
                                                def giftHistoryAddYear(giftHistory: GiftHistory, playerKey: PlayerKey): GiftHistory =
                                                giftHistory :+ GiftPair(playerKey, playerKey)

                                                def giftHistoryUpdateGiftHistory(giftHistory: GiftHistory, giftYear: GiftYear, giftPair: GiftPair): GiftHistory = {
                                                                                                                                                                   giftHistory.updated(giftYear, giftPair)
                                                                                                                                                                   }

(ostest/instrument)
