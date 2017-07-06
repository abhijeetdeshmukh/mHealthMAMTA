package com.abhijeetdeshmukh.android.mhealthmamta.anm;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.abhijeetdeshmukh.android.mhealthmamta.ANMActivity;
import com.abhijeetdeshmukh.android.mhealthmamta.R;

import java.util.Calendar;

/*** Created by ABHIJEET on 26-06-2017.*/

public class ANM1Fragment extends Fragment{

    /** Identifier for the profile data loader */
    private static final int EXISTING_PROFILE_LOADER = 0;

    /** Content URI for the existing profile (null if it's a new profile) */
    private Uri mCurrentProfileUri;

    /*** check box for c11*/
    private CheckBox mC11CheckBox ;

    private Calendar mCalendar;
    private int mYear, mMonth, mDay, mCurrentYear;

    /*** TextView field to show assessment date*/
    private TextView mAssessmentDateTextView;

    public ANM1Fragment () {
        //Requires empty public constructor
    }

    /** Boolean flag that keeps track of whether the profile has been edited (true) or not (false) */
    private boolean mProfileHasChanged = false;
    /**
     * OnTouchListener that listens for any user touches on a View, implying that they are modifying
     * the view, and we change the mProfileHasChanged boolean to true.
     */
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mProfileHasChanged = true;
            return false;
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_anm_form, container, false);

        // insert <code></code>

        ANMActivity activity = (ANMActivity) getActivity();
        mCurrentProfileUri = activity.getCurrentProfileUri();

//        if (mCurrentProfileUri != null){
//
//            // Initialize a loader to read the profile data from the database
//            // and display the current values in the editor
//            getLoaderManager().initLoader(EXISTING_PROFILE_LOADER, null, this);
//        }

        // Initialize a loader to read the profile data from the database
        // and display the current values in the editor
    //    getLoaderManager().initLoader(EXISTING_PROFILE_LOADER, null, this);                       //error last parameter this

        ///////////////////////////////////////////////////////////////////////////

        // Find all relevant views that we will need to read user input from
        mAssessmentDateTextView = (TextView) rootView.findViewById(R.id.tv_anm_assessment_date);
        mC11CheckBox = (CheckBox) rootView.findViewById(R.id.cb_c11);
        boolean hasC11 = mC11CheckBox.isChecked();


        mCalendar = Calendar.getInstance();
        mYear = mCalendar.get(Calendar.YEAR);         //current year
        mMonth = mCalendar.get(Calendar.MONTH);       //current month
        mDay = mCalendar.get(Calendar.DAY_OF_MONTH);  //current day
        showDate(mYear, mMonth+1, mDay);

        return rootView;
    }

    private void showDate(int year, int month, int day) {
        mAssessmentDateTextView.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
    }


}
