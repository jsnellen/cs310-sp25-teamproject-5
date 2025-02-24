package edu.jsu.mcis.cs310.tas_sp25;

import java.util.*;
import java.time.*;
import java.time.format.DateTimeParseException;
import java.time.format.DateTimeFormatter;

public class Shift {
    
    private final String description;
    private final LocalTime shiftstart, shiftstop, lunchstart, lunchstop;
    private final int id, roundinterval, graceperiod, dockpenalty, lunchthreshold, lunchduration, shiftduration;
    
    // Constructor accepting a HashMap
    public Shift(Map<String, String> shiftDetails) {
        this.id = parseInt(shiftDetails.get("id"));
        this.description = shiftDetails.getOrDefault("description", "");
        this.shiftstart = parseTime(shiftDetails.get("shiftstart"));
        this.shiftstop = parseTime(shiftDetails.get("shiftstop"));
        this.roundinterval = parseInt(shiftDetails.get("roundinterval"));
        this.graceperiod = parseInt(shiftDetails.get("graceperiod"));
        this.dockpenalty = parseInt(shiftDetails.get("dockpenalty"));
        this.lunchstart = parseTime(shiftDetails.get("lunchstart"));
        this.lunchstop = parseTime(shiftDetails.get("lunchstop"));
        this.lunchthreshold = parseInt(shiftDetails.get("lunchthreshold"));
        this.lunchduration = parseTimeToMinutes(shiftDetails.get("lunchduration"));
        this.shiftduration = parseTimeToMinutes(shiftDetails.get("shiftduration"));
    }
    
    // Helper method to parse integer values safely
    private int parseInt(String value) {
    if (value == null || value.isEmpty()) {
        return 0;
    }
    try {
        return Integer.parseInt(value);
    } catch (NumberFormatException e) {
        return 0; // Default value in case of invalid input
    }
    }
    
    // Helper method to parse time values safely
    private LocalTime parseTime(String time) {
        if (time == null || time.isEmpty()) {
            return null;
        }
        try {
            return LocalTime.parse(time);
        } catch (DateTimeParseException e) {
            return null; // Handle invalid time formats
        }
    }
    
    // Helper method to parse time values and convert to minutes
    private int parseTimeToMinutes(String time) {
        if (time == null || time.isEmpty()) {
            return 0; // Default value for missing time
        }
        try {
            LocalTime parsedTime = LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm"));
            return parsedTime.getHour() * 60 + parsedTime.getMinute();
        } catch (DateTimeParseException e) {
            return 0; // Default to 0 if parsing fails
        }
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public LocalTime getShiftStart() {
        return shiftstart;
    }

    public LocalTime getShiftStop() {
        return shiftstop;
    }

    public int getRoundInterval() {
        return roundinterval;
    }

    public int getGracePeriod() {
        return graceperiod;
    }

    public int getDockPenalty() {
        return dockpenalty;
    }

    public LocalTime getLunchStart() {
        return lunchstart;
    }

    public LocalTime getLunchStop() {
        return lunchstop;
    }

    public int getLunchThreshold() {
        return lunchthreshold;
    }
    
    public int getLunchDuration() {
        return lunchduration;
    }
    
    public int getShiftDuration() {
        return shiftduration;
    }
}