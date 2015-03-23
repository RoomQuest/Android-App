package edu.csusb.cse.roomquest.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import edu.csusb.cse.roomquest.R;
import edu.csusb.cse.roomquest.mapping.Floor;

/**
 * Created by Michael on 3/14/2015.
 */
public class FloorSelectorView extends View {

    private static final String TAG = FloorSelectorView.class.toString();
    // Gesture detection
    GestureDetector gestureDetector;
    OnIndexChangeListener listener;

    // Display metrics
    private int selectionBoxWidth;
    private float innerPaddingDIP = 4;
    private float innerPadding;
    private float textSizeSP = 32;
    private float textSize;

    private Paint innerBoxPaint = new Paint(),
            selectionBoxPaint = new Paint(),
            textPaint = new Paint();


    private Floor[] floors;
    private float selectorY;
    private int index = 0;

    public FloorSelectorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        innerPadding = getResources().getDisplayMetrics().density * innerPaddingDIP;
        textSize = getResources().getDisplayMetrics().scaledDensity * textSizeSP;
        innerBoxPaint.setColor(getResources().getColor(R.color.csusb_dark_grey));
        selectionBoxPaint.setColor(getResources().getColor(R.color.csusb_blue));
        textPaint.setAntiAlias(true);
        textPaint.setColor(getResources().getColor(android.R.color.white));
        textPaint.setTextSize(textSize);
        textPaint.setTextAlign(Paint.Align.CENTER);

        if (isInEditMode()) {
            setFloors(new Floor[]{
                    new Floor("B", null),
                    new Floor("1", null),
                    new Floor("2", null),
                    new Floor("3", null),
                    new Floor("4", null),
                    new Floor("5", null),
                    new Floor("6", null),
                    new Floor("7", null)
            });
        }
        gestureDetector = new GestureDetector(getContext(),new GestureListener());
        gestureDetector.setIsLongpressEnabled(false);
    }

    public void setFloors(Floor[] floors, Floor floor) {
        this.floors = floors;
        selectFloor(floor);
    }

    public void setFloors(Floor[] floors,int index) {
        this.floors = floors;
        this.index = index;
        setSelectorYFromIndex();
        requestLayout();
        if(listener != null) {
            listener.onIndexChange(index);
        }
    }
    public void setFloors(Floor[] floors) {
        setFloors(floors,0);
    }

    public void selectFloor(Floor floor) {
        if (floors != null && floors.length != 0) {
            for (int i = 0; i < floors.length; i++) {
                if (floors[i] == floor) {
                    setIndex(i);
                    return;
                }
            }
        }
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldw, int oldh) {
        super.onSizeChanged(width, height, oldw, oldh);
        selectionBoxWidth = width;
        setSelectorYFromIndex();
    }

    private void setSelectorYFromIndex() {
        selectorY = getHeight()-((index+1)*selectionBoxWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (floors == null || floors.length == 0) {
            return;
        }
        RectF target = new RectF();
        target.set (
                0f,
                selectorY,
                selectionBoxWidth,
                selectionBoxWidth + selectorY
        );
        canvas.drawRect(target,selectionBoxPaint);
        for (int i = 0; i < floors.length; i++) {
            // Draw inner boxes
            target.set(
                    innerPadding, // left
                    getHeight()-((i+1)*selectionBoxWidth - innerPadding), // top
                    selectionBoxWidth - innerPadding, // right
                    getHeight()-(i*selectionBoxWidth + innerPadding) // bottom
            );
            canvas.drawRect(target, innerBoxPaint);
            // Draw text
            float centerX = selectionBoxWidth / 2;
            float centerY = getHeight()-((i)*selectionBoxWidth + selectionBoxWidth/2) + textSize / 3;
            canvas.drawText(
                    floors[i].getName(),
                    centerX,
                    centerY,
                    textPaint
            );
        }
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        int width = MeasureSpec.getSize(widthSpec);
        if (floors == null || floors.length == 0) {
            setMeasuredDimension(0,0);
        } else {
            setMeasuredDimension(
                    width,
                    width * floors.length
            );
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        super.onTouchEvent(motionEvent);
        gestureDetector.onTouchEvent(motionEvent);
        return true;
    }

    public void setIndex(int index) {
        if (index != this.index) {
            Log.d(TAG,"Index changed from " + this.index + " to " + index);
            this.index = index;
            if (listener != null) {
                listener.onIndexChange(index);
            }
            setSelectorYFromIndex();
            invalidate();
        }
    }

    public void setOnIndexChangeListener(OnIndexChangeListener listener) {
        this.listener = listener;
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float x, float y) {
            //if (e2.getAction() == MotionEvent.ACTION_UP) { // Doesn't seem to capture on up
                setIndexFromCenterY(e2.getY());
            //} else {
            //    setSelectorYFromCenterY(e2.getY());
            //}
            return true;
        }
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            setIndexFromCenterY(e.getY());
            return true;
        }
    }

    private void setSelectorYFromCenterY(float center) {
        float offset = selectionBoxWidth / 2;
        if (center < offset) {
            selectorY = 0;
        } else if (center > getHeight() - offset) {
            selectorY = getHeight() - selectionBoxWidth;
        } else {
            selectorY = center - offset;
        }
    }

    private void setIndexFromCenterY(float center) {
        for (int i = 0; i < floors.length; i++) {
            if (center > getHeight() - (i + 1) * selectionBoxWidth) {
                setIndex(i);
                return;
            }
        }
    }

    public interface OnIndexChangeListener {
        public void onIndexChange(int index);
    }

}
