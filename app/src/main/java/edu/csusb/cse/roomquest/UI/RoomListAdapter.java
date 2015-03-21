package edu.csusb.cse.roomquest.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import edu.csusb.cse.roomquest.R;
import edu.csusb.cse.roomquest.mapping.Room;

/**
 * Created by Michael on 2/27/2015.
 *
 */
public class RoomListAdapter extends BaseAdapter {

    private final Context context;
    Room[] rooms = null;

    RoomListAdapter(Context context) {
        this.context = context;
    }

    public void setRooms(Room[] rooms) {
        this.rooms = rooms;
        notifyDataSetInvalidated();
    }

    @Override
    public int getCount() {
        if (rooms != null) {
            return rooms.length;
        } else {
            return 0;
        }
    }

    @Override
    public Room getItem(int position) {
        if (rooms != null)
            return rooms[position];
        else
            return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Tag tag;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.room_list_item,parent,false);
            tag = new Tag(convertView);
            convertView.setTag(tag);
        } else {
            tag = (Tag) convertView.getTag();
        }
        tag.fill(rooms[position]);
        return convertView;

    }

    private class Tag {
        final TextView textView;
        final ImageView imageView;
        final RoomIconDrawable roomIconDrawable;

        Tag(View v) {
            roomIconDrawable = new RoomIconDrawable(context);
            textView = (TextView) v.findViewById(R.id.text);
            imageView = (ImageView) v.findViewById(R.id.icon);
            imageView.setImageDrawable(roomIconDrawable);
        }

        public void fill(Room room) {
            textView.setText(room.getType());
            roomIconDrawable.setRoom(room);
            imageView.invalidate();
        }
    }
}
