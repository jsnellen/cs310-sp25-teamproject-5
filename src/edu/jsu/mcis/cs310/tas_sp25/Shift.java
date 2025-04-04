package edu.jsu.mcis.cs310.tas_sp25;

import java.time.*;
import java.util.*;

/**
 * Represents a work shift with details such as start and stop times, lunch breaks, 
 * and associated penalties and thresholds.
 */
public class Shift {
    
    /** Description of the shift */
    private final String description;
    
    /** Unique identifier for the shift */
    private Integer id;
    /** Shift start time */
    private LocalTime shiftstart;
    /** Shift stop time */
    private LocalTime shiftstop;
    /** Interval for rounding clock-ins and outs */
    private Integer roundinterval;
    /** Grace period for late arrivals */
    private Integer graceperiod;
    /** Penalty for late arrivals */
    private Integer dockpenalty;
    /** Lunch start time */
    private LocalTime lunchstart;
    /** Lunch stop time */
    private LocalTime lunchstop;
    /** Minimum shift duration before lunch is required */
    private Integer lunchthreshold;
    /** Duration of the lunch break */
    private Duration lunchduration;
    /** Total duration of the shift */
    private Duration shiftduration;

    /**
     * Constructs a Shift object using details provided in a HashMap.
     * 
     * @param ShiftDetail A HashMap containing shift details
     */
    public Shift(HashMap<String, String> ShiftDetail) {
        
        this.id = Integer.valueOf(ShiftDetail.get("id"));
        this.description = ShiftDetail.get("description");
        this.shiftstart = LocalTime.parse(ShiftDetail.get("shiftStart"));
        this.shiftstop = LocalTime.parse(ShiftDetail.get("shiftStop"));
        this.roundinterval = Integer.valueOf(ShiftDetail.get("roundInterval"));
        this.graceperiod = Integer.valueOf(ShiftDetail.get("gracePeriod"));
        this.dockpenalty = Integer.valueOf(ShiftDetail.get("dockPenalty"));
        this.lunchstart = LocalTime.parse(ShiftDetail.get("lunchStart"));
        this.lunchstop = LocalTime.parse(ShiftDetail.get("lunchStop"));
        this.lunchthreshold = Integer.valueOf(ShiftDetail.get("lunchThreshold"));
        
        this.lunchduration = Duration.between(lunchstart, lunchstop); 
        
        // Handle shifts that cross midnight
        if (Duration.between(shiftstart, shiftstop).isNegative()) {
            this.shiftduration = Duration.between(shiftstart, shiftstop).plusDays(1);
        } else { 
            this.shiftduration = Duration.between(shiftstart, shiftstop);
        } 
    }

    /**
     * @return the shift ID
     */
    public Integer getId() {
        return id;
    }

    /**
     * @return the shift description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return the shift start time
     */
    public LocalTime getShiftStart() {
        return shiftstart;
    }

    /**
     * @return the shift stop time
     */
    public LocalTime getShiftStop() {
        return shiftstop;
    }

    /**
     * @return the rounding interval for shift times
     */
    public Integer getRoundInterval() {
        return roundinterval;
    }

    /**
     * @return the grace period duration
     */
    public Integer getGracePeriod() {
        return graceperiod;
    }

    /**
     * @return the dock penalty duration
     */
    public Integer getDockPenalty() {
        return dockpenalty;
    }

    /**
     * @return the lunch start time
     */
    public LocalTime getLunchStart() {
        return lunchstart;
    }

    /**
     * @return the lunch stop time
     */
    public LocalTime getLunchStop() {
        return lunchstop;
    }

    /**
     * @return the minimum shift duration required for lunch
     */
    public Integer getLunchThreshold() {
        return lunchthreshold;
    }

    /**
     * @return the duration of the lunch break
     */
    public Duration getLunchDuration() {
        return lunchduration;
    }

    /**
     * @return the total duration of the shift
     */
    public Duration getShiftDuration() {
        return shiftduration;
    }
    
    /**
     * Determines if a given day is a workday.
     * Assumes the workweek runs Monday through Friday.
     *
     * @param day The day of the week to check
     * @return true if the day is a workday, false otherwise
     */
    public boolean isWorkDay(DayOfWeek day) {
        return !day.equals(DayOfWeek.SATURDAY) && !day.equals(DayOfWeek.SUNDAY);
    }

    /**
     * Returns a string representation of the shift, including its details.
     * 
     * @return A formatted string describing the shift
     */
    @Override
    public String toString() {
        StringBuilder build = new StringBuilder();
           
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
