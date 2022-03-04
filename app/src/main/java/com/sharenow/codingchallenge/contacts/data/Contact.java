package com.sharenow.codingchallenge.contacts.data;

/**
 * Contact description. Includes name and e-mail address.
 */
public class Contact {

    private final int id;
    private final String name;
    private final String email;

    public Contact(
            int id,
            String name,
            String email
    ) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    /**
     * @return ID of contact.
     */
    public int getId() {
        return id;
    }

    /**
     * @return Name of contact.
     */
    public String getName() {
        return name;
    }

    /**
     * @return E-mail of contact.
     */
    public String getEmail() {
        return email;
    }
}
