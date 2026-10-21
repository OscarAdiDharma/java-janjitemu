package com.janjitemu.app.service;

import com.janjitemu.app.model.ServiceItem;
import com.janjitemu.app.model.User;
import com.janjitemu.app.repository.ServiceRepository;
import com.janjitemu.app.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        try {
            seedAdmin();
            seedServices();
        } catch (Exception e) {
            log.warn("⚠️ Gagal seeding data awal (Database mungkin belum terhubung): {}", e.getMessage());
        }
    }

    private void seedAdmin() {
        if (userRepository.findByEmail("admin@janjitemu.com").isEmpty()) {
            User admin = new User(
                    "Admin Master",
                    "admin@janjitemu.com",
                    passwordEncoder.encode("admin123"),
                    "admin"
            );
            userRepository.save(admin);
            log.info("✅ Akun Admin Berhasil Dibuat: admin@janjitemu.com / admin123");
        }
    }

    private void seedServices() {
        if (serviceRepository.count() == 0) {
            List<ServiceItem> defaults = List.of(
                    new ServiceItem("Badan Usaha", "Pendirian PT, CV, Yayasan"),
                    new ServiceItem("Perdata", "Konsultasi Hukum Perdata"),
                    new ServiceItem("Tata Negara", "Hukum Tata Negara"),
                    new ServiceItem("OPHI", "Optimalisasi Hak Intelektual")
            );
            serviceRepository.saveAll(defaults);
            log.info("✅ Layanan Default Berhasil Dibuat (4 Layanan)");
        }
    }
}
