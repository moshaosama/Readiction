package dev.bakr.readiction_backend.utils;

import dev.bakr.readiction_backend.enums.ReadingStatus;

public abstract class StatusValidator {
    public static boolean validateStatus(String bookStatus) {
        boolean validStatus = false;
        // loop over the enum values (constants) and compare each value with the give book status
        for (int i = 0; i < ReadingStatus.values().length; i++) {
            var statusConstant = ReadingStatus.values()[i];
            if (statusConstant.name().equalsIgnoreCase(bookStatus)) {
                validStatus = true;
                break;
            }
        }
        return validStatus;
    }
}
