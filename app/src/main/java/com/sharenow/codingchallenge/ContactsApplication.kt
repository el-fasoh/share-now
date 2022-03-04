package com.sharenow.codingchallenge

import android.app.Application
import com.sharenow.codingchallenge.user.UserManager
import dagger.hilt.android.HiltAndroidApp

/**
 * Custom application class that performs some initializations.
 */
@HiltAndroidApp
class ContactsApplication: Application() {

    override fun onCreate() {
        super.onCreate()
    }
}