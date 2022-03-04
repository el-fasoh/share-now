@file:DoNotAlter

package com.sharenow.codingchallenge.contacts.domain

import com.sharenow.codingchallenge.challenge.DoNotAlter
import com.sharenow.codingchallenge.contacts.data.Contact

/**
 * API for service that provides contacts for current user.
 *
 * This is a legacy API that still uses blocking API.
 */
interface ContactsApi {

    /**
     * Get contacts from contacts service. The service is quite faulty, so there may be the following problems:
     * 1. This call may block the calling thread for a long time.
     * 2. Sometimes there may be duplicate entries in the collection you receive. ID of the contact should be used as a uniqueness qualifier.
     */
    fun getContacts(userId: String): Collection<Contact>
}