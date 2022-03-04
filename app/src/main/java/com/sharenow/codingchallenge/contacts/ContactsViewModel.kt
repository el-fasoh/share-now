package com.sharenow.codingchallenge.contacts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sharenow.codingchallenge.contacts.data.Contact
import com.sharenow.codingchallenge.contacts.data.ContactsState
import com.sharenow.codingchallenge.contacts.domain.ContactsApi
import com.sharenow.codingchallenge.user.UserManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * View model providing state for contacts activity.
 */
@HiltViewModel
class ContactsViewModel @Inject constructor(
    private val contactsApi: ContactsApi
): ViewModel() {

    private val refreshFlow = MutableSharedFlow<Unit>(replay = 0)

    /**
     * Returns [Flow] that never completes, never errors out
     * and emits current state.
     */
    fun stateFlow(): Flow<ContactsState> {
        return combine(
            contactsFlow(),
            contactsCountFlow()
        ) { contacts, contactsCount ->
            ContactsState.Available(
                contacts = contacts,
                allContactsCount = contactsCount,
                availableUsers = UserManager.getInstance().availableUsers,
                selectedUser = UserManager.getInstance().currentUser
            ) as ContactsState
        }.catch {
            emit(ContactsState.Error(
                availableUsers = UserManager.getInstance().availableUsers,
                selectedUser = UserManager.getInstance().currentUser
            ))
        }
    }

    private fun contactsFlow(): Flow<List<Contact>> {
        return fetchTriggersFlow()
            .map { userId ->
                contactsApi.getContacts(userId).toList()
            }
    }

    private fun contactsCountFlow(): Flow<Int> {
        return contactsFlow().map { contactList ->
            contactList.size
        }
    }

    private fun fetchTriggersFlow(): Flow<String> {
        return callbackFlow {

            val callback = UserManager.OnUserChangedListener {
                trySend(it)
            }

            UserManager.getInstance().addOnUserChangeListener(callback)

            trySend(UserManager.getInstance().currentUser)

            awaitClose {
                UserManager.getInstance().removeOnUserChangeListener(callback)
            }
        }.flatMapMerge { userId ->
            refreshFlow
                .onStart {
                    emit(Unit)
                }
                .map {
                    userId
                }
        }
    }

    /**
     * Refresh was requested.
     */
    fun onRefreshRequested() {
        viewModelScope.launch { refreshFlow.emit(Unit) }
    }

    /**
     * Another user was selected.
     */
    fun onUserSelected(userId: String) {
        UserManager.getInstance().currentUser = userId
    }

}