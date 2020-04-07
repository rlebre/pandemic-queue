package com.ruilebre.pandemicqueue.ui.login;

import androidx.annotation.Nullable;

/**
 * Authentication result : success (user details) or error message.
 */
class LoginResult {
    @Nullable
    private String message;
    @Nullable
    private Boolean error;

    LoginResult(@Nullable Boolean error, @Nullable String message) {
        this.error = error;
        this.message = message;
    }

    @Nullable
    String getMessage() {
        return message;
    }

    @Nullable
    Boolean getError() {
        return error;
    }
}
