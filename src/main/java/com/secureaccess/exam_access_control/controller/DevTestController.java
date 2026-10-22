package com.secureaccess.exam_access_control.controller;

import java.util.List;
import java.util.Map;
import org.springframework.context.annotation.Profile;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Development-only diagnostic endpoints. Activate the "dev" Spring profile
 * explicitly to expose them; they are not registered in the default profile.
 */
@RestController
@Profile("dev")
public class DevTestController {

    @GetMapping("/api/test/me")
    public Map<String, Object> currentUser(Authentication authentication) {
        List<String> authorities = authentication.getAuthorities().stream()
                .map(authority -> authority.getAuthority())
                .toList();
        return Map.of(
                "username", authentication.getName(),
                "authorities", authorities
        );
    }

    @GetMapping("/api/test/enter-marks")
    @PreAuthorize("hasAuthority('ENTER_MARKS')")
    public Map<String, String> testEnterMarksPermission() {
        return Map.of(
                "message", "Access granted",
                "requiredPermission", "ENTER_MARKS"
        );
    }
}
