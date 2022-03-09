package com.sharenow.codingchallenge.contacts.data

/**
 * Contact description. Includes name and e-mail address.
 */
data class Contact(
    /**
     * @return ID of contact.
     */
    val id: Int,
    /**
     * @return Name of contact.
     */
    val name: String,
    /**
     * @return E-mail of contact.
     */
    val email: String
) 
