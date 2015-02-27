package edu.csusb.cse.roomquest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.transition.CircularPropagation;
import android.view.View;

import static android.os.Environment.*;

/**
 * Created by Michael on 2/13/2015.
 * Renders and allows navigation though a Map.
 */
public class MapView extends View {

    private final static int
            LABEL_SIZE = 36,
            TEXT_SIZE = 24;

    Map map = null;
    Bitmap mapBitmap = null;

    private PointF location;
    Paint locationPaint = new Paint();

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
        if (map == null)
            return;
        canvas.drawBitmap(mapBitmap,0,0, circlePaint);
        for (Room room : map.rooms) {
            drawRoomLabel(canvas, room);
        }
        if (location != null)
            canvas.drawCircle(location.x,location.y,12,locationPaint);
    }

    private void drawRoomLabel(Canvas canvas, Room room) {
        canvas.drawCircle(room.getXCoord(),room.getYCoord(),LABEL_SIZE, circlePaint);
        Rect dest = new Rect();
        dest.set(
                room.getXCoord() - LABEL_SIZE / 2,
                room.getYCoord() - LABEL_SIZE / 2,
                room.getXCoord() + LABEL_SIZE / 2,
                room.getYCoord() + LABEL_SIZE / 2
        );
        Bitmap icon;
        switch (room.getRoomType()) {
            default :
            case "bathroom" :
                canvas.drawBitmap(toilet, new Rect(0, 0, toilet.getHeight(), toilet.getWidth()), dest, circlePaint);
                break;
            case "classroom" :
                canvas.drawText(room.getName(),room.getXCoord(),room.getYCoord() + TEXT_SIZE/2,textPaint);
                break;
        }
    }

    public void setLocation(float x, float y) {
        location = new PointF(x,y);
    }

    public void loadMap(Map map) {
        this.map = map;
        mapBitmap = BitmapFactory.decodeFile(getExternalStorageDirectory() + "/RoomQuest/map.png");
        postInvalidate();
    }
}