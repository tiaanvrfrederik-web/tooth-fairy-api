package com.example.tooth_fairy.brushroutine.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalTime;

public class BrushRoutineRequest {

    @NotBlank
    @Size(max = 100)
    private String personName;

    @Min(1)
    @Max(12)
    private int timesPerDay = 2;

    private LocalTime morningTime;
    private LocalTime eveningTime;

    @Size(max = 500)
    private String notes;

    private Boolean active;

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public int getTimesPerDay() {
        return timesPerDay;
    }

    public void setTimesPerDay(int timesPerDay) {
        this.timesPerDay = timesPerDay;
    }

    public LocalTime getMorningTime() {
        return morningTime;
    }

    public void setMorningTime(LocalTime morningTime) {
        this.morningTime = morningTime;
    }

    public LocalTime getEveningTime() {
        return eveningTime;
    }

    public void setEveningTime(LocalTime eveningTime) {
        this.eveningTime = eveningTime;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
