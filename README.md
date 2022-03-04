Files marked with @DoNotAlter should not be altered.

## Known bugs:
1. Sometimes after loading contacts there are duplicated entries in the list
2. Application is unresponsive while loading the data
3. Application accesses contacts service twice (check the entries "I/ContactsService: Loading contacts for user ID" in the logs)
4. Number of items in the lower part of the screen and actual number of items are out of sync sometimes
5. Refresh stops working after an error

## Features to implement
1. Contacts should be sorted by name
2. Add search field that allows to filter contacts to the ones that contain search query in the e-mail or the name.
   The number at the bottom should still show total number of contacts, not the filtered ones.

## What to refactor
1. Refactor adapter into a Kotlin class using View Binding
2. (optional) Also refactor UsersAdapter to use view binding
3. Refactor UserManager from a singleton into injected object that is scoped to whole application.
   Rewrite it in Kotlin and add reactive API instead of change listeners.
4. Anything else that you may want to refactor.