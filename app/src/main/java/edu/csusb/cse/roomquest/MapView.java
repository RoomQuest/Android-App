package edu.csusb.cse.roomquest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

import static android.os.Environment.*;

/**
 * Created by Michael on 2/13/2015.
 */
public class MapView extends View {

    Map map = null;
    Bitmap mapBitmap = null;

    Paint paint = new Paint();
    Bitmap toilet = null;

    MapView(Context context) {
        super(context);
        init();
    }

    private void init() {
        paint.setAntiAlias(true);
        paint.setColor(getContext().getResources().getColor(R.color.csusb_blue));
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.outHeight = 48;
        o.outWidth = 48;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (map == null)
            return;

        canvas.drawBitmap(mapBitmap,0,0, paint);

        for (Room r : map.rooms) {
            canvas.drawCircle(r.getXCoord(),r.getYCoord(),25, paint);
        }
    }

    public void loadMap(Map map) {
        mapBitmap = BitmapFactory.decodeFile(getExternalStorageDirectory() + "/RoomQuest/map.png");
        this.map = map;
        postInvalidate();
    }
}