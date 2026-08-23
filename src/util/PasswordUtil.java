package util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Very simple password hashing so we don't store plain text.
 * Uses SHA-256 to match MySQL's SHA2(x, 256), which is what schema.sql
 * uses to seed the default admin password. If you later add a proper
 * hashing library (e.g. jBCrypt) you can swap the implementation here
 * without touching any other class.
 */
public class PasswordUtil {

    public static String hash(String rawPassword) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(rawPassword.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException | java.io.UnsupportedEncodingException e) {
            throw new RuntimeException("Could not hash password", e);
        }
    }

    public static boolean matches(String rawPassword, String hashedPassword) {
        return hash(rawPassword).equalsIgnoreCase(hashedPassword);
    }
}
