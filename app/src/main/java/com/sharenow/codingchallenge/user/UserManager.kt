package com.sharenow.codingchallenge.user

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class UserManager @Inject constructor(private val context: Context) {

    private val mutableCurrentUserAsFlow = MutableStateFlow(getCurrentUser())
    val currentUserAsFlow = mutableCurrentUserAsFlow.asStateFlow()

    /**
     * @return List of all available users.
     */
    fun getAvailableUsers(): List<String> {
        return HardcodedUsersList.allUsers
    }

    fun getCurrentUser(): String {
        return getPrefs().getString(CURRENT_USER_KEY, getAvailableUsers().first())!!
    }

    /**
     *
     * @param id New selected user ID.
     */
    fun setCurrentUser(id: String) {
        if (!getAvailableUsers().contains(id)) {
            throw IllegalArgumentException("Unknown user ID $id")
        }

        getPrefs()
            .edit()
            .putString(CURRENT_USER_KEY, id)
            .apply()

        mutableCurrentUserAsFlow.update { id }
    }

    private fun getPrefs(): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    companion object {
        const val PREFS_NAME = "UserManager_prefs"
        const val CURRENT_USER_KEY = "currentUser"
    }
}
