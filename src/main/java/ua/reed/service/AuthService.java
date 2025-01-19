package ua.reed.service;

import ua.reed.dto.CustomChangePasswordRequest;
import ua.reed.dto.CustomConfirmChangePasswordRequest;
import ua.reed.dto.CustomSignInRequest;
import ua.reed.dto.CustomSignInResponse;
import ua.reed.dto.CustomSignUpRequest;
import ua.reed.dto.ResetPasswordRequest;
import ua.reed.dto.UserVerificationRequest;

public interface AuthService {

    /**
     * Signs up a new user. Uses email verification in order to validate whether it is a real user or not.
     * Basically that meant that a verification code is sent.
     *
     * @param request custom sign up details provided by a new user
     */
    void signUp(CustomSignUpRequest request);

    /**
     * Verifies a user by a confirmation code. The code itself is sent to the user specified email.
     *
     * @param request verification request
     */
    void verifyUserByVerificationCode(UserVerificationRequest request);

    /**
     * Signs in a user by the provided credentials.
     *
     * @param request sign in request
     * @return authentication response
     */
    CustomSignInResponse login(CustomSignInRequest request);

    /**
     * Allows users to change their password. This feature can not be user for the users who forgot their password.
     *
     * @param request change password request
     */
    void changePassword(CustomChangePasswordRequest request);

    /**
     * Allows to reset password using 'forgot password' feature.
     *
     * @param request reset password request
     */
    void resetPassword(ResetPasswordRequest request);

    /**
     * Confirms a password reset. For those who use 'forgot password' feature.
     *
     * @param request confirm change password request
     */
    void confirmResetPassword(CustomConfirmChangePasswordRequest request);

}
