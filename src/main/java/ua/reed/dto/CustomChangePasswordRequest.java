package ua.reed.dto;

public record CustomChangePasswordRequest(String token, String oldPassword, String newPassword) {
}
