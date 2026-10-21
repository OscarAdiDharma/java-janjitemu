package com.janjitemu.app.application.dto.appointment;

public class AppointmentCreateRequest {
    private String layanan;
    private String tanggal;
    private String customerName;
    private String waktu;

    public AppointmentCreateRequest() {
    }

    public AppointmentCreateRequest(String layanan, String tanggal, String customerName, String waktu) {
        this.layanan = layanan;
        this.tanggal = tanggal;
        this.customerName = customerName;
        this.waktu = waktu;
    }

    public String getLayanan() {
        return layanan;
    }

    public void setLayanan(String layanan) {
        this.layanan = layanan;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getWaktu() {
        return waktu;
    }

    public void setWaktu(String waktu) {
        this.waktu = waktu;
    }
}
