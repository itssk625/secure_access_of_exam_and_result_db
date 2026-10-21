
package com.secureaccess.exam_access_control;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class PasswordHashGenerator {

    @Bean
    CommandLineRunner generatePasswordHashes(
            PasswordEncoder passwordEncoder) {

        return args -> {
            String password = "StudentTest@123";

            System.out.println("Temporary test password: " + password);

            for (int i = 1; i <= 4; i++) {
                System.out.println(
                    "Hash " + i + ": "
                    + passwordEncoder.encode(password)
                );
            }
        };
    }
}
