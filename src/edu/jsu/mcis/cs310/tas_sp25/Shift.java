package edu.jsu.mcis.cs310.tas_sp25;

import java.util.*;
import java.time.*;
import java.time.format.DateTimeParseException;
import java.time.format.DateTimeFormatter;

public class Shift {
    
    private final String description;
    private final int shiftStart, shiftStop, lunchStart, lunchStop;
    private final int id, roundInterval, gracePeriod, dockPenalty, lunchThreshold;
    
    // Constructor accepting a HashMap
    public Shift(Map<String, String> shiftDetails) {
        this.id = parseInt(shiftDetails.get("id"));
        this.description = shiftDetails.getOrDefault("description", "");
        this.shiftStart = parseTimeToMinutes(shiftDetails.get("shiftstart"));
        this.shiftStop = parseTimeToMinutes(shiftDetails.get("shiftstop"));
        this.roundInterval = parseInt(shiftDetails.get("roundinterval"));
        this.gracePeriod = parseInt(shiftDetails.get("graceperiod"));
        this.dockPenalty = parseInt(shiftDetails.get("dockpenalty"));
        this.lunchStart = parseTimeToMinutes(shiftDetails.get("lunchstart"));
        this.lunchStop = parseTimeToMinutes(shiftDetails.get("lunchstop"));
        this.lunchThreshold = parseInt(shiftDetails.get("lunchthreshold"));
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

    public int getShiftStart() {
        return shiftStart;
    }

    public int getShiftStop() {
        return shiftStop;
    }

    public int getRoundInterval() {
        return roundInterval;
    }

    public int getGracePeriod() {
        return gracePeriod;
    }

    public int getDockPenalty() {
        return dockPenalty;
    }

    public int getLunchStart() {
        return lunchStart;
    }

    public int getLunchStop() {
        return lunchStop;
    }

    public int getLunchThreshold() {
        return lunchThreshold;
    }
}
