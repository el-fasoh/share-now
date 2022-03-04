@file:DoNotAlter

package com.sharenow.codingchallenge.contacts.di

import com.sharenow.codingchallenge.challenge.DoNotAlter
import com.sharenow.codingchallenge.contacts.domain.ContactsApi
import com.sharenow.codingchallenge.contacts.domain.impl.FakeContactsService
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Component that provides implementation for [ContactsApi].
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class ContactsApiModule {

    @Binds
    @Singleton
    abstract fun bindsContactsApi(fakeContactsService: FakeContactsService): ContactsApi

}