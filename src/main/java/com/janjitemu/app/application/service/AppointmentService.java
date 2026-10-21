package com.janjitemu.app.application.service;

import com.janjitemu.app.domain.model.Appointment;
import com.janjitemu.app.domain.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAllByOrderByTanggalAsc();
    }

    public List<Appointment> getAppointmentsByUserId(String userId) {
        return appointmentRepository.findByUserId(userId);
    }

    public List<Appointment> getTodayAppointments() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String todayStr = sdf.format(new Date());

        return appointmentRepository.findAllByOrderByTanggalAsc().stream()
                .filter(a -> a.getTanggal() != null && sdf.format(a.getTanggal()).equals(todayStr))
                .collect(Collectors.toList());
    }

    public Appointment createAppointment(String userId, String customerName, String layanan, Date tanggal, String waktu, String status) {
        Appointment appointment = new Appointment(
                userId,
                customerName != null ? customerName.trim() : null,
                layanan,
                tanggal,
                waktu,
                status != null ? status : "Pending"
        );
        return appointmentRepository.save(appointment);
    }

    public boolean updateStatus(String id, String status) {
        Optional<Appointment> opt = appointmentRepository.findById(id);
        if (opt.isPresent()) {
            Appointment a = opt.get();
            a.setStatus(status);
            appointmentRepository.save(a);
            return true;
        }
        return false;
    }

    public boolean updateAppointment(String id, String status, String layanan, String waktu, String customerName, Date tanggal) {
        Optional<Appointment> opt = appointmentRepository.findById(id);
        if (opt.isPresent()) {
            Appointment a = opt.get();
            if (StringUtils.hasText(status)) a.setStatus(status);
            if (StringUtils.hasText(layanan)) a.setLayanan(layanan);
            if (StringUtils.hasText(waktu)) a.setWaktu(waktu);
            if (StringUtils.hasText(customerName)) a.setCustomerName(customerName);
            if (tanggal != null) a.setTanggal(tanggal);
            appointmentRepository.save(a);
            return true;
        }
        return false;
    }

    public Optional<Appointment> findById(String id) {
        return appointmentRepository.findById(id);
    }

    public Date parseFlexibleDate(String dateStr) {
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
