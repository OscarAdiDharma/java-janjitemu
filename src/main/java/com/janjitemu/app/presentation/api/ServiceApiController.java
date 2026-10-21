package com.janjitemu.app.presentation.api;

import com.janjitemu.app.application.dto.common.MessageResponse;
import com.janjitemu.app.application.dto.service.ServiceRequest;
import com.janjitemu.app.application.service.LegalServiceService;
import com.janjitemu.app.domain.model.ServiceItem;
import com.janjitemu.app.infrastructure.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/services")
public class ServiceApiController {

    @Autowired
    private LegalServiceService legalServiceService;

    @GetMapping
    public List<ServiceItem> getAllServices() {
        return legalServiceService.getAllServices();
    }

    @PostMapping
    public ResponseEntity<?> addService(@RequestBody ServiceRequest request,
                                        @AuthenticationPrincipal UserPrincipal currentUser) {
        if (currentUser == null || !"admin".equalsIgnoreCase(currentUser.getRole())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new MessageResponse("Akses Ditolak"));
        }

        legalServiceService.createService(request.getNama(), request.getDeskripsi());
        return ResponseEntity.ok(new MessageResponse("Layanan ditambahkan"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateService(@PathVariable String id,
                                           @RequestBody ServiceRequest request,
                                           @AuthenticationPrincipal UserPrincipal currentUser) {
        if (currentUser == null || !"admin".equalsIgnoreCase(currentUser.getRole())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new MessageResponse("Akses Ditolak"));
        }

        boolean updated = legalServiceService.updateService(id, request.getNama(), request.getDeskripsi());
        if (!updated) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Layanan tidak ditemukan"));
        }

        return ResponseEntity.ok(new MessageResponse("Layanan diperbarui"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteService(@PathVariable String id,
                                           @AuthenticationPrincipal UserPrincipal currentUser) {
        if (currentUser == null || !"admin".equalsIgnoreCase(currentUser.getRole())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new MessageResponse("Akses Ditolak"));
        }

        boolean deleted = legalServiceService.deleteService(id);
        if (!deleted) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Layanan tidak ditemukan"));
        }

        return ResponseEntity.ok(new MessageResponse("Layanan dihapus"));
    }
}
