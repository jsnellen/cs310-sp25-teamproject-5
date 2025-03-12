package edu.jsu.mcis.cs310.tas_sp25;

import edu.jsu.mcis.cs310.tas_sp25.dao.BadgeDAO;
import edu.jsu.mcis.cs310.tas_sp25.dao.DAOFactory;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.time.LocalTime;
import java.time.DayOfWeek;

public class Punch {

    private final int id, terminalid, eventtypeid;
    private final String badgeid, timestamp;
    private final LocalDateTime ots;
    private final Badge badge;
    private final EventType eventtype;
    private PunchAdjustmentType adjustmenttype;
    private LocalDateTime adjustedtimestamp;
    private Shift shift;

    public Punch(HashMap<String, String> PunchDetail) {

        DAOFactory daoFactory = new DAOFactory("tas.jdbc");
        BadgeDAO badgeDAO = daoFactory.getBadgeDAO();

        this.id = Integer.valueOf((String) PunchDetail.get("id"));
        this.terminalid = Integer.valueOf((String) PunchDetail.get("terminalid"));
        this.badgeid = (String) PunchDetail.get("badgeid");
        this.timestamp = (String) PunchDetail.get("timestamp");
        this.eventtypeid = Integer.valueOf((String) PunchDetail.get("eventtypeid"));
        this.badge = badgeDAO.find(badgeid);
        this.ots = LocalDateTime.now();

        
        switch(eventtypeid) {
            case 0 -> this.eventtype = EventType.CLOCK_OUT;
            case 1 -> this.eventtype = EventType.CLOCK_IN;
            case 2 -> this.eventtype = EventType.TIME_OUT;
            default -> {
                this.eventtype = EventType.CLOCK_OUT;
            }
        }
    }


    public Punch(int i, Badge badge, EventType clockIn) {

        this.id = 0;
        this.terminalid = i;
        this.badge = badge;
        this.eventtypeid = clockIn.ordinal();
        this.badgeid = badge.getId();
        this.timestamp = null;
        this.ots = LocalDateTime.now();
        this.eventtype = clockIn;

    }


    public int getId() {
        return id;
    }

    public int getTerminalid() {
        return terminalid;
    }

    public String getBadgeId() {
        return badgeid;
    }

