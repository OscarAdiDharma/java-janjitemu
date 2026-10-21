package com.janjitemu.app.infrastructure.seeder;

import com.janjitemu.app.application.service.LegalServiceService;
import com.janjitemu.app.application.service.UserService;
import com.janjitemu.app.domain.model.ServiceItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);

    @Autowired
    private UserService userService;

    @Autowired
    private LegalServiceService legalServiceService;

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
        if (!userService.existsByEmail("admin@janjitemu.com")) {
            userService.registerUser(
                    "Admin Master",
                    "admin@janjitemu.com",
                    "admin123",
                    "admin"
            );
            log.info("✅ Akun Admin Berhasil Dibuat: admin@janjitemu.com / admin123");
        }
    }

    private void seedServices() {
        if (legalServiceService.countServices() == 0) {
            List<ServiceItem> defaults = List.of(
                    new ServiceItem("Badan Usaha", "Pendirian PT, CV, Yayasan"),
                    new ServiceItem("Perdata", "Konsultasi Hukum Perdata"),
                    new ServiceItem("Tata Negara", "Hukum Tata Negara"),
                    new ServiceItem("OPHI", "Optimalisasi Hak Intelektual")
            );
            legalServiceService.saveAll(defaults);
            log.info("✅ Layanan Default Berhasil Dibuat (4 Layanan)");
        }
    }
}
