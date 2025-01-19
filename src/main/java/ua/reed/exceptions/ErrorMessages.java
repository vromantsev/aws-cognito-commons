package ua.reed.exceptions;

public enum ErrorMessages {
    ENV_HAS_NO_PROPERTY_MSG("Current environment has no value found for property %s"),
    FAILED_SIGN_UP_MSG("Failed to sing up a new user: '%s'"),
    FAILED_TO_CONFIRM_USER("Cannot verify user: '%s' via confirmation code"),
    FAILED_TO_SIGN_IN_USER("Failed to sign in a user: '%s'"),
    UNSUPPORTED_ALGORITHM("Failed to create hash via unsupported algorithm - '%s'"),
    INVALID_KEY("Cannot init secret key"),
    FAILED_TO_CHANGE_PASSWORD("Failed to change a password, details: %s"),
    FAILED_TO_RESET_PASSWORD("Failed to reset password"),
    FAILED_TO_CONFIRM_PASSWORD_RESET("Failed to confirm password reset for user: %s");

    private String message;

    ErrorMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
