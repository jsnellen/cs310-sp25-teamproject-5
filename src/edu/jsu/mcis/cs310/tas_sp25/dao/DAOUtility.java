package edu.jsu.mcis.cs310.tas_sp25.dao;

import java.time.*;
import java.util.*;
import java.time.temporal.ChronoUnit;
import java.time.format.DateTimeFormatter;
import com.github.cliftonlabs.json_simple.*;
import edu.jsu.mcis.cs310.tas_sp25.EventType;
import edu.jsu.mcis.cs310.tas_sp25.Punch;
import edu.jsu.mcis.cs310.tas_sp25.Shift;


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

        for (Punch punch : dailypunchlist) {
            EventType eventType = punch.getPunchtype();
            LocalDateTime punchTimestamp = punch.getAdjustedTimestamp();

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
                if (shiftDurationMinutes > shift.getLunchThreshold()) {
                    lunchDeductible = true;
                }

                // If the employee is clocking out and lunch is deductible, subtract lunch duration
                if (eventType == EventType.CLOCK_OUT && lunchDeductible) {
                    
                    long lunchDurationMinutes = shift.getLunchDuration().toMinutes();
                    
                    if (shiftDurationMinutes > lunchDurationMinutes) {
                        totalWorkedMinutes += shiftDurationMinutes - lunchDurationMinutes;
                        
                    } else {
                        
                        totalWorkedMinutes += shiftDurationMinutes;
                    }
                    
                } else {
                    
                    totalWorkedMinutes += shiftDurationMinutes;
                    
                }

                // Reset shift start and stop times for the next shift
                shiftStart = null;
                shiftStop = null;
                lunchDeductible = false;
            }
        }

        return (int) totalWorkedMinutes;
    }
}


    
