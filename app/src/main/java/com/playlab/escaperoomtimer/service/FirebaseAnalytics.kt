package com.playlab.escaperoomtimer.service

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import com.google.firebase.Firebase

object FirebaseAnalytics {

    private var firebaseAnalytics: FirebaseAnalytics? = null

    fun getInstance(): FirebaseAnalytics {
        if (firebaseAnalytics == null) {
            firebaseAnalytics = Firebase.analytics
        }
        return firebaseAnalytics!!
    }

    fun logAnalyticsEvent(eventName: String, bundle: Bundle) {
        getInstance().logEvent(eventName, bundle)
    }
}