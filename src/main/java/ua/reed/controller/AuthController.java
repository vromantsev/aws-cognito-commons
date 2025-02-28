package ua.reed.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ua.reed.dto.CustomChangePasswordRequest;
import ua.reed.dto.CustomConfirmChangePasswordRequest;
import ua.reed.dto.CustomSignInRequest;
import ua.reed.dto.CustomSignInResponse;
import ua.reed.dto.CustomSignUpRequest;
import ua.reed.dto.ResetPasswordRequest;
import ua.reed.dto.UserVerificationRequest;
import ua.reed.service.AuthService;

/**
 * Simple REST controller that provides basic auth capabilities for the end users.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(final AuthService authService) {
        this.authService = authService;
    }

    /**
     * Allows a new user to sign-up.
     *
     * @param customSignUpRequest sign up details provided by a new user
     */
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/sign-up")
    public void signUp(@RequestBody final CustomSignUpRequest customSignUpRequest) {
        this.authService.signUp(customSignUpRequest);
    }

    /**
     * Confirms user email by verification code.
     *
     * @param verificationRequest verification request
     */
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/verify")
    public void verifyUserByVerificationCode(@RequestBody final UserVerificationRequest verificationRequest) {
        this.authService.verifyUserByVerificationCode(verificationRequest);
    }

    /**
     * Signs in a user using provided credentials.
     *
     * @param request sign in request
     * @return auth response
     */
    @PostMapping("/sign-in")
    public ResponseEntity<CustomSignInResponse> signIn(@RequestBody final CustomSignInRequest request) {
        return ResponseEntity
                .ok()
                .body(this.authService.login(request));
    }

    /**
     * Allows to change password. This flow is for the authenticated users, because aut token is mandatory.
     * This feature can not be used for the users who forget their password.
     *
     * @param request change password request
     */
    @PostMapping("/change-password")
    @ResponseStatus(HttpStatus.OK)
    public void changePassword(@RequestBody final CustomChangePasswordRequest request) {
        this.authService.changePassword(request);
    }

    /**
     * Allows to reset password by leveraging Cognito's 'forgot password' feature.
     *
     * @param request reset password request
     */
    @PostMapping("/reset-password")
    @ResponseStatus(HttpStatus.OK)
    public void resetPassword(@RequestBody final ResetPasswordRequest request) {
        this.authService.resetPassword(request);
    }

    /**
     * Verifies a password reset. For those who use 'forgot password' feature.
     *
     * @param request confirm change password request
     */
    @PostMapping("/verify-reset-password")
    @ResponseStatus(HttpStatus.OK)
    public void verifyResetPassword(@RequestBody final CustomConfirmChangePasswordRequest request) {
        this.authService.confirmResetPassword(request);
    }
}
