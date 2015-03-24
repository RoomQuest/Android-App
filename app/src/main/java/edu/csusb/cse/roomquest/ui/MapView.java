package edu.csusb.cse.roomquest.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import edu.csusb.cse.roomquest.R;
import edu.csusb.cse.roomquest.mapping.Floor;
import edu.csusb.cse.roomquest.mapping.Room;
import edu.csusb.cse.roomquest.mapping.Map;

/**
 * Created by Michael on 2/13/2015.
 * Renders and allows navigation though a Map.
 */
// TODO Document this mess
public class MapView extends View {
    private static final String TAG = MapView.class.toString();
    private PointF viewPort;

    // Metrics
    private final static int
            CIRCLE_SP = 20,
            DOT_DP = 8,
            TEXT_SP = 16,
            STROKE_OUTLINE_DP = 1;
    private float
            circleSize,
            dotSize,
            textSize,
            strokeOutlineSize;

    // Map Data to display
    Map map = null;
    Floor floor = null;
    private Room highlightedRoom = null;
    Bitmap mapBitmap = null;

    // Location
    private PointF location;

    // Zooming stuff
    float maxScale = 4;
    ScaleGestureDetector scaleGestureDetector;
    GestureDetector gestureDetector;
    RectF bitmapRect, viewRect;
    Matrix
            concatMatrix = new Matrix(),
            zoomMatrix = new Matrix(),
            viewMatrix = new Matrix();
    float zoom = 1;

    // Paints
    Paint circlePaint = new Paint();
    Paint textPaint = new Paint();
    Paint whiteOutline = new Paint();
    Paint locationPaint = new Paint();

    // icons
    Drawable toilet = null;

    public MapView(Context context, AttributeSet attrs) {
        super(context,attrs);
        init();
    }

    public MapView(Context context) {
        super(context);
        init();
    }

    private void init() {
        // Set up sizes
        float scaledDensity = getResources().getDisplayMetrics().scaledDensity;
        float density = getResources().getDisplayMetrics().density;
        textSize = TEXT_SP * scaledDensity;
        circleSize = CIRCLE_SP * scaledDensity;
        dotSize = DOT_DP * density;
        strokeOutlineSize = STROKE_OUTLINE_DP * density;
        // Set up Paints;
        circlePaint.setAntiAlias(true);
        circlePaint.setColor(getContext().getResources().getColor(R.color.csusb_blue));
        whiteOutline.setColor(0xFFFFFFFF);
        whiteOutline.setStyle(Paint.Style.STROKE);
        whiteOutline.setAntiAlias(true);
        whiteOutline.setStrokeWidth(strokeOutlineSize);
        locationPaint.setColor(0xFFFF0000);
        locationPaint.setAntiAlias(true);
        textPaint.setAntiAlias(true);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(textSize);
        textPaint.setColor(0xFFFFFFFF);

        // load icons
        toilet = getResources().getDrawable(R.drawable.toilet);

        // set up zoom
        scaleGestureDetector = new ScaleGestureDetector(getContext(), new ScaleListener());
        gestureDetector = new GestureDetector(getContext(),new GestureListener());
        // concatMatrix.postScale(2,2);
    }

    /**
     * Draw the map.
     * @param canvas used for drawing calls.
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // Draw floor image
        if (mapBitmap == null) {
            String message;
            if (map == null)
                message = "No map selected";
            else
                message = "Unable to view map image";
            canvas.drawText(message, getWidth()/2, getHeight()/2, textPaint);
            return;
        }
        canvas.drawBitmap(mapBitmap, concatMatrix, circlePaint);
        // Draw floors
        if (highlightedRoom == null) {
            for (Room room : map.rooms) {// Draw normally
                if (room.getFloor() == floor)
                    drawRoomLabel(canvas, room);
            }
        } else {
            for (Room room : map.rooms) { // Hide others
                if (room.getFloor() == floor)
                    if (room != highlightedRoom)
                        drawRoomDot(canvas,room);
            }
            if (highlightedRoom.getFloor() == floor) // Draw highlighted room normally.
                drawRoomLabel(canvas, highlightedRoom);
        }
        if (location != null)
            drawLocation(canvas);
    }

    private void drawLocation(Canvas canvas) {
        float[] point = new float[2];
        point[0]=location.x;
        point[1]=location.y;
        concatMatrix.mapPoints(point);
        float x = point[0], y = point[1];
        canvas.drawCircle(x, y, dotSize,locationPaint);
    }

    private void drawRoomLabel(Canvas canvas, Room room) {
        float[] point = new float[2];
        point[0]=room.getXCoord();
        point[1]=room.getYCoord();
        concatMatrix.mapPoints(point);
        float x = point[0], y = point[1];
        canvas.drawCircle(x, y, circleSize, circlePaint);
        canvas.drawCircle(x, y, circleSize, whiteOutline);
        switch (room.getType().toLowerCase()) {
            case "bathroom" :
            case "restroom" :
                toilet.setBounds(
                        (int)(x - circleSize / 2),
                        (int)(y - circleSize / 2),
                        (int)(x + circleSize / 2),
                        (int)(y + circleSize / 2)
                );
                toilet.draw(canvas);
                break;
            default :
            case "classroom" :
                canvas.drawText(room.getName(),x,y + textSize/3,textPaint);
                break;
        }
    }

    private void drawRoomDot(Canvas canvas,Room room) {
        float[] point = new float[2];
        point[0]=room.getXCoord();
        point[1]=room.getYCoord();
        concatMatrix.mapPoints(point);
        float x = point[0], y = point[1];
        canvas.drawCircle(x, y, dotSize, circlePaint);
    }

    public void setLocation(float x, float y) {
        location = new PointF(x,y);
    }

    public void loadFloor(Floor floor) {
        this.floor = floor;
        if (mapBitmap != null) {
            mapBitmap.recycle();
            mapBitmap = null;
        }
        if (map != null && floor != null) {
            if (floor.getImageFile().exists())
                mapBitmap = BitmapFactory.decodeFile(floor.getImageFile().getPath());
            updateBaseMatrix();
            updateConcatMatrix();
        }
        else
            mapBitmap = null;
        Log.d(TAG, "decode " + floor + " " + (mapBitmap == null ? "failure" : "success"));
        invalidate();
    }
    /**
     * Prepare to view a given map. This will load the
     * @param map The map to load.
     * @param floor the floor to load.
     */
    public void loadMap(Map map, Floor floor) {
        this.map = map;
        loadFloor(floor);
    }

