package com.janjitemu.app.presentation.web;

import com.janjitemu.app.application.service.AppointmentService;
import com.janjitemu.app.application.service.LegalServiceService;
import com.janjitemu.app.domain.model.Appointment;
import com.janjitemu.app.domain.model.ServiceItem;
import com.janjitemu.app.infrastructure.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/customer")
@PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
public class WebCustomerController {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private LegalServiceService legalServiceService;

    @GetMapping
    public String customerDashboard(@AuthenticationPrincipal UserPrincipal user,
                                    @RequestParam(value = "success", required = false) String success,
                                    Model model) {
        List<Appointment> userAppointments = appointmentService.getAppointmentsByUserId(user.getId());
        List<ServiceItem> services = legalServiceService.getAllServices();

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
        Date parsedDate = appointmentService.parseFlexibleDate(tanggal);
        appointmentService.createAppointment(
                user.getId(),
                user.getUsername(),
                layanan,
                parsedDate,
                waktu,
                "Pending"
        );

        return "redirect:/customer?success=booked";
    }
}
