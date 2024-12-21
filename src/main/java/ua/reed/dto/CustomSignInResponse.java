package ua.reed.dto;

public record CustomSignInResponse(String token,
                                   String expiresIn,
                                   String refreshToken,
                                   String idToken,
                                   String tokenType) {
}
