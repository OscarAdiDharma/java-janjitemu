package com.janjitemu.app.controller;

import com.janjitemu.app.dto.AppointmentCreateRequest;
import com.janjitemu.app.dto.AppointmentUpdateRequest;
import com.janjitemu.app.dto.MessageResponse;
import com.janjitemu.app.model.Appointment;
import com.janjitemu.app.repository.AppointmentRepository;
import com.janjitemu.app.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @GetMapping
    public ResponseEntity<?> getAppointments(@AuthenticationPrincipal UserPrincipal currentUser) {
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Token invalid"));
        }

        try {
            if ("admin".equalsIgnoreCase(currentUser.getRole())) {
                List<Appointment> list = appointmentRepository.findAllByOrderByTanggalAsc();
                return ResponseEntity.ok(list);
            } else {
                List<Appointment> list = appointmentRepository.findByUserId(currentUser.getId());
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

            Date dateVal = parseFlexibleDate(request.getTanggal());

            Appointment appointment = new Appointment(
                    uId,
                    name,
                    request.getLayanan(),
                    dateVal,
                    request.getWaktu(),
                    "Pending"
            );
            appointmentRepository.save(appointment);

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
            Optional<Appointment> opt = appointmentRepository.findById(id);
            if (opt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Data janji tidak ditemukan"));
            }

            Appointment appointment = opt.get();

            if (request.getStatus() != null) {
                appointment.setStatus(request.getStatus());
            }
            if (request.getLayanan() != null) {
                appointment.setLayanan(request.getLayanan());
            }
            if (request.getWaktu() != null) {
                appointment.setWaktu(request.getWaktu());
            }
            if (request.getCustomerName() != null) {
                appointment.setCustomerName(request.getCustomerName());
            }
            if (StringUtils.hasText(request.getTanggal())) {
                appointment.setTanggal(parseFlexibleDate(request.getTanggal()));
            }

            appointmentRepository.save(appointment);

            return ResponseEntity.ok(new MessageResponse("Data janji diperbarui"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Error"));
        }
    }

    private Date parseFlexibleDate(String dateStr) {
        if (!StringUtils.hasText(dateStr)) {
            return new Date();
        }
        try {
            if (dateStr.length() == 10 && dateStr.charAt(4) == '-' && dateStr.charAt(7) == '-') {
                LocalDate localDate = LocalDate.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE);
                return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            }
            if (dateStr.contains("T")) {
                SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                return isoFormat.parse(dateStr.substring(0, Math.min(dateStr.length(), 19)));
            }
            SimpleDateFormat fallbackFormat = new SimpleDateFormat("yyyy-MM-dd");
            return fallbackFormat.parse(dateStr);
        } catch (Exception e) {
            return new Date();
        }
    }
}
