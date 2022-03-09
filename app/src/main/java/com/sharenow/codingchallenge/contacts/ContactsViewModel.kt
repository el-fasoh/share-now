package com.sharenow.codingchallenge.contacts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sharenow.codingchallenge.contacts.data.Contact
import com.sharenow.codingchallenge.contacts.data.ContactsState
import com.sharenow.codingchallenge.contacts.domain.ContactsApi
import com.sharenow.codingchallenge.user.UserManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * View model providing state for contacts activity.
 */
@HiltViewModel
class ContactsViewModel @Inject constructor(
    private val contactsApi: ContactsApi,
    private val userManager: UserManager
) : ViewModel() {

    private val refreshFlow = MutableSharedFlow<Unit>(replay = 1)

    init {
        refreshFlow.tryEmit(Unit)
    }

    /**
     * Returns [Flow] that never completes, never errors out
     * and emits current state.
     */
    fun stateFlow(): Flow<ContactsState> = flow {
        fetchTriggersFlow()
            .map { userId ->
                try {
                    val contacts = contactsApi.getContacts(userId).distinctBy { it.email }.sortedBy { it.name }
                    emit(
                        ContactsState.Available(
                            contacts = contacts,
                            allContactsCount = contacts.size,
                            availableUsers = userManager.getAvailableUsers(),
                            selectedUser = userManager.getCurrentUser()
                        ) as ContactsState
                    )
                } catch (e: Exception) {
                    emit(
                        ContactsState.Error(
                            availableUsers = userManager.getAvailableUsers(),
                            selectedUser = userManager.getCurrentUser()
                        )
                    )
                }
            }
            .collect()
    }.flowOn(Dispatchers.IO)

    private fun contactsFlow(): Flow<List<Contact>> {
        return fetchTriggersFlow()
            .map { userId ->
                contactsApi.getContacts(userId).toList()
            }
            .flowOn(Dispatchers.IO)
    }

    private fun fetchTriggersFlow(): Flow<String> {
        return combine(refreshFlow, userManager.currentUserAsFlow) { refresh, userId ->
            userId
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
        userManager.setCurrentUser(userId)
    }
}
