package edu.jsu.mcis.cs310.tas_sp25;

/**
 * The Badge class represents an identification badge used in the time and attendance system.
 * It contains details about the badge's ID and description, which can be used to uniquely identify
 * employees or terminals in the system.
 */
public class ScheduleOverride {

    /** The unique identifier for the badge. */
    private final int id;

    /** A description associated with the badge. */
    private final String start;

    private final String end;

    private final String badgeid;

    private final int day;

    private final int dailyscheduleid;

    /**
     * Constructs a Badge object with the given ID and description.
     *
     * @param id the unique identifier for the badge.
     * @param description a description for the badge (e.g., employee name or terminal description).
     */
    public ScheduleOverride(int id, String start, String end, String badgeid, int day, int dailyscheduleid) {

        this.id = id;
        this.start = start;
        this.end = end;
        this.badgeid = badgeid;
        this.day = day;
        this.dailyscheduleid = dailyscheduleid;

    }

    /**
     * Returns the unique identifier of the badge.
     *
     * @return the ID of the badge.
     */
    public int getId() {
        return id;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

    public String getBadgeId() {
        return badgeid;
    }

    public int getDay() {
        return day;
    }

    public int getDailyScheduleId() {
        return dailyscheduleid;
    }

    /**
     * Returns a string representation of the Badge object in the format:
     * "#<id> (<description>)".
     *
     * @return a string representation of the badge.
     */
    @Override
    public String toString() {

        StringBuilder s = new StringBuilder();

        // Append badge ID and description to the string builder
        s.append('#').append(id).append(' ')
        .append("start: ").append(start).append(", ")
        .append("end: ").append(end).append(", ")
        .append("badgeid: ").append(badgeid).append(" ")
        .append("day: ").append(day).append(" ")
        .append("dailyscheduleid: ").append(dailyscheduleid);

        return s.toString();

    }
}