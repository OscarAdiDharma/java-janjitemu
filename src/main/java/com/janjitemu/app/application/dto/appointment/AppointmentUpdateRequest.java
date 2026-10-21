package com.janjitemu.app.application.dto.appointment;

public class AppointmentUpdateRequest {
    private String status;
    private String tanggal;
    private String layanan;
    private String waktu;
    private String customerName;

    public AppointmentUpdateRequest() {
    }

    public AppointmentUpdateRequest(String status, String tanggal, String layanan, String waktu, String customerName) {
        this.status = status;
        this.tanggal = tanggal;
        this.layanan = layanan;
        this.waktu = waktu;
        this.customerName = customerName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getLayanan() {
        return layanan;
    }

    public void setLayanan(String layanan) {
        this.layanan = layanan;
    }

    public String getWaktu() {
        return waktu;
    }

    public void setWaktu(String waktu) {
        this.waktu = waktu;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
}
