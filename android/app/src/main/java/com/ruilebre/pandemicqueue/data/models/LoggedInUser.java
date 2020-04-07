package com.ruilebre.pandemicqueue.data.models;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUser {

    private static LoggedInUser single_instance = null;

    private String userId;
    private String displayName;
    private SessionToken token;

    private LoggedInUser(String userId, String displayName, SessionToken token) {
        this.userId = userId;
        this.displayName = displayName;
        this.token = token;
    }

    public static LoggedInUser getInstance(String userId, String displayName, SessionToken token) {
        if (single_instance == null)
            single_instance = new LoggedInUser(userId, displayName, token);

        return single_instance;
    }

    public static LoggedInUser getInstance() {
        return single_instance;
    }

    public String getUserId() {
        return userId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public SessionToken getToken() {
        return token;
    }


}
