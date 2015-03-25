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
import edu.csusb.cse.roomquest.search.SearchMap;

/**
 * Created by Michael on 2/27/2015.
 *
 */
public class ResultListAdapter extends BaseAdapter {

    private final Context context;
    SearchMap.Result[] results = null;

    public ResultListAdapter(Context context) {
        this.context = context;
    }

    public void setResults(SearchMap.Result[] results) {
        this.results = results;
        notifyDataSetInvalidated();
    }

    @Override
    public int getCount() {
        if (results != null) {
            return results.length;
        } else {
            return 0;
        }
    }

    @Override
    public SearchMap.Result getItem(int position) {
        if (results != null)
            return results[position];
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
        tag.fill(results[position]);
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

        public void fill(SearchMap.Result result) {
            textView.setText(result.getName());
            roomIconDrawable.setRoom(result.getRoom());
            imageView.invalidate();
        }
    }
}
