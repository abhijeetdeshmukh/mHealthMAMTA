package com.abhijeetdeshmukh.android.mhealthmamta;

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.abhijeetdeshmukh.android.mhealthmamta.data.ProfileContract.ProfileEntry;

/*** Created by ABHIJEET on 21-06-2017.*/

public class ProfileCursorAdapter extends CursorAdapter {

    public ProfileCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Inflate a list item view using the layout specified in list_item.xml
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find individual views that we want to modify in the list item layout
        TextView nameTextView = (TextView) view.findViewById(R.id.name);
        TextView summaryTextView = (TextView) view.findViewById(R.id.summary);

        // Find the columns of profile attributes that we're interested in
        int nameColumnIndex = cursor.getColumnIndex(ProfileEntry.COLUMN_PW_1_NAME);
        int addressColumnIndex = cursor.getColumnIndex(ProfileEntry.COLUMN_PW_5_ADDRESS);

        // Read the profile attributes from the Cursor for the current profile
        String pwName = cursor.getString(nameColumnIndex);
        String pwAddress = cursor.getString(addressColumnIndex);

        // If the pet breed is empty string or null, then use some default text
        // that says "Unknown breed", so the TextView isn't blank.
        if (TextUtils.isEmpty(pwAddress)) {
            pwAddress = context.getString(R.string.unknown_address);
        }

        // Update the TextViews with the attributes for the current profile
        nameTextView.setText(pwName);
        summaryTextView.setText(pwAddress);
    }

}
