package edu.csusb.cse.roomquest.UI;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.view.View;

import edu.csusb.cse.roomquest.R;
import edu.csusb.cse.roomquest.mapping.Room;
import edu.csusb.cse.roomquest.mapping.Map;

/**
 * Created by Michael on 2/13/2015.
 * Renders and allows navigation though a Map.
 */
public class MapView extends View {

    private PointF viewPort;

    private final static int
            LABEL_SIZE = 36,
            TEXT_SIZE = 24,
            DOT_SIZE = 12;

    // Map to display
    Map map = null;
    Bitmap mapBitmap = null;

    // Location
    private PointF location;
    Paint locationPaint = new Paint();

    /**
     * Highlight Room
     * @param highlightedRoom The room to highlight (null for none)
     */
    private Room highlightedRoom = null;

    // Paints
    Paint circlePaint = new Paint();
    Bitmap toilet = null;
    Paint textPaint = new Paint();

    MapView(Context context) {
        super(context);
        init();
    }

    private void init() {
        circlePaint.setAntiAlias(true);
        circlePaint.setColor(getContext().getResources().getColor(R.color.csusb_blue));
        toilet = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.toilet);
        locationPaint.setColor(0xFFFF0000);
        locationPaint.setAntiAlias(true);
        textPaint.setAntiAlias(true);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(TEXT_SIZE);
        textPaint.setColor(0xFFFFFFFF);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //canvas.scale(1.5f,1.5f);
        if (map == null)
            return;
        canvas.drawBitmap(mapBitmap, 0, 0, circlePaint);
        if (highlightedRoom == null) {
            for (Room room : map.rooms) {
                drawRoomLabel(canvas, room);
            }
        } else {
            for (Room room : map.rooms) {
                if (room != highlightedRoom)
                    drawRoomDot(canvas,room);
            }
            drawRoomLabel(canvas, highlightedRoom);
        }
        if (location != null)
            canvas.drawCircle(location.x,location.y,DOT_SIZE,locationPaint);
    }

    private void drawRoomLabel(Canvas canvas, Room room) {
        canvas.drawCircle(room.getXCoord(), room.getYCoord(), LABEL_SIZE, circlePaint);
        switch (room.getRoomType()) {
            default :
            case "bathroom" :
                Rect dest = new Rect();
                dest.set(
                        room.getXCoord() - LABEL_SIZE / 2,
                        room.getYCoord() - LABEL_SIZE / 2,
                        room.getXCoord() + LABEL_SIZE / 2,
                        room.getYCoord() + LABEL_SIZE / 2
                );
                canvas.drawBitmap(toilet, new Rect(0, 0, toilet.getHeight(), toilet.getWidth()), dest, circlePaint);
                break;
            case "classroom" :
                canvas.drawText(room.getName(),room.getXCoord(),room.getYCoord() + TEXT_SIZE/2,textPaint);
                break;
        }
    }

    private void drawRoomDot(Canvas canvas,Room room) {
        canvas.drawCircle(room.getXCoord(),room.getYCoord(),DOT_SIZE,circlePaint);
    }

    public void setLocation(float x, float y) {
        location = new PointF(x,y);
    }

    public void setLocation(PointF point) {
        location = point;
    }

    /**
     * Prepare to view a given map. This will load the
     * @param map The map to load
     */
    public void loadMap(Map map, String path) {
        this.map = map;
        mapBitmap = BitmapFactory.decodeFile(path);
        postInvalidate();
    }

    /**
     * Highlight a Room on the Map
     * @param room the Room in the map to highlight. Can be null for no highlight.
     */
    public void highlightRoom(Room room) {
        highlightedRoom = room;
        invalidate();
    }
}