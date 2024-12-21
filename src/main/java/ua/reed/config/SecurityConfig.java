package ua.reed.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;

@Configuration
public class SecurityConfig {

    /**
     * Creates an instance of {@link CognitoIdentityProviderClient} which is used across the application
     * and provides authentication/authorization capabilities.
     * Note: In this project this is used as a sandbox in order to have some practice using AWS Cognito.
     *
     * @return instance of {@link CognitoIdentityProviderClient}
     */
    @Bean
    public CognitoIdentityProviderClient cognitoIdentityProviderClient() {
        return CognitoIdentityProviderClient.builder()
                .region(Region.EU_NORTH_1)
                .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
                .build();
    }
}
