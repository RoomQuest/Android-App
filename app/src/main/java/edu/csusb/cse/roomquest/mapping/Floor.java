package edu.csusb.cse.roomquest.mapping;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;

/**
 * Created by Michael on 3/12/2015.
 */
public class Floor {

    private final String name;
    private final File image;
    // More information on each floor here in the future

    public Floor(String name, File image) {
        this.name = name;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public File getImageFile() {
        return image;
    }

    public Bitmap getImageBitmap() {
        return BitmapFactory.decodeFile(image.getPath());
    }

    public static Floor findFloorByName(Floor[] floors, String name) {
        for (Floor floor : floors) {
            if (floor.getName().contains(name)) {
                return floor;
            }
        }
        return null;
    }

}
