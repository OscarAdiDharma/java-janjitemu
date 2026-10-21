package com.janjitemu.app.controller;

import com.janjitemu.app.model.Appointment;
import com.janjitemu.app.model.ServiceItem;
import com.janjitemu.app.repository.AppointmentRepository;
import com.janjitemu.app.repository.ServiceRepository;
import com.janjitemu.app.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/customer")
@PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
public class WebCustomerController {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @GetMapping
    public String customerDashboard(@AuthenticationPrincipal UserPrincipal user,
                                    @RequestParam(value = "success", required = false) String success,
                                    Model model) {
        List<Appointment> userAppointments = appointmentRepository.findByUserId(user.getId());
        List<ServiceItem> services = serviceRepository.findAll();

        model.addAttribute("currentUser", user);
        model.addAttribute("appointments", userAppointments);
        model.addAttribute("services", services);

        if ("booked".equals(success)) {
            model.addAttribute("successMsg", "Permohonan janji temu Anda berhasil dikirim! Menunggu konfirmasi admin.");
        }

        return "customer/dashboard";
    }

    @PostMapping("/appointments/create")
    public String createAppointment(@AuthenticationPrincipal UserPrincipal user,
                                    @RequestParam("layanan") String layanan,
                                    @RequestParam("tanggal") String tanggal,
                                    @RequestParam(value = "waktu", required = false) String waktu) {
        Appointment appointment = new Appointment(
                user.getId(),
                user.getUsername(),
                layanan,
                parseDate(tanggal),
                waktu,
                "Pending"
        );
        appointmentRepository.save(appointment);

        return "redirect:/customer?success=booked";
    }

    private Date parseDate(String dateStr) {
        try {
            if (dateStr.length() == 10 && dateStr.charAt(4) == '-' && dateStr.charAt(7) == '-') {
                LocalDate localDate = LocalDate.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE);
                return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return sdf.parse(dateStr);
        } catch (Exception e) {
            return new Date();
        }
    }
}
