package com.opencampus.cartel.services;

import com.opencampus.cartel.model.entity.Inventory;
import com.opencampus.cartel.model.entity.Product;
import com.opencampus.cartel.model.entity.User;
import com.opencampus.cartel.model.entity.Warehouse;
import com.opencampus.cartel.model.enums.OperationStatus;
import com.opencampus.cartel.model.enums.ProductType;
import com.opencampus.cartel.model.enums.Role;
import com.opencampus.cartel.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DataInitializationService implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        createDefaultAdmin();
        System.out.println("✅ Default admin user ready!");
        System.out.println("🔑 Login: admin / admin123");
    }

    private void createDefaultAdmin() {
        // Always delete and recreate admin to ensure fresh credentials
        userRepository.findByUsername("admin").ifPresent(userRepository::delete);

        // Create admin user with fresh password encoding
        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setEmail("admin@cartel.com");
        admin.setCodeName("El Jefe");
        admin.setRole(Role.ADMIN);
        admin.setPhoneNumber("+1234567890");
        admin.setTerritory("Headquarters");
        admin.setIsActive(true);

        User savedAdmin = userRepository.save(admin);

        // Verify the password works
        boolean passwordMatch = passwordEncoder.matches("admin123", savedAdmin.getPassword());
        System.out.println("🔐 Admin password verification: " + (passwordMatch ? "✅ SUCCESS" : "❌ FAILED"));
        System.out.println("👤 Admin ID: " + savedAdmin.getId());
        System.out.println("🏠 Use these credentials in Postman:");
        System.out.println("   Username: admin");
        System.out.println("   Password: admin123");
    }
}