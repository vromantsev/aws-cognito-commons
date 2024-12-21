package ua.reed.exceptions;

public enum ErrorMessages {
    ENV_HAS_NO_PROPERTY_MSG("Current environment has no value found for property %s"),
    FAILED_SIGN_UP_MSG("Failed to sing up a new user: '%s'"),
    FAILED_TO_CONFIRM_USER("Cannot verify user: '%s' via confirmation code"),
    FAILED_TO_SIGN_IN_USER("Failed to sign in a user: '%s'");

    private String message;

    ErrorMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
