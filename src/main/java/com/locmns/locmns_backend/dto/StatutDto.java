package com.locmns.locmns_backend.dto;

import java.time.LocalDate;

public class StatutDto {

    private Integer statutId;
    private String code;
    private LocalDate date;

    public Integer getStatutId() { return statutId; }
    public void setStatutId(Integer statutId) { this.statutId = statutId; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
}