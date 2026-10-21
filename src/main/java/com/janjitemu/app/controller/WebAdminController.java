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
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class WebAdminController {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @GetMapping
    public String adminDashboard(@AuthenticationPrincipal UserPrincipal user,
                                 @RequestParam(value = "tab", defaultValue = "dashboard") String tab,
                                 @RequestParam(value = "success", required = false) String success,
                                 Model model) {
        List<Appointment> allAppointments = appointmentRepository.findAllByOrderByTanggalAsc();
        List<ServiceItem> services = serviceRepository.findAll();

        // Calculate today's date in yyyy-MM-dd
        SimpleDateFormat ymdFormat = new SimpleDateFormat("yyyy-MM-dd");
        String todayStr = ymdFormat.format(new Date());

        List<Appointment> todayAppointments = allAppointments.stream()
                .filter(a -> a.getTanggal() != null && ymdFormat.format(a.getTanggal()).equals(todayStr))
                .collect(Collectors.toList());

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
        Optional<Appointment> opt = appointmentRepository.findById(id);
        if (opt.isPresent()) {
            Appointment a = opt.get();
            a.setStatus(status);
            appointmentRepository.save(a);
        }
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
        Optional<Appointment> opt = appointmentRepository.findById(id);
        if (opt.isPresent()) {
            Appointment a = opt.get();
            if (StringUtils.hasText(status)) a.setStatus(status);
            if (StringUtils.hasText(layanan)) a.setLayanan(layanan);
            if (StringUtils.hasText(waktu)) a.setWaktu(waktu);
            if (StringUtils.hasText(customerName)) a.setCustomerName(customerName);
            if (StringUtils.hasText(tanggal)) {
                a.setTanggal(parseDate(tanggal));
            }
            appointmentRepository.save(a);
        }
        return "redirect:/admin?tab=" + redirectTab + "&success=status_updated";
    }

    @PostMapping("/appointments/create")
    public String createBookingAdmin(@RequestParam("customerName") String customerName,
                                     @RequestParam("layanan") String layanan,
                                     @RequestParam("tanggal") String tanggal,
                                     @RequestParam(value = "waktu", required = false) String waktu) {
        Appointment a = new Appointment(
                null,
                customerName.trim(),
                layanan,
                parseDate(tanggal),
                waktu,
                "Approved"
        );
        appointmentRepository.save(a);
        return "redirect:/admin?tab=all-apps&success=booked";
    }

    @PostMapping("/services/create")
    public String createService(@RequestParam("nama") String nama,
                                @RequestParam("deskripsi") String deskripsi) {
        ServiceItem item = new ServiceItem(nama.trim(), deskripsi.trim());
        serviceRepository.save(item);
        return "redirect:/admin?tab=services&success=service_added";
    }

    @PostMapping("/services/update")
    public String updateService(@RequestParam("id") String id,
                                @RequestParam("nama") String nama,
                                @RequestParam("deskripsi") String deskripsi) {
        Optional<ServiceItem> opt = serviceRepository.findById(id);
        if (opt.isPresent()) {
            ServiceItem item = opt.get();
            item.setNama(nama.trim());
            item.setDeskripsi(deskripsi.trim());
            serviceRepository.save(item);
        }
        return "redirect:/admin?tab=services&success=service_updated";
    }

    @PostMapping("/services/delete")
    public String deleteService(@RequestParam("id") String id) {
        serviceRepository.deleteById(id);
        return "redirect:/admin?tab=services&success=service_deleted";
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
