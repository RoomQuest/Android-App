package edu.csusb.cse.roomquest.parsing;


import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import edu.csusb.cse.roomquest.mapping.Floor;
import edu.csusb.cse.roomquest.mapping.Map;
import edu.csusb.cse.roomquest.mapping.Room;

/**
 * Contains static methods to handle parsing files.
 */
public class MapMaker {
    public static final String LOG_TAG = "MapMaker";

    /**
     *
     * @param folder The folder where the files reside (see File Structure documentation)
     * @param name The name of the map ex. JB
     * @param fullName The full name of the map ex. Jack Brown Hall
     * @return A new Map made from parsing the folder.
     */
    public static Map parseMapFolder(File folder, String name, String fullName) {


        List<Room> roomList = new ArrayList<Room>();
        Floor[] floors = null;

        // Ok, lets parse rooms.csv first.
        // Setup BufferedReader to read lines.
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(
                    // rooms.csv path
                    new File(folder, "rooms.csv")
            ));
        } catch (FileNotFoundException e) {
            // Where's the file?
            Log.e(LOG_TAG, "Can't find rooms.csv");
            return null;
        }
        String line = null;
        try {
            // Read first line.
            if ((line = br.readLine())!= null) {
                String[] floorNames = line.split (",");
                floors = new Floor[floorNames.length];
                for (int i = 0; i < floorNames.length; i++) {
                    floors[i] = new Floor(
                            floorNames[i],
                            new File(folder,floorNames[i])
                    );
                }
            } else {
                return null;
            }
            // Read each line while there is one
            while((line = br.readLine()) != null) {
                // Split it up into elements
                String[] elements = line.split(",");
                // Make sure each element doesn't throw an exception
                try {
                    // Create and add a new Room.
                    roomList.add(new Room(
                            elements[0], // Name
                            elements[1], // Type
                            Floor.findFloorByName(floors, elements[2]), // Floor
                            Integer.parseInt(elements[3]), // X
                            Integer.parseInt(elements[4]) // Y
                    ));
                } catch (Exception e) {
                    // Well, looks like someone screwed up that line in the CSV format, don't expect me to read it.
                    Log.e(LOG_TAG,"Unable to parse line in rooms.csv");
                }
            }
        // More irritating exception crap
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // -- Make sure to close the BufferedReader!
            // -- Thanks Mom!
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        // Finally, make the map and return it.
        return new Map(
                name, // name
                fullName, // full name
                roomList.toArray(new Room[roomList.size()]), // rooms
                floors // floors
        );
    }



}

