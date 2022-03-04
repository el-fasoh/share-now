@file:DoNotAlter

package com.sharenow.codingchallenge.contacts.domain.impl

import android.util.Log
import com.sharenow.codingchallenge.challenge.DoNotAlter
import com.sharenow.codingchallenge.contacts.data.Contact
import com.sharenow.codingchallenge.contacts.domain.ContactsApi
import javax.inject.Inject
import kotlin.random.Random

/**
 * Fake implementation of service.
 *
 * The service is a bit faulty and may sometimes take longer to return the results, results may not be ideal,
 * and it may error out sometimes.
 */
class FakeContactsService @Inject constructor(): ContactsApi {

    override fun getContacts(userId: String): Collection<Contact> {
        Log.i(TAG, "Loading contacts for user ID $userId")

        val rollTheDice = Random.nextInt(MAX_RNG_VALUE + 1)

        Thread.sleep(Random.nextLong(MAX_WAITING_TIME_MS))

        return when (rollTheDice) {
            in FAULT_INTERVAL -> throw ServiceError()

            in BAD_DATA_INTERVAL -> {
                val originalCollection = (USER_CONTACTS[userId] ?: emptySet())
                val entryToDuplicate = originalCollection.randomOrNull()

                if (entryToDuplicate != null) {
                    originalCollection + generateSequence {
                        Contact(
                            entryToDuplicate.id,
                            entryToDuplicate.name,
                            entryToDuplicate.email
                        )
                    }
                        .take(1 + Random.nextInt(MAX_BAD_DATA_COUNT))
                        .toList()
                } else {
                    originalCollection
                }
            }

            else -> USER_CONTACTS[userId] ?: emptySet()
        }
    }

    private class ServiceError: RuntimeException("Some error occurred in the service")

    private companion object {

        private const val TAG = "ContactsService"

        private const val MAX_WAITING_TIME_MS = 2000L

        private val FAULT_INTERVAL = 0 until 20
        private val BAD_DATA_INTERVAL = 20 until 60

        private const val MAX_RNG_VALUE = 100

        private const val MAX_BAD_DATA_COUNT = 4

        private val USER_CONTACTS = buildMap {
            this["user1"] = setOf(
                Contact(1, "Jeremiah Gottwald", "orange@gov.uk"),
                Contact(2, "Francis Crozier", "erebus@hmsterror.com"),
                Contact(3, "Cloud Strife", "cldstr777@shinra.co.jp"),
                Contact(4, "Austin Powers", "yeah@baby.yeah")
            )

            this["user2"] = setOf(
                Contact(102, "Joseph Joestar", "gentleman@joestars.uk"),
                Contact(101, "Benjamin Hanscom", "haystack@derry.com"),
                Contact(523, "Thomas Angelo", "tom.angelo1900@lostheaven.com"),
                Contact(777, "Boris Yurinov", "thebulletdodger@mail.com")
            )
        }

    }

}