package edu.jsu.mcis.cs310.tas_sp25;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Punch {

    private final int id, terminalid, eventtypeid;
    private final String badgeid, timestamp;

    public Punch(int id, int terminalid, String badgeid, String timestamp, int eventtypeid) {
        this.id = id;
        this.terminalid = terminalid;
        this.badgeid = badgeid;
        this.timestamp = timestamp;
        this.eventtypeid = eventtypeid;
    }

    public int getId() {
        return id;
    }

    public int getTerminalId() {
        return terminalid;
    }

    public String getBadgeId() {
        return badgeid;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public int getEventTypeId() {
        return eventtypeid;
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
}
