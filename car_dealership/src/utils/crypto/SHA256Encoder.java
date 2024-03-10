package utils.crypto;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class SHA256Encoder {
    public static final Charset UTF_8 = StandardCharsets.UTF_8;
    private static final String HASH_ALGORITHM = "SHA3-256";

    public static byte[] resume(byte[] inputBytes) {
        MessageDigest messageDigest = null;

        try {
            messageDigest = MessageDigest.getInstance(HASH_ALGORITHM);
        } catch (Exception e) {
            e.printStackTrace();
        }

        byte[] resultingBytes = messageDigest.digest(inputBytes);
        return resultingBytes;
    }
}
