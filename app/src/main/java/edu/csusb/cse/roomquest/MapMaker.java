package edu.csusb.cse.roomquest;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MapMaker {
    public void ParseMapFile(String file) {
        ParseMapFile(new File(file));
    }
	public Map ParseMapFile(File file) {
        // Now using file instead
		//String mapFileToRead = "mapfiles/map.csv";
		BufferedReader br = null;
		String line = "";
		String splitBy = ",";

        Map map = new Map();
		List<Room> mapList = new ArrayList<Room>();
		
		try {
			br = new BufferedReader(new FileReader(file));

            map.mapImageName = br.readLine();
			while((line = br.readLine()) != null) {
				//split comma
				String [] maps = line.split(splitBy);
				
				//Create map object to store values
				Room mapObject = new Room();
				
				//Enter csv information to Map object
				mapObject.setName(maps[0]);
				mapObject.setRoomType(maps[1]);
				mapObject.setXCoord(maps[2]);
				mapObject.setYCoord(maps[3]);
				
				//add mapObject to the list
				mapList.add(mapObject);
			}
			//print values stored in Map
			//printMapList(mapList);

            return map;

			
		}catch(FileNotFoundException e) {
			e.printStackTrace();
		}catch(IOException e) {
			e.printStackTrace();
		}finally {
			if(br != null) {
				try {
					br.close();
				}catch(IOException e) {
					e.printStackTrace();
				}
			}
		}

        return map;
    }
/*	public void printMapList(List<Room> mapListToPrint) {
		for(int i = 0; i < mapListToPrint.size(); i++) {
			System.out.println("Maps [ Name: " + mapListToPrint.get(i).getName()
			+ " RoomType: " + mapListToPrint.get(i).getRoomType() + " XCoord: "
			+ mapListToPrint.get(i).getXCoord() + " YCoord: " + mapListToPrint.get(i).getYCoord()
			+ " ]");
		}
	}
*/
}

