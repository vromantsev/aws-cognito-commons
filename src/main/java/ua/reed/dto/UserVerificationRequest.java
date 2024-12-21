package ua.reed.dto;

public record UserVerificationRequest(String username, String confirmationCode) {
}
