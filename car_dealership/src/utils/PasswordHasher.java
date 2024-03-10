package utils;

import java.util.Base64;

import utils.crypto.SHA256Encoder;

public class PasswordHasher {
    public static String hashPassword(String password) {
        String shaHashedPassword = new String(SHA256Encoder.resume(password.getBytes()));
        return Base64.getEncoder().encodeToString(shaHashedPassword.getBytes());
    }
}
