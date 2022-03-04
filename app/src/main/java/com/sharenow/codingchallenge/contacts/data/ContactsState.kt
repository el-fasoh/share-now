@file:DoNotAlter

package com.sharenow.codingchallenge.contacts.data

import com.sharenow.codingchallenge.challenge.DoNotAlter

/**
 * View state for contacts page.
 */
sealed interface ContactsState {

    val availableUsers: List<String>

    val selectedUser: String

    /**
     * Data is available.
     */
    data class Available(
        val contacts: List<Contact>,
        val allContactsCount: Int,
        override val availableUsers: List<String>,
        override val selectedUser: String
    ): ContactsState

    /**
     * Data is not available.
     */
    data class Error(
        override val availableUsers: List<String>,
        override val selectedUser: String
    ): ContactsState

}