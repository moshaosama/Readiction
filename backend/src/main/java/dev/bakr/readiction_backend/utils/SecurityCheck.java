package dev.bakr.readiction_backend.utils;

import dev.bakr.readiction_backend.model.ReaderPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public abstract class SecurityCheck {
    public static Long getAuthenticatedUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        ReaderPrincipal reader = (ReaderPrincipal) auth.getPrincipal();
        return reader.getId();
    }
}
