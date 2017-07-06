package com.abhijeetdeshmukh.android.mhealthmamta;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import com.abhijeetdeshmukh.android.mhealthmamta.anm.ANMPageAdapter;
import com.abhijeetdeshmukh.android.mhealthmamta.data.ProfileContract.ProfileEntry;

import java.util.Calendar;

public class ANMActivity extends AppCompatActivity {

    /** Identifier for the profile data loader */
    private static final int EXISTING_PROFILE_LOADER = 0;

    /** Content URI for the existing profile (null if it's a new profile) */
    private Uri mCurrentProfileUri;

    private String mDoaArm1String ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anm);

        setTitle(getString(R.string.anm_visit));

        // Examine the intent that was used to launch this activity,
        // in order to figure out if we're creating a new profile or editing an existing one.
//        Intent intent = getIntent();
//        mCurrentProfileUri = intent.getData();

        // Initialize a loader to read the profile data from the database
        // and display the current values in the editor
//        getLoaderManager().initLoader(EXISTING_PROFILE_LOADER, null, this);

        // Find the view pager that will allow the user to swipe between fragments
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);

        // Create an adapter that knows which fragment should be shown on each page
        ANMPageAdapter adapter = new ANMPageAdapter(this, getSupportFragmentManager());

        // Set the adapter onto the view pager
        viewPager.setAdapter(adapter);

        // Find the tab layout that shows the tabs
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        // Connect the tab layout with the view pager. This will
        //   1. Update the tab layout when the view pager is swiped
        //   2. Update the view pager when a tab is selected
        //   3. Set the tab layout's tab names with the view pager's adapter's titles
        //      by calling onPageTitle()
        tabLayout.setupWithViewPager(viewPager);

    }





    public Uri getCurrentProfileUri (){
        return mCurrentProfileUri ;
    }

    public String getmDoaArm1String () {
        return mDoaArm1String;
    }



    /*** This method is called when the back button is pressed.*/
//    @Override
//    public void onBackPressed() {
//        // If the profile hasn't changed, continue with handling back button press
//        if (!mProfileHasChanged) {
//            super.onBackPressed();
//            return;
//        }
//
//        // Otherwise if there are unsaved changes, setup a dialog to warn the user.
//        // Create a click listener to handle the user confirming that changes should be discarded.
//        DialogInterface.OnClickListener discardButtonClickListener =
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        // User clicked "Discard" button, close the current activity.
//                        finish();
//                    }
//                };
//
//        // Show dialog that there are unsaved changes
//        showUnsavedChangesDialog(discardButtonClickListener);
//    }

    /**
     * Show a dialog that warns the user there are unsaved changes that will be lost
     * if they continue leaving the FormActivity.
     *
     * @param discardButtonClickListener is the click listener for what to do when
     *                                   the user confirms they want to discard their changes
     */
    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Keep editing" button, so dismiss the dialog
                // and continue editing the profile form.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
