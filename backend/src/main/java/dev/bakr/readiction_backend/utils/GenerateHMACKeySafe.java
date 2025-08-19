package dev.bakr.readiction_backend.utils;

import io.jsonwebtoken.io.Encoders;

import java.security.SecureRandom;

public class GenerateHMACKeySafe {
    public static void main(String[] args) {
        try {
            // Creates empty array: e.g., [0, 0, 0, 0, ...] filled with bytes, all initialized to 0
            byte[] keyBytes = new byte[32];

            // Creates a cryptographically secure random number generator
            SecureRandom randomNumberGenerator = new SecureRandom();

            // Fills the keyBytes array with random secure bytes: e.g., [47, -23, 108, 91, ...]
            randomNumberGenerator.nextBytes(keyBytes);

            // Encode to Base64: converts binary data to Base64 text representation for safe storage/transmission
            // NOTE: Base64 allows embedding binary files (images, documents) within text-based formats like HTML, JSON, or email,
            String secretKey = Encoders.BASE64.encode(keyBytes);

            System.out.println("=== Generated HMAC Secret Key ===");
            System.out.println();
            System.out.println("🔑 Copy this line to your existing .env file:");
            System.out.println("JWT_SECRET_KEY=" + secretKey);
            System.out.println();
            System.out.println("📝 Also add this if you don't have it:");
            System.out.println("JWT_EXPIRATION=86400000");
            System.out.println();
            System.out.println("=== Key Information ===");
            System.out.println("• Algorithm: HMAC-SHA256 (HS256)");
            System.out.println("• Key Length: " + secretKey.length() + " characters");
            System.out.println("• Decoded Length: 32 bytes (256 bits)");
            System.out.println("• Cryptographically secure: ✅");
            System.out.println();
            System.out.println("⚠️  IMPORTANT: Keep this key secret and secure!");

        } catch (Exception e) {
            System.err.println("Error generating key: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