    /**
     * Highlight a Room on the Map
     * @param room the Room in the map to highlight. Can be null for no highlight.
     */
    public void highlightRoom(Room room) {
        highlightedRoom = room;
        invalidate();
    }

   @Override
   public boolean onTouchEvent(MotionEvent e) {
       boolean ret = super.onTouchEvent(e);
       if (mapBitmap != null) { // lol, zoom null? Not a chance.
           ret |= scaleGestureDetector.onTouchEvent(e);
           if (!scaleGestureDetector.isInProgress())
               ret |= gestureDetector.onTouchEvent(e);
       }
       return ret;
   }

    private void updateBaseMatrix() {
        if (mapBitmap != null) {
            viewMatrix.setRectToRect(
                    new RectF(0, 0, mapBitmap.getWidth(), mapBitmap.getHeight()),
                    new RectF(0, 0, getWidth(), getHeight()),
                    Matrix.ScaleToFit.CENTER
            );
        } else {
            viewMatrix.reset();
        }
    }

    private void updateConcatMatrix() {
        concatMatrix.set(viewMatrix);
        concatMatrix.postConcat(zoomMatrix);
    }

    @Override
    public void onSizeChanged(int x, int y, int oldx, int oldy) {
        super.onSizeChanged(x,y,oldx,oldy);
        updateBaseMatrix();
        zoomMatrix.reset();
        updateConcatMatrix();
    }

    private void translateBy(float dx,float dy) {
        RectF map = new RectF(
                0,
                0,
                mapBitmap.getWidth(),
                mapBitmap.getHeight()
        );
        concatMatrix.mapRect(map);
        float cx = getWidth()/2;
        float cy = getHeight()/2;
        if (map.left + dx > cx)
            dx = cx - map.left;
        else if (map.right + dx < cx)
            dx = cx - map.right;
        if (map.top + dy > cy)
            dy = cy - map.top;
        else if (map.bottom + dy < cy)
            dy = cy - map.bottom;
        zoomMatrix.postTranslate(dx, dy);
        updateConcatMatrix();
    }

    private void scaleBy(float s, float x, float y) {
        float currentScale = zoomMatrix.mapRadius(1);
        if (currentScale * s > maxScale)
            s = maxScale / currentScale;
        else if (currentScale * s < 1)
            s = 1 / currentScale;
        zoomMatrix.postScale(s, s, x, y);
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        float lastX, lastY;
        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            //Log.d(TAG,"Scale Begin");
            lastX = detector.getFocusX();
            lastY = detector.getFocusY();
            return true;
        }
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            //Log.d(TAG, "Scale: " + detector.getScaleFactor());
            float x = detector.getFocusX(), y = detector.getFocusY();
            translateBy(x - lastX, y - lastY);
            scaleBy(detector.getScaleFactor(),x,y);
            updateConcatMatrix();
            invalidate();
            lastX = x;
            lastY = y;
            return true;
        }
        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            //Log.d(TAG, "Scale ended");
        }

    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float x, float y) {
            translateBy(-x, -y);
            updateConcatMatrix();
            invalidate();
            return true;
        }
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            //Log.d(TAG, "onDoubleTap()");
            scaleBy(2f,e.getX(),e.getY());
            updateConcatMatrix();
            invalidate();
            return true;
        }
    }
}
