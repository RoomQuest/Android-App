package edu.csusb.cse.roomquest.mapping;

import java.io.File;

/**
 * Created by Michael on 2/18/2015.
 */

public class Map {
    public final String name, fullName;
    public final Room[] rooms;
    public final Floor[] floors;
    public final File folder;
    public final RoomAliases[] roomsAliases;

    public Map(String name, String fullName, Room[] rooms, Floor[] floors, File folder) {
        this.name = name;
        this.fullName = fullName;
        this.rooms = rooms;
        this.floors = floors;
        this.folder = folder;
        this.roomsAliases = null;
    }

    public String toString() {
        return fullName;
    }

}
