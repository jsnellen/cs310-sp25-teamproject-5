package edu.jsu.mcis.cs310.tas_sp25;

import java.util.*;
import java.time.*;
import java.time.format.DateTimeParseException;
import java.time.format.DateTimeFormatter;

public class Shift {
    
    //Instance fields to hold shift data
    private final String description;
    
    private Integer id;
    private LocalTime shiftstart;
    private LocalTime shiftstop;
    private Integer roundinterval;
    private Integer graceperiod;
    private Integer dockpenalty;
    private LocalTime lunchstart;
    private LocalTime lunchstop;
    private Integer lunchthreshold;
    private Duration lunchduration;
    private Duration shiftduration;

    
    // Constructor that takes a HashMap to initialize all fields
    public Shift(HashMap<String, String> ShiftDetail) {
        
        // Parsing and assigning values from the HashMap
        this.id = Integer.valueOf((String) ShiftDetail.get("id"));
        this.description = (String) ShiftDetail.get("description");
        this.shiftstart = LocalTime.parse((String) ShiftDetail.get("shiftStart"));
        this.shiftstop = LocalTime.parse((String) ShiftDetail.get("shiftStop"));
        this.roundinterval = Integer.valueOf((String) ShiftDetail.get("roundInterval"));
        this.graceperiod = Integer.valueOf((String) ShiftDetail.get("gracePeriod"));
        this.dockpenalty = Integer.valueOf((String) ShiftDetail.get("dockPenalty"));
        this.lunchstart = LocalTime.parse((String) ShiftDetail.get("lunchStart"));
        this.lunchstop = LocalTime.parse((String) ShiftDetail.get("lunchStop"));
        this.lunchthreshold = Integer.valueOf((String) ShiftDetail.get("lunchThreshold"));
        // The lunch duration is calculated by finding the difference between the lunch start and stop times
        this.lunchduration = Duration.between(lunchstart,lunchstop); 
        
        // Check for time duration between differant dates
        if (Duration.between(shiftstart,shiftstop).isNegative()) {
            
            this.shiftduration = Duration.between(shiftstart,shiftstop).plusDays(1);
            
        } else { 
            
            this.shiftduration = Duration.between(shiftstart,shiftstop);
            
        } 
        
    }

        // Getters methods

        public Integer getId() {
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

        public Integer getRoundInterval() {
            return roundinterval;
        }

        public Integer getGracePeriod() {
            return graceperiod;
        }

        public Integer getDockPenalty() {
        return dockpenalty;
        }

        public LocalTime getLunchStart() {
            return lunchstart;
        }

        public LocalTime getLunchStop() {
            return lunchstop;
        }

        public Integer getLunchThreshold() {
            return lunchthreshold;
        }

        public Duration getLunchDuration() {
            return lunchduration;
        }
    
        public Duration getShiftDuration() {
            return shiftduration;
        }

        // Override the toString() method to display shift details
        @Override
        public String toString() {
        StringBuilder build = new StringBuilder();
           
        // Append shift details to the StringBuilder
        build.append(description).append(": ")
               .append(shiftstart).append(" - ")
               .append(shiftstop).append(" (")
               .append(shiftduration.toMinutes()).append(" minutes); Lunch: ")
               .append(lunchstart).append(" - ")
               .append(lunchstop).append(" (")
               .append(lunchduration.toMinutes()).append(" minutes)");


        return build.toString();

    }
}
