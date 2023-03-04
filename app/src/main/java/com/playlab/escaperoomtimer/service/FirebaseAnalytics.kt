package com.playlab.escaperoomtimer.service

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.ParametersBuilder
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.google.firebase.analytics.ktx.logEvent

object FirebaseAnalytics {

    private var firebaseAnalytics: FirebaseAnalytics? = null

    fun getInstance(): FirebaseAnalytics {
        if (firebaseAnalytics == null) {
            firebaseAnalytics = Firebase.analytics
        }
        return firebaseAnalytics!!
    }

    fun logAnalyticsEvent(eventName: String, pb: ParametersBuilder.() -> Unit) {
        getInstance().logEvent(eventName, pb)
    }
}