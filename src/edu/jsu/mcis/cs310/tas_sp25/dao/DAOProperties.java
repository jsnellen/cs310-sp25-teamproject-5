/**
 * DAOProperties is a utility class for loading and retrieving database 
 * configuration values from a properties file. 
 * It uses a prefix to isolate configuration groups for different environments.
 */
package edu.jsu.mcis.cs310.tas_sp25.dao;

import java.io.*;
import java.util.Properties;

public class DAOProperties {

    /** Name of the properties file containing DAO configuration settings. */
    private static final String PROPERTIES_FILE = "dao.properties";

    /** Shared Properties object loaded with values from the properties file. */
    private static final Properties PROPERTIES = new Properties();

    /** Prefix used to distinguish property groups (e.g., 'tas.jdbc'). */
    private final String prefix;

    // Static block to load the properties file once when the class is loaded
    static {
        try {
            InputStream file = DAOProperties.class.getResourceAsStream(PROPERTIES_FILE);
            PROPERTIES.load(file);
        } catch (IOException e) {
            throw new DAOException(e.getMessage());
        }
    }

    /**
     * Constructs a DAOProperties object for a given configuration prefix.
     *
     * @param prefix the prefix used to retrieve specific property values
     */
    public DAOProperties(String prefix) {
        this.prefix = prefix;
    }

    /**
     * Retrieves a property value using the prefix and provided key.
     *
     * @param key the property key (e.g., "url", "username")
     * @return the corresponding property value, or null if not found
     */
    public String getProperty(String key) {
        String fullKey = prefix + "." + key;
        String property = PROPERTIES.getProperty(fullKey);

        if (property == null || property.trim().length() == 0) {
            property = null;
        }

        return property;
    }

}
