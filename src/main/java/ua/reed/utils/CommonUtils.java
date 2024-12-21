package ua.reed.utils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Map;
import java.util.Objects;

import static ua.reed.exceptions.ErrorMessages.ENV_HAS_NO_PROPERTY_MSG;
import static ua.reed.utils.Constants.HASH_SECRET_ALGORITHM;

public final class CommonUtils {

    private static final Map<String, String> ENV = System.getenv();

    private CommonUtils() {}

    public static String getEnvProperty(final String key) {
        return ENV.get(key);
    }

    /**
     * Calculates a secret hash for a user in order to determine the user in a Cognito pool.
     *
     * @param username username
     * @param clientId client id
     * @param clientSecret client secret
     *
     * @return calculated secret hash for a user
     */
    public static String calculateSecretHash(final String username, final String clientId, final String clientSecret) {
        Objects.requireNonNull(username, "Param [username] must not be null!");
        Objects.requireNonNull(clientId, "Param [clientId] must not be null!");
        Objects.requireNonNull(clientSecret, "Param [clientSecret] must not be null!");
        String hashAlgorithm = CommonUtils.getEnvProperty(HASH_SECRET_ALGORITHM);
        if (Objects.isNull(hashAlgorithm)) {
            throw new IllegalArgumentException(ENV_HAS_NO_PROPERTY_MSG.getMessage().formatted(HASH_SECRET_ALGORITHM));
        }
        try {
            Mac mac = Mac.getInstance(hashAlgorithm);
            SecretKeySpec secretKey = new SecretKeySpec(clientSecret.getBytes(), HASH_SECRET_ALGORITHM);
            mac.init(secretKey);
            String message = username + clientId;
            byte[] rawHmac = mac.doFinal(message.getBytes());
            return Base64.getEncoder().encodeToString(rawHmac);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to create hash via unsupported algorithm - '%s'".formatted(hashAlgorithm), e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException("Cannot init secret key", e);
        }
    }
}
