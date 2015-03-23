package edu.csusb.cse.roomquest.search;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

import edu.csusb.cse.roomquest.mapping.Map;
import edu.csusb.cse.roomquest.mapping.Room;
import edu.csusb.cse.roomquest.mapping.RoomAliases;

/**
 * Created by Michael on 3/22/2015.
 * Allows searching through a map.
 */
public class SearchMap {

    public static Result[] search(Map map, String query) {

        Log.d("RoomQuestSearch","Searched for " + query + " in " + map);
        if (map == null) {
            return null;
        }
        // set up a TreeSet with a comparator to make things ordered and the chaos to a minimum.
        TreeSet<Result> results = new TreeSet<Result>(new Comparator<Result>() {
            @Override
            public int compare(Result lhs, Result rhs) {
                if(lhs.getName().equalsIgnoreCase(rhs.getName()))
                    return lhs.getRoom().getName().compareToIgnoreCase(rhs.getRoom().getName());
                else
                    return (lhs.getDifference() >= rhs.getDifference()) ? 1 : -1;
            }
        });
        if (query == null || query.isEmpty()) { // return all if empty
            Log.d("", "Searching all");
            for (Room room : map.rooms) {
                results.add(new Result(
                        room,
                        room.getType(),
                        0
                ));
            }
            if (map.roomsAliases != null) {
                for (RoomAliases roomAliases : map.roomsAliases) {
                    for (String alias : roomAliases.getAliases()) {
                        results.add(new Result(
                                roomAliases.getRoom(),
                                alias,
                                1
                        ));
                    }
                }
            }
        } else {
            query = query.toLowerCase();
            String[] subqueries = query.split(" ");
            for (Room room : map.rooms) {
                // test if the query matches as it is
                if (room.getName().toLowerCase().contains(query)) {
                    results.add(new Result(
                            room,
                            room.getName(),
                            room.getName().length() - query.length()
                    ));
                } else {
                    for (String q : subqueries) { // go though each sub query
                        int qLength = q.length();
                        if (room.getName().toLowerCase().contains(q)) {
                            results.add(new Result(
                                    room,
                                    room.getName(),
                                    room.getName().length() - qLength
                            ));
                        }
                        if (room.getType().toLowerCase().contains(q)) {
                            results.add(new Result(
                                    room,
                                    room.getType(),
                                    room.getType().length() - qLength
                            ));
                        }
                    }
                }
            }
            if (map.roomsAliases != null) {
                for (RoomAliases alias : map.roomsAliases) {
                    for (String name : alias.getAliases()) {
                        if (name.toLowerCase().contains(query)) {
                            results.add(new Result(
                                    alias.getRoom(),
                                    name,
                                    name.length() - query.length()
                            ));
                        } else {
                            for (String q : subqueries) { // go though each sub query
                                if (name.toLowerCase().contains(q)) {
                                    results.add(new Result(
                                            alias.getRoom(),
                                            name,
                                            name.length() - q.length()
                                    ));
                                }
                            }
                        }
                    }
                }
            }
        }
        return results.toArray(new Result[results.size()]);
    }

    public static class Result {
        private final Room room;
        private final String name;
        private int difference;

        public Result(Room room, String name, int difference) {
            this.room = room;
            this.name = name;
            this.difference = difference;
        }

        public Room getRoom() {
            return room;
        }

        public String getName() {
            return name;
        }

        public int getDifference() {
            return difference;
        }

        public void setDifference(int difference) {
            this.difference = difference;
        }
    }
}
