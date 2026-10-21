package com.janjitemu.app.controller;

import com.janjitemu.app.dto.MessageResponse;
import com.janjitemu.app.dto.ServiceRequest;
import com.janjitemu.app.model.ServiceItem;
import com.janjitemu.app.repository.ServiceRepository;
import com.janjitemu.app.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/services")
public class ServiceController {

    @Autowired
    private ServiceRepository serviceRepository;

    @GetMapping
    public List<ServiceItem> getAllServices() {
        return serviceRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<?> addService(@RequestBody ServiceRequest request,
                                        @AuthenticationPrincipal UserPrincipal currentUser) {
        if (currentUser == null || !"admin".equalsIgnoreCase(currentUser.getRole())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new MessageResponse("Akses Ditolak"));
        }

        ServiceItem item = new ServiceItem(request.getNama(), request.getDeskripsi());
        serviceRepository.save(item);

        return ResponseEntity.ok(new MessageResponse("Layanan ditambahkan"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateService(@PathVariable String id,
                                           @RequestBody ServiceRequest request,
                                           @AuthenticationPrincipal UserPrincipal currentUser) {
        if (currentUser == null || !"admin".equalsIgnoreCase(currentUser.getRole())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new MessageResponse("Akses Ditolak"));
        }

        Optional<ServiceItem> opt = serviceRepository.findById(id);
        if (opt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Layanan tidak ditemukan"));
        }

        ServiceItem item = opt.get();
        if (request.getNama() != null) {
            item.setNama(request.getNama());
        }
        if (request.getDeskripsi() != null) {
            item.setDeskripsi(request.getDeskripsi());
        }
        serviceRepository.save(item);

        return ResponseEntity.ok(new MessageResponse("Layanan diperbarui"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteService(@PathVariable String id,
                                           @AuthenticationPrincipal UserPrincipal currentUser) {
        if (currentUser == null || !"admin".equalsIgnoreCase(currentUser.getRole())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new MessageResponse("Akses Ditolak"));
        }

        serviceRepository.deleteById(id);
        return ResponseEntity.ok(new MessageResponse("Layanan dihapus"));
    }
}
