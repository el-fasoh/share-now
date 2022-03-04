package com.sharenow.codingchallenge.user;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Handles users that are saved in the app.
 */
public final class UserManager {

    /**
     * Interface for user change listeners.
     */
    public interface OnUserChangedListener {

        /**
         * This method is called when user change has occurred.
         * @param newUserId New user ID.
         */
        void onUserChanged(final String newUserId);
    }


    private static final UserManager INSTANCE = new UserManager();

    private static final String PREFS_NAME = "UserManager_prefs";
    private static final String CURRENT_USER_KEY = "currentUser";

    private Context context = null;
    private final Set<OnUserChangedListener> listeners = new HashSet<>();

    /**
     * @return singleton instance of UserManager.
     */
    public static UserManager getInstance() {
        return INSTANCE;
    }

    /**
     * Initialize UserManager.
     *
     * @param context Context of the application.
     */
    public void initialize(Context context) {
        this.context = context;
    }

    /**
     * @return List of all available users.
     */
    public List<String> getAvailableUsers() {
        return HardcodedUsersList.INSTANCE.getAllUsers();
    }

    /**
     * @return current user ID.
     */
    public String getCurrentUser() {
        return getPrefs().getString(CURRENT_USER_KEY, getAvailableUsers().get(0));
    }

    /**
     *
     * @param id New selected user ID.
     */
    public void setCurrentUser(String id) {
        if (!getAvailableUsers().contains(id)) {
            throw new IllegalArgumentException("Unknown user ID " + id);
        }

        getPrefs()
                .edit()
                .putString(CURRENT_USER_KEY, id)
                .apply();

        synchronized (listeners) {
            for (OnUserChangedListener l : listeners) {
                l.onUserChanged(id);
            }
        }
    }

    /**
     * Add user change listener.
     * @param l Listener to add. If an attempt to add same instance will be made, nothing will happen.
     */
    public void addOnUserChangeListener(OnUserChangedListener l) {
        synchronized (listeners) {
            listeners.add(l);
        }
    }

    /**
     * Remove user change listener.
     * @param l Listener to remove.
     */
    public void removeOnUserChangeListener(OnUserChangedListener l) {
        synchronized (listeners) {
            listeners.remove(l);
        }
    }

    private SharedPreferences getPrefs() {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

}
