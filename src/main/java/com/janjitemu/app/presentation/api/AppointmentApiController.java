package com.janjitemu.app.presentation.api;

import com.janjitemu.app.application.dto.appointment.AppointmentCreateRequest;
import com.janjitemu.app.application.dto.appointment.AppointmentUpdateRequest;
import com.janjitemu.app.application.dto.common.MessageResponse;
import com.janjitemu.app.application.service.AppointmentService;
import com.janjitemu.app.domain.model.Appointment;
import com.janjitemu.app.infrastructure.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/appointments")
public class AppointmentApiController {

    @Autowired
    private AppointmentService appointmentService;

    @GetMapping
    public ResponseEntity<?> getAppointments(@AuthenticationPrincipal UserPrincipal currentUser) {
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Token invalid"));
        }

        try {
            if ("admin".equalsIgnoreCase(currentUser.getRole())) {
                List<Appointment> list = appointmentService.getAllAppointments();
                return ResponseEntity.ok(list);
            } else {
                List<Appointment> list = appointmentService.getAppointmentsByUserId(currentUser.getId());
                return ResponseEntity.ok(list);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Error"));
        }
    }

    @PostMapping
    public ResponseEntity<?> createAppointment(@RequestBody AppointmentCreateRequest request,
                                               @AuthenticationPrincipal UserPrincipal currentUser) {
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Token invalid"));
        }

        try {
            boolean isAdmin = "admin".equalsIgnoreCase(currentUser.getRole());
            String name = isAdmin ? request.getCustomerName() : currentUser.getUsername();
            String uId = isAdmin ? null : currentUser.getId();

            Date dateVal = appointmentService.parseFlexibleDate(request.getTanggal());
            appointmentService.createAppointment(uId, name, request.getLayanan(), dateVal, request.getWaktu(), "Pending");

            return ResponseEntity.ok(new MessageResponse("Berhasil booking"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Error"));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAppointment(@PathVariable String id,
                                               @RequestBody AppointmentUpdateRequest request,
                                               @AuthenticationPrincipal UserPrincipal currentUser) {
        if (currentUser == null || !"admin".equalsIgnoreCase(currentUser.getRole())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new MessageResponse("Akses Ditolak"));
        }

        try {
            Date dateVal = request.getTanggal() != null ? appointmentService.parseFlexibleDate(request.getTanggal()) : null;
            boolean updated = appointmentService.updateAppointment(
                    id,
                    request.getStatus(),
                    request.getLayanan(),
                    request.getWaktu(),
                    request.getCustomerName(),
                    dateVal
            );

            if (!updated) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Data janji tidak ditemukan"));
            }

            return ResponseEntity.ok(new MessageResponse("Data janji diperbarui"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Error"));
        }
    }
}
