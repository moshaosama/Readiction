package dev.bakr.readiction_backend.utils;

import java.util.Random;

public abstract class GenerateVerificationCode {
    public static String generateCode() {
        Random random = new Random();
        int code = random.nextInt(900000) + 100000;
        return String.valueOf(code);
    }
}
