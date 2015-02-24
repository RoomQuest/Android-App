package com.torres.java;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

import com.torres.domain.Maps;


public class MapMaker {
	public void ParseMapFile() {
		String mapFileToRead = "mapfiles/map.csv";
		BufferedReader br = null;
		String line = "";
		String splitBy = ",";
		List<Maps> mapList = new ArrayList<Maps>();
		
		try {
			br = new BufferedReader(new FileReader(mapFileToRead));
			while((line = br.readLine()) != null) {
				//split comma
				String [] maps = line.split(splitBy);
				
				//Create map object to store values
				Maps mapObject = new Maps();
				
				//Enter csv information to Map object
				mapObject.setName(maps[0]);
				mapObject.setRoomType(maps[1]);
				mapObject.setXCoord(maps[2]);
				mapObject.setYCoord(maps[3]);
				
				//add mapObject to the list
				mapList.add(mapObject);
			}
			//print values stored in Map
			printMapList(mapList);
			
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
		
	}
	public void printMapList(List<Maps> mapListToPrint) {
		for(int i = 0; i < mapListToPrint.size(); i++) {
			System.out.println("Maps [ Name: " + mapListToPrint.get(i).getName()
			+ " RoomType: " + mapListToPrint.get(i).getRoomType() + " XCoord: "
			+ mapListToPrint.get(i).getXCoord() + " YCoord: " + mapListToPrint.get(i).getYCoord()
			+ " ]");
		}
	}

}

