package com.shankarlabs.carlog.ui;


import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.shankarlabs.carlog.R;

public class RawStatsRowAdapter extends CursorAdapter {

    private LayoutInflater layoutInflater;
    private Cursor rawStatsCursor;
    private final String LOGTAG = "CarLog";

    public RawStatsRowAdapter(Context context, Cursor cursor) {
        super(context, cursor);
        layoutInflater = LayoutInflater.from(context);
        rawStatsCursor = cursor;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return layoutInflater.inflate(R.layout.fillupstatisticrow, viewGroup, false); // Do not attach to root
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (!mDataValid) { // CursorAdapter maintains this variable, if the cursor is valid
            throw new IllegalStateException("The Cursor/Data is not valid");
        }

        if (!mCursor.moveToPosition(position)) {
            throw new IllegalStateException("Cannot move cursor to position " + position);
        }

        View v;
        if(convertView == null) {
            v = newView(mContext, rawStatsCursor, parent); // CursorAdapter also has an mContext object that we can use
        } else {
            v = convertView;
        }
        bindView(v, mContext, rawStatsCursor);
        return v;
    }

    @Override
    public void bindView(View rowView, Context context, Cursor cursor) {

        TextView rowId = (TextView) rowView.findViewById(R.id.rowid);
        rowId.setText("" + cursor.getInt(0));

        TextView volumeStat = (TextView) rowView.findViewById(R.id.volumestat);
        volumeStat.setText(cursor.getString(1));

        TextView priceStat = (TextView) rowView.findViewById(R.id.pricestat);
        priceStat.setText(cursor.getString(2));

        TextView costStat = (TextView) rowView.findViewById(R.id.coststat);
        float cost = Float.parseFloat(cursor.getString(1)) * Float.parseFloat(cursor.getString(2));
        costStat.setText("" + cost);

        TextView dateStat = (TextView) rowView.findViewById(R.id.datestat);
        dateStat.setText(cursor.getString(3));

        TextView partialStat = (TextView) rowView.findViewById(R.id.partialstat);
        partialStat.setText(cursor.getInt(4) == 0 ? "No" : "Yes");
    }
}
