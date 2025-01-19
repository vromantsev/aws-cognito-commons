package ua.reed.dto;

public record CustomConfirmChangePasswordRequest(String username, String password, String confirmationCode) {
}
