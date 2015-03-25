package edu.csusb.cse.roomquest.mapping;

import java.io.File;

import edu.csusb.cse.roomquest.wifi_nav.LocationSample;

/**
 * Created by Michael on 2/18/2015.
 */

public class Map {
    public final String name, fullName;
    public final Room[] rooms;
    public final Floor[] floors;
    public final File folder;
    /**
     * optional, can be null.
     */
    public final RoomAliases[] roomsAliases;
    /**
     * optional, can be null.
     */
    public final LocationSample[] locationSamples;

    public Map(String name, String fullName, Room[] rooms, Floor[] floors, File folder,RoomAliases[] aliases,LocationSample[] samples) {
        this.name = name;
        this.fullName = fullName;
        this.rooms = rooms;
        this.floors = floors;
        this.folder = folder;
        this.roomsAliases = aliases;
        this.locationSamples = samples;
    }

    public String toString() {
        return fullName;
    }

}
