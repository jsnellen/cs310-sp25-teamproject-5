package edu.jsu.mcis.cs310.tas_sp25.dao;

import java.time.*;
import java.util.*;
import java.time.temporal.ChronoUnit;
import java.time.format.DateTimeFormatter;
import com.github.cliftonlabs.json_simple.*;
import edu.jsu.mcis.cs310.tas_sp25.EventType;
import edu.jsu.mcis.cs310.tas_sp25.Punch;
import edu.jsu.mcis.cs310.tas_sp25.Shift;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.time.DayOfWeek;
import java.time.LocalDate;



/**
 * 
 * Utility class for DAOs.  This is a final, non-constructable class containing
 * common DAO logic and other repeated and/or standardized code, refactored into
 * individual static methods.
 * 
 */
public final class DAOUtility {
    
    public static String getPunchListAsJSON(ArrayList<Punch> dailypunchlist) {
   
        // Create an ArrayList for the punch data
        ArrayList<HashMap<String, String>> jsonData = new ArrayList<>();
        // Iterate throught the list of punches
        for (Punch punch : dailypunchlist) {
            // Create a Hashmap to store the data for the current punch
            HashMap<String, String> punchData = new HashMap<>();
       
            // Add the punch data to the Hashmap
            punchData.put("id", String.valueOf(punch.getId()));
            punchData.put("badgeid", punch.getBadgeId());
            punchData.put("terminalid", String.valueOf(punch.getTerminalid()));
            punchData.put("punchtype", punch.getPunchtype().toString());
            punchData.put("adjustmenttype", punch.getAdjustmentType().toString());

            punchData.put("originaltimestamp", punch.toString());
            punchData.put("adjustedtimestamp", punch.adjustedToString());
       
            // Append the HashMap to the ArrayList
            jsonData.add(punchData);
        }
    
        // Serialize the ArrayList to a JSON String
        String json = Jsoner.serialize(jsonData);
    
        // Return the JSON String
        return json;
    
    }

        public static int calculateTotalMinutes(ArrayList<Punch> dailypunchlist, Shift shift) {
        LocalDateTime shiftStart = null;
        LocalDateTime shiftStop = null;
        boolean lunchDeductible = false;
        long totalWorkedMinutes = 0;

        ArrayList<Integer> testing = new ArrayList<Integer>();

        for (Punch punch : dailypunchlist) {
            EventType eventType = punch.getPunchtype();
            LocalDateTime punchTimestamp = punch.getAdjustedTimestamp();

            System.err.println("Adjusted time stamp: " + punchTimestamp);

            switch (eventType) {
                case CLOCK_IN:
                    // Set the shift start time when a clock-in punch is encountered
                    shiftStart = punchTimestamp;
                    // Reset shift stop time
                    shiftStop = null; 
                    // Reset lunch deductible
                    lunchDeductible = false; 
                    break;

                case CLOCK_OUT:
                    // Set the shift stop time when a clock-out punch is encountered
                    shiftStop = punchTimestamp;
                    break;

                case TIME_OUT:
                    // Ignore time-out punches and reset shift stop time
                    shiftStop = null;
                    break;
                default:
                    break;
            }

            // If both shift start and stop times are set, calculate the duration
            if (shiftStart != null && shiftStop != null) {
                long shiftDurationMinutes = Duration.between(shiftStart, shiftStop).toMinutes();

                // Check if the shift duration exceeds the lunch threshold
                if (shiftDurationMinutes - shift.getLunchDuration().toMinutes() > shift.getLunchThreshold()) {
                    lunchDeductible = true;
                }

                // If the employee is clocking out and lunch is deductible, subtract lunch duration
                if (eventType == EventType.CLOCK_OUT && lunchDeductible) {
                    
                    long lunchDurationMinutes = shift.getLunchDuration().toMinutes();
                    
                    if (shiftDurationMinutes > lunchDurationMinutes) {
                        totalWorkedMinutes += shiftDurationMinutes - lunchDurationMinutes;
                        testing.add((int)shiftDurationMinutes - (int)lunchDurationMinutes);
                        
                    } else {
                        
                        totalWorkedMinutes += shiftDurationMinutes;
                        testing.add((int)shiftDurationMinutes);
                    }
                    
                } else {
                    
                    totalWorkedMinutes += shiftDurationMinutes;
                    testing.add((int)shiftDurationMinutes);
                    
                }

                // Reset shift start and stop times for the next shift
                shiftStart = null;
                shiftStop = null;
                lunchDeductible = false;
            }
        }

        //System.err.println("Worked Minutes" + totalWorkedMinutes);
        System.err.println("Worked Minutes: " + testing);
        return (int) totalWorkedMinutes;
 
        }
       
        public static BigDecimal calculateAbsenteeism(ArrayList<Punch> punchList, Shift shift) {
        if (punchList == null || punchList.isEmpty() || shift == null) {

            System.err.println("Method calculateAbsenteeism has null input.");

            return BigDecimal.ZERO;
        }

        // Calculate total minutes worked in the pay period
        //long totalWorkedMinutes = getTotalMinutesWorked(punchList, shift);
        long totalWorkedMinutes = calculateTotalMinutes(punchList, shift);

        //System.err.println("Total Minutes Worked: " + totalWorkedMinutes);

        // Calculate total scheduled minutes in the pay period
        long totalScheduledMinutes = getScheduledMinutes(shift, punchList);

        System.err.println("Sheduled and worked minutes: " + totalScheduledMinutes + " " + totalWorkedMinutes);

        // Ensure valid calculations
        if (totalScheduledMinutes <= 0) {

            System.err.println("Method calculateAbsenteeism has returned less than 0.");

            return BigDecimal.ZERO;
        }

        // Absenteeism = (Scheduled - Worked) / Scheduled * 100 2520
        BigDecimal absenteeism = new BigDecimal(totalScheduledMinutes - totalWorkedMinutes)
                .divide(new BigDecimal(totalScheduledMinutes).setScale(4, RoundingMode.HALF_UP))
                .multiply(new BigDecimal(100).setScale(4, RoundingMode.HALF_UP));
       
        return absenteeism;
    }

    /* 
    private static long getTotalMinutesWorked(ArrayList<Punch> punchList, Shift shift) {
        long totalMinutes = 0;

        ArrayList<Integer> testing = new ArrayList<Integer>();
        
        for (Punch punch : punchList) {

            //System.err.println("Inside getTotalMinutesWorked for loop: " + punch.getMinutesWorked(shift));
            totalMinutes += punch.getMinutesWorked(shift);  // Ensure Punch has this method
            testing.add(punch.getMinutesWorked(shift));
        }

        System.err.println(testing);
        System.err.println("getTotalMinutesWorked return value: " + totalMinutes);
        return totalMinutes;
    }
    */
    
    private static long getScheduledMinutes(Shift shift, ArrayList<Punch> punchList) {
        LocalDate startDate = punchList.get(0).getAdjustedTimestamp().toLocalDate()
                .with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
        LocalDate endDate = startDate.with(TemporalAdjusters.next(DayOfWeek.SATURDAY));



        long workingDays = 0;
        long shiftDurationMinutes = shift.getShiftDuration().toMinutes();
        long lunchDurationMinutes = shift.getLunchDuration().toMinutes();
        long scheduledMinutes = shiftDurationMinutes - lunchDurationMinutes;

        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            if (shift.isWorkDay(date.getDayOfWeek())) {  // Assuming Shift has isWorkDay() method
                workingDays++;
            }
        }

        return workingDays * scheduledMinutes;
    }
}
          
