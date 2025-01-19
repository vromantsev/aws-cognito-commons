package ua.reed.utils;

import software.amazon.awssdk.services.cognitoidentityprovider.model.AttributeType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AuthFlowType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ChangePasswordRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ConfirmForgotPasswordRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ConfirmSignUpRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ForgotPasswordRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.InitiateAuthRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.SignUpRequest;
import ua.reed.dto.CustomChangePasswordRequest;
import ua.reed.dto.CustomConfirmChangePasswordRequest;
import ua.reed.dto.CustomSignInRequest;
import ua.reed.dto.CustomSignUpRequest;
import ua.reed.dto.ResetPasswordRequest;
import ua.reed.dto.UserVerificationRequest;

import java.util.List;
import java.util.Map;

public final class CloudUtils {

    private static final String USERNAME = "USERNAME";
    private static final String PASSWORD = "PASSWORD";
    private static final String SECRET_HASH = "SECRET_HASH";
    private static final String EMAIL = "email";

    private CloudUtils() {}

    /**
     * Creates a wrapper which is being used in email verification for the specified user.
     *
     * @param verificationRequest verification request
     * @return instance of {@link ConfirmSignUpRequest}
     */
    public static ConfirmSignUpRequest createEmailVerificationRequest(final UserVerificationRequest verificationRequest) {
        return ConfirmSignUpRequest.builder()
                .clientId(CommonUtils.getEnvProperty(Constants.CLIENT_ID))
                .username(verificationRequest.username())
                .confirmationCode(verificationRequest.confirmationCode())
                .secretHash(
                        CommonUtils.calculateSecretHash(
                                verificationRequest.username(),
                                CommonUtils.getEnvProperty(Constants.CLIENT_ID),
                                CommonUtils.getEnvProperty(Constants.CLIENT_SECRET)
                        )
                )
                .build();
    }

    /**
     * Creates a wrapper for a new user tao being able to register in Cognito user pool.
     *
     * @param customSignUpRequest custom sign up request
     * @return instance of {@link SignUpRequest}
     */
    public static SignUpRequest createSignUpRequest(CustomSignUpRequest customSignUpRequest) {
        return SignUpRequest.builder()
                .clientId(CommonUtils.getEnvProperty(Constants.CLIENT_ID))
                .password(customSignUpRequest.password())
                .username(customSignUpRequest.email()) // username is a must, validation fails without this property
                .userAttributes(
                        List.of(
                                AttributeType.builder().name(EMAIL).value(customSignUpRequest.email()).build()
                        )
                )
                .secretHash(
                        CommonUtils.calculateSecretHash(
                                customSignUpRequest.email(),
                                CommonUtils.getEnvProperty(Constants.CLIENT_ID),
                                CommonUtils.getEnvProperty(Constants.CLIENT_SECRET)
                        )
                )
                .build();
    }

    /**
     * Creates a wrapper for a user to sign in via Cognito user pool.
     *
     * @param customSignInRequest custom sign in request
     * @return instance of {@link InitiateAuthRequest}
     */
    public static InitiateAuthRequest createInitiateAuthRequest(final CustomSignInRequest customSignInRequest) {
        String clientId = CommonUtils.getEnvProperty(Constants.CLIENT_ID);
        return InitiateAuthRequest.builder()
                .clientId(clientId)
                .authFlow(AuthFlowType.USER_PASSWORD_AUTH) // chosen for simplicity, not the most secure one
                .authParameters(
                        Map.of(
                                USERNAME, customSignInRequest.email(),
                                PASSWORD, customSignInRequest.password(),
                                SECRET_HASH, CommonUtils.calculateSecretHash(
                                        customSignInRequest.email(),
                                        clientId,
                                        CommonUtils.getEnvProperty(Constants.CLIENT_SECRET)
                                )
                        )
                )
                .build();
    }

    /**
     * Creates a wrapper for a user who wants to change their password via Cognito user pool.
     *
     * @param request custom change password request
     * @return instance of {@link ChangePasswordRequest}
     */
    public static ChangePasswordRequest createChangePasswordRequest(final CustomChangePasswordRequest request) {
        return ChangePasswordRequest.builder()
                .accessToken(request.token())
                .previousPassword(request.oldPassword())
                .proposedPassword(request.newPassword())
                .build();
    }

    /**
     * Creates a wrapper for user who wants to reset their password by leveraging Cognito's 'forgot password' feature.
     *
     * @param request custom reset password request
     * @return instance of {@link ForgotPasswordRequest}
     */
    public static ForgotPasswordRequest createForgotPasswordRequest(final ResetPasswordRequest request) {
        String clientId = CommonUtils.getEnvProperty(Constants.CLIENT_ID);
        return ForgotPasswordRequest.builder()
                .clientId(clientId)
                .username(request.username())
                .secretHash(
                        CommonUtils.calculateSecretHash(
                                request.username(),
                                clientId,
                                CommonUtils.getEnvProperty(Constants.CLIENT_SECRET)
                        )
                )
                .build();
    }

    /**
     * Creates a wrapper for a user that wants to confirm their password reset (for those who use 'forgot password' feature only).
     *
     * @param request custom confirm change password request
     * @return instance of {@link ConfirmForgotPasswordRequest}
     */
    public static ConfirmForgotPasswordRequest createConfirmForgotPasswordRequest(final CustomConfirmChangePasswordRequest request) {
        String clientId = CommonUtils.getEnvProperty(Constants.CLIENT_ID);
        return ConfirmForgotPasswordRequest.builder()
                .clientId(clientId)
                .confirmationCode(request.confirmationCode())
                .username(request.username())
                .password(request.password())
                .secretHash(
                        CommonUtils.calculateSecretHash(
                                request.username(),
                                clientId,
                                CommonUtils.getEnvProperty(Constants.CLIENT_SECRET)
                        )
                )
                .build();
    }
}
