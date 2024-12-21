package ua.reed.service;

import ua.reed.dto.CustomSignInRequest;
import ua.reed.dto.CustomSignInResponse;
import ua.reed.dto.CustomSignUpRequest;
import ua.reed.dto.UserVerificationRequest;

public interface AuthService {

    /**
     * Signs up a new user. Uses email verification in order to validate whether it is a real user or not.
     * Basically that meant that a verification code is sent.
     *
     * @param customSignUpRequest custom sign up details provided by a new user
     */
    void signUp(final CustomSignUpRequest customSignUpRequest);

    /**
     * Verifies a user by a confirmation code. The code itself is sent to the user specified email.
     *
     * @param verificationRequest verification request
     */
    void verifyUserByVerificationCode(UserVerificationRequest verificationRequest);

    /**
     * Signs in a user by the provided credentials.
     *
     * @param customSignInRequest sign in request
     * @return authentication response
     */
    CustomSignInResponse login(final CustomSignInRequest customSignInRequest);

}