    public Badge getBadge() {
        return badge;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public LocalDateTime getOriginaltimestamp() {
        return ots;
    }
    
    //Jakolbe adding this because my Original gett
    
    public int getEventTypeId() {
        return eventtypeid;
    }

    public EventType getPunchtype() {
        return eventtype;
    }
    
    public PunchAdjustmentType getAdjustmentType(){
        return adjustmenttype;
    }
    
    
    public LocalDateTime getAdjustedTimestamp(){
        return adjustedtimestamp;
    }
   
    
    public void adjust(Shift shift) {
        adjustmenttype = null;
        adjustedtimestamp = null;
        boolean isWeekend = false;
        
        DayOfWeek day = getOriginaltimestamp().getDayOfWeek();
        
        if (day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY) {
            isWeekend = true;
        }

        int interval = shift.getRoundInterval();
        int grace = shift.getGracePeriod();
        int dock = shift.getDockPenalty();

        LocalTime shiftStart = shift.getShiftStart();
        LocalTime shiftStop = shift.getShiftStop();
        LocalTime lunchStart = shift.getLunchStart();
        LocalTime lunchStop = shift.getLunchStop();

        LocalDateTime shiftStartDateTime = getOriginaltimestamp().with(shiftStart);
        LocalDateTime shiftStopDateTime = getOriginaltimestamp().with(shiftStop);
        LocalDateTime lunchStartDateTime = getOriginaltimestamp().with(lunchStart);
        LocalDateTime lunchStopDateTime = getOriginaltimestamp().with(lunchStop);

        LocalDateTime shiftStartInterval = shiftStartDateTime.minusMinutes(interval);
        LocalDateTime shiftStartGrace = shiftStartDateTime.plusMinutes(grace);
        LocalDateTime shiftStartDock = shiftStartDateTime.plusMinutes(dock);

        LocalDateTime shiftStopInterval = shiftStopDateTime.plusMinutes(interval);
        LocalDateTime shiftStopGrace = shiftStopDateTime.minusMinutes(grace);
        LocalDateTime shiftStopDock = shiftStopDateTime.minusMinutes(dock);

        if (eventtype == EventType.CLOCK_IN) {
            if (getOriginaltimestamp().isAfter(shiftStartInterval.minusSeconds(1)) && getOriginaltimestamp().isBefore(shiftStartDateTime)) {
                adjustedtimestamp = shiftStartDateTime;
                adjustmenttype = PunchAdjustmentType.SHIFT_START;
                
            } else if (getOriginaltimestamp().isAfter(shiftStartDateTime) && getOriginaltimestamp().isBefore(shiftStartGrace)) {
                adjustedtimestamp = shiftStartDateTime;
                adjustmenttype = PunchAdjustmentType.SHIFT_START;
                
            } else if (getOriginaltimestamp().isAfter(shiftStartGrace) && getOriginaltimestamp().isBefore(shiftStartDock.plusSeconds(1))) {
                adjustedtimestamp = shiftStartDock;
                adjustmenttype = PunchAdjustmentType.SHIFT_DOCK;
                
            } else if (!isWeekend && getOriginaltimestamp().isAfter(lunchStartDateTime) && getOriginaltimestamp().isBefore(lunchStopDateTime)) {
                adjustedtimestamp = lunchStopDateTime;
                adjustmenttype = PunchAdjustmentType.LUNCH_STOP;
                
            }
        } else if (eventtype == EventType.CLOCK_OUT || eventtype == EventType.TIME_OUT) {
            if (getOriginaltimestamp().isAfter(shiftStopDateTime) && getOriginaltimestamp().isBefore(shiftStopInterval.plusSeconds(1))) {
                adjustedtimestamp = shiftStopDateTime;
                adjustmenttype = PunchAdjustmentType.SHIFT_STOP;
                
            } else if (getOriginaltimestamp().isBefore(shiftStopDateTime) && getOriginaltimestamp().isAfter(shiftStopGrace)) {
                adjustedtimestamp = shiftStopDateTime;
                adjustmenttype = PunchAdjustmentType.SHIFT_STOP;
                
            } else if (getOriginaltimestamp().isBefore(shiftStopGrace) && getOriginaltimestamp().isAfter(shiftStopDock.minusSeconds(1))) {
                adjustedtimestamp = shiftStopDock;
                adjustmenttype = PunchAdjustmentType.SHIFT_DOCK;
                
            } else if (!isWeekend && getOriginaltimestamp().isAfter(lunchStartDateTime) && getOriginaltimestamp().isBefore(lunchStopDateTime)) {
                adjustedtimestamp = lunchStartDateTime;
                adjustmenttype = PunchAdjustmentType.LUNCH_START;
            }
        }

        if (adjustmenttype == null) {
            int adjustMinute;
            int minutes = getOriginaltimestamp().getMinute();

            if((minutes % interval) < (interval / 2)){
                adjustMinute = (Math.round(minutes / interval) * interval);
            }else{
                adjustMinute = (Math.round(minutes / interval) * interval) + interval;
            }

            if ((adjustMinute / 60) == 1) {
                adjustmenttype = PunchAdjustmentType.INTERVAL_ROUND;
                adjustedtimestamp = getOriginaltimestamp().withHour(getOriginaltimestamp().getHour() + 1).withMinute(0).withSecond(0).withNano(0);
            } else
            {
                adjustmenttype = PunchAdjustmentType.INTERVAL_ROUND;
                adjustedtimestamp = getOriginaltimestamp().withMinute(adjustMinute).withSecond(0).withNano(0);
            }
            if((getOriginaltimestamp().getMinute() == adjustedtimestamp.getMinute() ) && (getOriginaltimestamp().getHour() == adjustedtimestamp.getHour())){
                adjustedtimestamp = getOriginaltimestamp().withSecond(0).withNano(0);
                adjustmenttype = PunchAdjustmentType.NONE;
            }
        }
    }
     
    @Override
    public String toString() {

        StringBuilder s = new StringBuilder();

        String dateTimeString = timestamp;
        // Define the desired format
        DateTimeFormatter start = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MM/dd/YYYY HH:mm:ss");

        // Format LocalDateTime using the defined formatter
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeString, start);
        String formattedDateTime = dateTime.format(formatter);

        s.append('#').append(badgeid).append(' ');
        s.append("CLOCK IN: ");
        s.append(formattedDateTime);

        return s.toString();

    }

    public String printOriginal() {

        StringBuilder s = new StringBuilder();
        String timestamp = this.getTimestamp();
        String badgeid = this.getBadgeId();
        int eventtypeid = this.getEventTypeId();

        String dateTimeString = timestamp;
        // Define the desired format
        DateTimeFormatter start = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EE MM/dd/yyyy HH:mm:ss");

        // Format LocalDateTime using the defined formatter
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeString, start);
        String formattedDateTime = dateTime.format(formatter).toUpperCase();

        s.append('#').append(badgeid).append(' ');

        if (eventtypeid == 1) {
            s.append("CLOCK IN: ");
        } else if (eventtypeid == 0) {
            s.append("CLOCK OUT: ");
        } else {
            s.append("TIME OUT: ");
        }
        
        s.append(formattedDateTime);

        return s.toString();

    }
    public String printAdjusted() {
    StringBuilder build = new StringBuilder();

    // Define the date and time format
    DateTimeFormatter format = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");

    // Get the day of the week from the original timestamp
    DayOfWeek dayOfTheWeek = getOriginaltimestamp().getDayOfWeek();

    // Build the adjusted punch string
    build.append("#")
         .append(badge.getId()).append(" ")
         .append(eventtype).append(": ") 
         .append(dayOfTheWeek.name().substring(0, 3))
         .append(" ")
         .append(adjustedtimestamp.format(format)) 
         .append(" (").append(adjustmenttype).append(")");

    return build.toString();
    }
}
    

