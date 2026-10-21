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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class WebAdminController {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private LegalServiceService legalServiceService;

    @GetMapping
    public String adminDashboard(@AuthenticationPrincipal UserPrincipal user,
                                 @RequestParam(value = "tab", defaultValue = "dashboard") String tab,
                                 @RequestParam(value = "success", required = false) String success,
                                 Model model) {
        List<Appointment> allAppointments = appointmentService.getAllAppointments();
        List<Appointment> todayAppointments = appointmentService.getTodayAppointments();
        List<ServiceItem> services = legalServiceService.getAllServices();

        SimpleDateFormat ymdFormat = new SimpleDateFormat("yyyy-MM-dd");
        String todayStr = ymdFormat.format(new Date());

        long pendingCount = allAppointments.stream().filter(a -> "Pending".equalsIgnoreCase(a.getStatus())).count();
        long approvedCount = allAppointments.stream().filter(a -> "Approved".equalsIgnoreCase(a.getStatus())).count();
        long completedCount = allAppointments.stream().filter(a -> "Completed".equalsIgnoreCase(a.getStatus())).count();

        model.addAttribute("currentUser", user);
        model.addAttribute("activeTab", tab);
        model.addAttribute("appointments", allAppointments);
        model.addAttribute("todayAppointments", todayAppointments);
        model.addAttribute("services", services);
        model.addAttribute("todayStr", todayStr);

        // Stats
        model.addAttribute("totalCount", allAppointments.size());
        model.addAttribute("todayCount", todayAppointments.size());
        model.addAttribute("pendingCount", pendingCount);
        model.addAttribute("approvedCount", approvedCount);
        model.addAttribute("completedCount", completedCount);

        if ("status_updated".equals(success)) {
            model.addAttribute("successMsg", "Status janji temu berhasil diperbarui!");
        } else if ("booked".equals(success)) {
            model.addAttribute("successMsg", "Janji temu baru berhasil dijadwalkan!");
        } else if ("service_added".equals(success)) {
            model.addAttribute("successMsg", "Layanan baru berhasil ditambahkan!");
        } else if ("service_updated".equals(success)) {
            model.addAttribute("successMsg", "Data layanan berhasil diperbarui!");
        } else if ("service_deleted".equals(success)) {
            model.addAttribute("successMsg", "Layanan berhasil dihapus!");
        }

        return "admin/dashboard";
    }

    @PostMapping("/appointments/status")
    public String updateStatus(@RequestParam("id") String id,
                               @RequestParam("status") String status,
                               @RequestParam(value = "redirectTab", defaultValue = "dashboard") String redirectTab) {
        appointmentService.updateStatus(id, status);
        return "redirect:/admin?tab=" + redirectTab + "&success=status_updated";
    }

    @PostMapping("/appointments/edit")
    public String editAppointment(@RequestParam("id") String id,
                                  @RequestParam(value = "status", required = false) String status,
                                  @RequestParam(value = "tanggal", required = false) String tanggal,
                                  @RequestParam(value = "layanan", required = false) String layanan,
                                  @RequestParam(value = "waktu", required = false) String waktu,
                                  @RequestParam(value = "customerName", required = false) String customerName,
                                  @RequestParam(value = "redirectTab", defaultValue = "all-apps") String redirectTab) {
        Date parsedDate = tanggal != null ? appointmentService.parseFlexibleDate(tanggal) : null;
        appointmentService.updateAppointment(id, status, layanan, waktu, customerName, parsedDate);
        return "redirect:/admin?tab=" + redirectTab + "&success=status_updated";
    }

    @PostMapping("/appointments/create")
    public String createBookingAdmin(@RequestParam("customerName") String customerName,
                                     @RequestParam("layanan") String layanan,
                                     @RequestParam("tanggal") String tanggal,
                                     @RequestParam(value = "waktu", required = false) String waktu) {
        Date parsedDate = appointmentService.parseFlexibleDate(tanggal);
        appointmentService.createAppointment(null, customerName, layanan, parsedDate, waktu, "Approved");
        return "redirect:/admin?tab=all-apps&success=booked";
    }

    @PostMapping("/services/create")
    public String createService(@RequestParam("nama") String nama,
                                @RequestParam("deskripsi") String deskripsi) {
        legalServiceService.createService(nama, deskripsi);
        return "redirect:/admin?tab=services&success=service_added";
    }

    @PostMapping("/services/update")
    public String updateService(@RequestParam("id") String id,
                                @RequestParam("nama") String nama,
                                @RequestParam("deskripsi") String deskripsi) {
        legalServiceService.updateService(id, nama, deskripsi);
        return "redirect:/admin?tab=services&success=service_updated";
    }

    @PostMapping("/services/delete")
    public String deleteService(@RequestParam("id") String id) {
        legalServiceService.deleteService(id);
        return "redirect:/admin?tab=services&success=service_deleted";
    }
}
