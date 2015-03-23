package edu.csusb.cse.roomquest.mapping;

public class Hotspot {
    int x;
    int y;
    String BSSID;
    String Power;

    public int getXLocation() {
        return x;
    }

    public void setXLocation(int X) {
        this.x = X;
    }

    public int getYLocation() {
        return y;
    }

    public void setYLocation(int Y) {
        this.y = Y;
    }

    public String getBSSID() {
        return BSSID;
    }

    public void setBSSID(String BSSID) {
        this.BSSID = BSSID;
    }

    public String getPower() {
        return Power;
    }

    public void setPower1(String Power) {
        this.Power = Power;
    }
}
	
