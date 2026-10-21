package com.janjitemu.app.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "appointments")
public class Appointment {

    @Id
    private String id;

    private String userId;

    private String customerName;

    private String layanan;

    private Date tanggal;

    private String waktu;

    private String status = "Pending";

    public Appointment() {
    }

    public Appointment(String userId, String customerName, String layanan, Date tanggal, String waktu, String status) {
        this.userId = userId;
        this.customerName = customerName;
        this.layanan = layanan;
        this.tanggal = tanggal;
        this.waktu = waktu;
        this.status = (status != null) ? status : "Pending";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("_id")
    public String getUnderscoreId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getLayanan() {
        return layanan;
    }

    public void setLayanan(String layanan) {
        this.layanan = layanan;
    }

    public Date getTanggal() {
        return tanggal;
    }

    public void setTanggal(Date tanggal) {
        this.tanggal = tanggal;
    }

    public String getWaktu() {
        return waktu;
    }

    public void setWaktu(String waktu) {
        this.waktu = waktu;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
