package com.example.appbeauty.models;

import java.util.Date;

public class Schedule {

    private long id;
    private String clientName;
    private String service;
    private double value;
    private Date date;
    private int length;
    private int status;
    private long professionalId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getService() { return service; }

    public void setService(String service) { this.service = service; }

    public double getValue() { return value; }

    public void setValue(double value) { this.value = value; }

    public Date getDate() { return date; }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getLength() { return length; }

    public void setLength(int length) { this.length = length; }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getProfessionalId() {
        return professionalId;
    }

    public void setProfessionalId(long professionalId) {
        this.professionalId = professionalId;
    }
}