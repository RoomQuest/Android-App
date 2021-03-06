package edu.csusb.cse.roomquest.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import edu.csusb.cse.roomquest.R;
import edu.csusb.cse.roomquest.mapping.Room;

/**
 * Created by Michael on 2/27/2015.
 */
public class RoomIconDrawable extends Drawable {

    // Resources
    Bitmap toilet;

    // Paints
    final Paint circlePaint = new Paint();
    final Paint textPaint = new Paint();

    // Room
    Room room = null;

    public RoomIconDrawable(Context context) {
        toilet = BitmapFactory.decodeResource(context.getResources(), R.drawable.toilet);
        circlePaint.setColor(context.getResources().getColor(R.color.csusb_blue));
        circlePaint.setAntiAlias(true);
        textPaint.setAntiAlias(true);
        textPaint.setColor(0xFFFFFFFF);
        textPaint.setTextAlign(Paint.Align.CENTER);
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawCircle(getBounds().centerX(),getBounds().centerY(),getBounds().height()/2,circlePaint);
        if(room != null) {
            switch (room.getType().toLowerCase()) {
                case "bathroom" :
                case "restroom" :
                    canvas.drawBitmap(toilet, new Rect(0, 0, toilet.getHeight(), toilet.getWidth()), new Rect(getBounds().width()/4,getBounds().height()/4,getBounds().width()*3/4,getBounds().height()*3/4), textPaint);
                break;
                default :
                case "classroom" :
                    textPaint.setTextSize(getBounds().height()/3);
                    canvas.drawText(room.getName(), getBounds().centerX(), getBounds().centerY()+getBounds().height()/6, textPaint);
            }
        }
    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(ColorFilter cf) {

    }

    @Override
    public int getOpacity() {
        return 0;
    }
}
