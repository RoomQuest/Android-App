package edu.csusb.cse.roomquest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.View;

/**
 * Created by Michael on 2/13/2015.
 */
public class MapView extends View {

    Map map = null;

    MapView(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //canvas.drawBitmap();
    }

    public void setMap(Map map) {
        this.map = map;
        invalidate();
    }
}