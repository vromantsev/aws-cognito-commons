package ua.reed.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.CodeDeliveryDetailsType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.CognitoIdentityProviderException;
import ua.reed.dto.CustomChangePasswordRequest;
import ua.reed.dto.CustomConfirmChangePasswordRequest;
import ua.reed.dto.CustomSignInRequest;
import ua.reed.dto.CustomSignInResponse;
import ua.reed.dto.CustomSignUpRequest;
import ua.reed.dto.ResetPasswordRequest;
import ua.reed.dto.UserVerificationRequest;
import ua.reed.exceptions.AuthException;

import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

import static ua.reed.exceptions.ErrorMessages.FAILED_SIGN_UP_MSG;
import static ua.reed.exceptions.ErrorMessages.FAILED_TO_CHANGE_PASSWORD;
import static ua.reed.exceptions.ErrorMessages.FAILED_TO_CONFIRM_PASSWORD_RESET;
import static ua.reed.exceptions.ErrorMessages.FAILED_TO_CONFIRM_USER;
import static ua.reed.exceptions.ErrorMessages.FAILED_TO_RESET_PASSWORD;
import static ua.reed.exceptions.ErrorMessages.FAILED_TO_SIGN_IN_USER;
import static ua.reed.utils.CloudUtils.createChangePasswordRequest;
import static ua.reed.utils.CloudUtils.createConfirmForgotPasswordRequest;
import static ua.reed.utils.CloudUtils.createEmailVerificationRequest;
import static ua.reed.utils.CloudUtils.createForgotPasswordRequest;
import static ua.reed.utils.CloudUtils.createInitiateAuthRequest;
import static ua.reed.utils.CloudUtils.createSignUpRequest;

@Service
public class SimpleAuthService implements AuthService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleAuthService.class);

    private final CognitoIdentityProviderClient cognito;

    @Autowired
    public SimpleAuthService(final CognitoIdentityProviderClient cognito) {
        this.cognito = cognito;
    }

    /**
     * {@inheritDoc}
     *
     * @param customSignUpRequest custom sign up details provided by a new user
     */
    @Override
    public void signUp(final CustomSignUpRequest customSignUpRequest) {
        try {
            var req = createSignUpRequest(customSignUpRequest);
            var response = this.cognito.signUp(req);
            LOGGER.info("Received userSub={}, userConfirmed={}, http status code={}", response.userSub(), response.userConfirmed(), response.sdkHttpResponse().statusCode());
        } catch (CognitoIdentityProviderException cipe) {
            throw new AuthException(FAILED_SIGN_UP_MSG.getMessage().formatted(customSignUpRequest.email()), cipe);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param verificationRequest verification request
     */
    @Override
    public void verifyUserByVerificationCode(final UserVerificationRequest verificationRequest) {
        try {
            var confirmation = createEmailVerificationRequest(verificationRequest);
            var confirmSignUpResponse = this.cognito.confirmSignUp(confirmation);
            var httpResponse = confirmSignUpResponse.sdkHttpResponse();
            LOGGER.info("Successfully confirmed identity of user: '{}', status code: {}, message: '{}'", verificationRequest.username(), httpResponse.statusCode(), httpResponse.statusText());
        } catch (CognitoIdentityProviderException cipe) {
            throw new AuthException(FAILED_TO_CONFIRM_USER.getMessage().formatted(verificationRequest.username()), cipe);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param customSignInRequest sign in request
     * @return authentication response
     */
    @Override
    public CustomSignInResponse login(final CustomSignInRequest customSignInRequest) {
        try {
            var request = createInitiateAuthRequest(customSignInRequest);
            var authResponse = this.cognito.initiateAuth(request);
            var authResult = authResponse.authenticationResult();
            String expiresIn = Instant.now()
                    .plus(authResult.expiresIn(), ChronoUnit.SECONDS)
                    .atZone(ZoneId.systemDefault())
                    .toString();
            return new CustomSignInResponse(authResult.accessToken(), expiresIn, authResult.refreshToken(), authResult.idToken(), authResult.tokenType());
        } catch (CognitoIdentityProviderException cipe) {
            throw new AuthException(FAILED_TO_SIGN_IN_USER.getMessage().formatted(customSignInRequest.email()), cipe);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param request change password request
     */
    @Override
    public void changePassword(final CustomChangePasswordRequest request) {
        try {
            var changePasswordRequest = createChangePasswordRequest(request);
            var response = this.cognito.changePassword(changePasswordRequest);
            var sdkHttpResponse = response.sdkHttpResponse();
            LOGGER.info("Got the response for reset password operation: status: {}, message: {}", sdkHttpResponse.statusCode(), sdkHttpResponse.statusText());
        } catch (CognitoIdentityProviderException cipe) {
            throw new AuthException(FAILED_TO_CHANGE_PASSWORD.getMessage().formatted(cipe.getMessage()), cipe);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param resetPasswordRequest reset password request
     */
    @Override
    public void resetPassword(final ResetPasswordRequest resetPasswordRequest) {
        try {
            var forgotPasswordRequest = createForgotPasswordRequest(resetPasswordRequest);
            var response = this.cognito.forgotPassword(forgotPasswordRequest);
            CodeDeliveryDetailsType type = response.codeDeliveryDetails();
            LOGGER.info("Reset password details - attributeName: {}, deliveryMedium: {}, destination: {}", type.attributeName(), type.deliveryMediumAsString(), type.destination());
        } catch (CognitoIdentityProviderException cipe) {
            throw new AuthException(FAILED_TO_RESET_PASSWORD.getMessage(), cipe);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param request confirm change password request
     */
    @Override
    public void confirmResetPassword(final CustomConfirmChangePasswordRequest request) {
        try {
            var confirmForgotPasswordRequest = createConfirmForgotPasswordRequest(request);
            var response = cognito.confirmForgotPassword(confirmForgotPasswordRequest);
            var sdkHttpResponse = response.sdkHttpResponse();
            LOGGER.info("Got response for reset password confirmation - status: {}, message: {}", sdkHttpResponse.statusCode(), sdkHttpResponse.statusText());
        } catch (CognitoIdentityProviderException cipe) {
            throw new AuthException(FAILED_TO_CONFIRM_PASSWORD_RESET.getMessage().formatted(request.username()), cipe);
        }
    }
}
