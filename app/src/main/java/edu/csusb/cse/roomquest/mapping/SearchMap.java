package edu.csusb.cse.roomquest.mapping;

import java.util.ArrayList;

/**
 * Contains methods to search maps.
 */
public class SearchMap {

    /**
     * Return an array of rooms matching the query.
     * @param map the map to search.
     * @param query the query to search for.
     * @return an array of rooms matching the query.
     */
    public static Room[] SearchForRooms(Map map, String query) {
        ArrayList<Room> roomList = new ArrayList<Room>();

        for (Room room : map.rooms) {
            if (room.getName().toLowerCase().contains(query.toLowerCase()) ||
                    room.getType().toLowerCase().contains(query.toLowerCase()))
                roomList.add(room);
        }
        if (roomList.isEmpty())
            return null;
        else
            return roomList.toArray(new Room[0]);
    }

    /**
     * Not used for now:
     */
    public static class results {
        static class SearchResults {
            Result[] results;
            static class Result {
                Room room;
                // Not used for now:
                // int matchStrength;
            }
        }
    }

}
//SearchMap sm = new SearchMap;
//SearchMap.SearchResults sr = SearchMap.SearchResults();