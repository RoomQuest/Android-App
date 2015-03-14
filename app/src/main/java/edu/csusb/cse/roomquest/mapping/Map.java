package edu.csusb.cse.roomquest.mapping;

/**
 * Created by Michael on 2/18/2015.
 */

public class Map {
    public final String name, fullName;
    public final Room[] rooms;
    public final Floor[] floors;

    public Map(String name, String fullName, Room[] rooms, Floor[] floors) {
        this.name = name;
        this.fullName = fullName;
        this.rooms = rooms;
        this.floors = floors;
    }

}