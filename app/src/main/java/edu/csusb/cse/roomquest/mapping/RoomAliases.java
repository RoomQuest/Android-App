package edu.csusb.cse.roomquest.mapping;

/**
 * Created by Michael on 3/22/2015.
 */
public class RoomAliases {
    private final Room room;
    private final String[] aliases;

    public RoomAliases(Room room, String[] aliases) {
        this.room = room;
        this.aliases = aliases;
    }

    public String[] getAliases() {
        return aliases;
    }

    public Room getRoom() {
        return room;
    }
}
