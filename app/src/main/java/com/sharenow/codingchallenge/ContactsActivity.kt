package com.sharenow.codingchallenge

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.sharenow.codingchallenge.contacts.ContactsViewModel
import com.sharenow.codingchallenge.contacts.data.ContactsState
import com.sharenow.codingchallenge.contacts.view.ContactsAdapter
import com.sharenow.codingchallenge.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * Activity that displays contacts.
 */
@AndroidEntryPoint
class ContactsActivity : AppCompatActivity() {

    private val viewBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val viewModel: ContactsViewModel by viewModels()
    private val contactsAdapter = ContactsAdapter()
    private val usersAdapter by lazy {
        val adapter = ArrayAdapter<String>(this, R.layout.user_item, R.id.user)
        adapter.setDropDownViewResource(R.layout.user_item_expanded)
        adapter
    }

    private var updatingState = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
        setSupportActionBar(viewBinding.toolbar)

        with(viewBinding) {
            contactsRecyclerView.adapter = contactsAdapter
            userSpinner.adapter = usersAdapter

            swipeRefreshLayout.setOnRefreshListener {
                viewModel.onRefreshRequested()
            }

            userSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    if (updatingState) {
                        return
                    }

                    val selectedUser = usersAdapter.getItem(position) ?: return
                    viewModel.onUserSelected(selectedUser)
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // when nothing selected, nothing is done (c) Some philosopher probably
                }
            }

            search.addTextChangedListener {
                contactsAdapter.search(it.toString())
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel
                    .stateFlow()
                    .collect {
                        updateState(it)
                    }
            }
        }
    }

    private fun updateState(state: ContactsState) {
        updatingState = true

        with(viewBinding) {
            swipeRefreshLayout.isRefreshing = false

            usersAdapter.clear()
            usersAdapter.addAll(state.availableUsers)
            usersAdapter.notifyDataSetChanged()

            userSpinner.setSelection(state.availableUsers.indexOf(state.selectedUser))

            when (state) {
                is ContactsState.Available -> {
                    error.isVisible = false
                    contactsRecyclerView.isVisible = true
                    count.isInvisible = false

                    contactsAdapter.submitContacts(state.contacts)
                    count.text = "${state.allContactsCount}"
                }

                is ContactsState.Error -> {
                    contactsRecyclerView.isVisible = false
                    error.isVisible = true
                    count.isInvisible = true

                    error.text = getString(R.string.general_error)
                }
            }
        }

        updatingState = false
    }
}
