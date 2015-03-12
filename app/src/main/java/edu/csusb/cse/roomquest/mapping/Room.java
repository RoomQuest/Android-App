package edu.csusb.cse.roomquest.mapping;

public class Room {
	String Name;
	String RoomType;
    String Floor;
	int x;
	int y;
	
	public String getName() {
		return Name;
	}
	public void setName(String Name) {
		this.Name = Name;
	}
	public String getRoomType() {
		return RoomType;
	}
	public void setRoomType(String RoomType) {
		this.RoomType = RoomType;
	}
	public int getXCoord() {
		return x;
	}
	public void setXCoord(int X) {
		this.x = X;
	}
	public int getYCoord() {
		return y;
	}
	public void setYCoord(int Y) {
		this.y = Y;
	}
}
