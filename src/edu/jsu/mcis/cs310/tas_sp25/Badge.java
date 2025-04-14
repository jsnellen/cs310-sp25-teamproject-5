package edu.jsu.mcis.cs310.tas_sp25;

import java.util.zip.CRC32;

/**
 * The Badge class represents an identification badge used in the time and attendance system.
 * It contains details about the badge's ID and description, which can be used to uniquely identify
 * employees or terminals in the system.
 */
public class Badge {

    /** The unique identifier for the badge. */
    private final String id;

    /** A description associated with the badge. */
    private final String description;

    /**
     * Constructs a Badge object with the given ID and description.
     *
     * @param id the unique identifier for the badge.
     * @param description a description for the badge (e.g., employee name or terminal description).
     */
    public Badge(String id, String description) {
        this.id = id;
        this.description = description;
    }
    
    // Constructor used for creating new badges.
    public Badge(String description){
        this.description = description;
        
        CRC32 crc = new CRC32();
        crc.update(description.getBytes());
        long checksum = crc.getValue();
        
        this.id = String.format("%08X", checksum);
    }

    /**
     * Returns the unique identifier of the badge.
     *
     * @return the ID of the badge.
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the description associated with the badge.
     *
     * @return the description of the badge.
     */
    public String getDescription() {
        return description;
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
        s.append('#').append(id).append(' ');
        s.append('(').append(description).append(')');

        return s.toString();

    }
}

