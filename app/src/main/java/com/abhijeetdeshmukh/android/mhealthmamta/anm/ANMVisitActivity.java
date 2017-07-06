package com.abhijeetdeshmukh.android.mhealthmamta.anm;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.abhijeetdeshmukh.android.mhealthmamta.FormActivity;
import com.abhijeetdeshmukh.android.mhealthmamta.R;
import com.abhijeetdeshmukh.android.mhealthmamta.data.ProfileContract.ProfileEntry;

import java.util.Calendar;

public class ANMVisitActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor>{

    /** Identifier for the profile data loader */
    private static final int EXISTING_PROFILE_LOADER = 0;

    /** Content URI for the existing profile (null if it's a new profile) */
    private Uri mCurrentProfileUri;


    /*** check box for c11*/
    private CheckBox mC11CheckBox ;
    private int mC11 = ProfileEntry.CHECKBOX_FALSE ;

    private Calendar mCalendar;
    private int mYear, mMonth, mDay, mCurrentYear;

    /*** TextView field to show assessment date*/
    private TextView mAssessmentDateTextView;


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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anm_form);

        // Examine the intent that was used to launch this activity,
        // in order to figure out if we're creating a new profile or editing an existing one.
        Intent intent = getIntent();
        mCurrentProfileUri = intent.getData();

        //TODO :
        // Find all relevant views that we will need to read user input from
        mAssessmentDateTextView = (TextView) findViewById(R.id.tv_anm_assessment_date);

        mC11CheckBox = (CheckBox) findViewById(R.id.cb_c11);






        ///////////////////////////////////////////////////////////////////////////////
        //TODO
        // Setup OnTouchListeners on all the input fields, so we can determine if the user
        // has touched or modified them. This will let us know if there are unsaved changes
        // or not, if the user tries to leave the editor without saving.

        // No need for mAssessmentDateTextView
        mC11CheckBox.setOnTouchListener(mTouchListener);


        // update above
        ////////////////////////////////////////////////////////



        // Set the integer mSelected to the constant values
        mC11CheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    mC11 = ProfileEntry.CHECKBOX_FALSE;
                }else{
                    mC11 = ProfileEntry.CHECKBOX_TRUE;
                }
            }
        });

        mCalendar = Calendar.getInstance();
        mYear = mCalendar.get(Calendar.YEAR);         //current year
        mMonth = mCalendar.get(Calendar.MONTH);       //current month
        mDay = mCalendar.get(Calendar.DAY_OF_MONTH);  //current day
        showDate(mYear, mMonth+1, mDay);

    }

    /*** Get user input from form and save profile into database.*/
    private void saveProfile() {

        /////////////////////////////////////////////////////////////////////////////////////////////
        //TODO
        // Read from input fields
        // Use trim to eliminate leading or trailing white space
        String assessmentDateString = mAssessmentDateTextView.getText().toString().trim();
        // No need for checkbox for c11 earlierPregnancies

        //TODO
        // Check if this is supposed to be a new profile
        // and check if all the fields in the editor are blank
        if (mCurrentProfileUri == null &&
                TextUtils.isEmpty(assessmentDateString) ) {
            // Since no fields were modified, we can return early without creating a new profile.
            // No need to create ContentValues and no need to do any ContentProvider operations.
            return;
        }

        ////////////////////////////////////////////////////////////////////////////////////////////
        // Create a ContentValues object where column names are the keys,
        // and pet attributes from the editor are the values.
        ContentValues values = new ContentValues();
        //TODO
        values.put(ProfileEntry.COLUMN_PW_ANM1_DOA, assessmentDateString);
        values.put(ProfileEntry.COLUMN_PW_ANM1_EARLIER_PREGNANCIES, mC11);

        // UPDATE ABOVE
        /////////////////////////////////////////////////////////////////////////////////////////////

        // Determine if this is a new or existing profile by checking if mCurrentProfileUri is null or not
        if (mCurrentProfileUri == null) {
            // This is a NEW profile, so insert a new profile into the provider,
            // returning the content URI for the new profile.
            Uri newUri = getContentResolver().insert(ProfileEntry.CONTENT_URI, values);

            // Show a toast message depending on whether or not the insertion was successful.
            if (newUri == null) {
                // If the new content URI is null, then there was an error with insertion.
                Toast.makeText(this, getString(R.string.form_insert_profile_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the insertion was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.form_insert_profile_successful),
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            // Otherwise this is an EXISTING profile, so update the profile with content URI: mCurrentProfileUri
            // and pass in the new ContentValues. Pass in null for the selection and selection args
            // because mCurrentProfileUri will already identify the correct row in the database that
            // we want to modify.
            int rowsAffected = getContentResolver().update(mCurrentProfileUri, values, null, null);

            // Show a toast message depending on whether or not the update was successful.
            if (rowsAffected == 0) {
                // If no rows were affected, then there was an error with the update.
                Toast.makeText(this, getString(R.string.form_update_profile_failef),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the update was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.form_update_profile_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // Since the form shows all profile attributes, define a projection that contains
        // all columns from the profile table
        //TODO
        String[] projection = {
                ProfileEntry._ID,
                ProfileEntry.COLUMN_PW_ANM1_DOA,
                ProfileEntry.COLUMN_PW_ANM1_EARLIER_PREGNANCIES};

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                mCurrentProfileUri,         // Query the content URI for the current pet
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        // Bail early if the cursor is null or there is less than 1 row in the cursor
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        // Proceed with moving to the first row of the cursor and reading data from it
        // (This should be the only row in the cursor)
        if (cursor.moveToFirst()) {
            //TODO
            // Find the columns of profile attributes that we're interested in
            int assessmentDateColumnIndex = cursor.getColumnIndex(ProfileEntry.COLUMN_PW_ANM1_DOA);
            int earlierPregnaciesDateColumnIndex = cursor.getColumnIndex(ProfileEntry.COLUMN_PW_ANM1_EARLIER_PREGNANCIES);

            //TODO :
            // Extract out the value from the Cursor for the given column index
            String assessmentDate = cursor.getString(assessmentDateColumnIndex);
            int c11 = cursor.getInt(earlierPregnaciesDateColumnIndex);

            //TODO :
            // Update the views on the screen with the values from the database
            mAssessmentDateTextView.setText(assessmentDate);

            // mC11CheckBox is checkbox, so map the constant value from the database
            // Then call setChecked() so that checkbox will be checked/unchecked on screen as the current selection.
            switch (c11) {
                case ProfileEntry.CHECKBOX_TRUE:
                    mC11CheckBox.setChecked(true);
                    break;
                default:
                    mC11CheckBox.setChecked(false);
                    break;
            }


        }
    }
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // If the loader is invalidated, clear out all the data from the input fields.
        //TODO
        mAssessmentDateTextView.setText("");
        mC11CheckBox.setChecked(false);
    }

    //////////////////////////////////////////////////////////////////////////
    /// completed methods -----------> need no more changes
    //////////////////////////////////////////////////////////////////////////////
    /// Methods for menu -----> Complete
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_form.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_form, menu);
        return true;
    }
    /**
     * This method is called after invalidateOptionsMenu(), so that the
     * menu can be updated (some menu items can be hidden or made visible).
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        // If this is a new profile, hide the "Delete" menu item.
        if (mCurrentProfileUri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {

            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                // Save profile to database
                saveProfile();
                // Exit activity
                finish();
                return true;

            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:
                // Pop up confirmation dialog for deletion
                showDeleteConfirmationDialog();
                return true;

            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // If the profile hasn't changed, continue with navigating up to parent activity
                // which is the {@link CatalogActivity}.
                if (!mProfileHasChanged) {
                    NavUtils.navigateUpFromSameTask(ANMVisitActivity.this);
                    return true;
                }

                // Otherwise if there are unsaved changes, setup a dialog to warn the user.
                // Create a click listener to handle the user confirming that
                // changes should be discarded.
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // User clicked "Discard" button, navigate to parent activity.
                                NavUtils.navigateUpFromSameTask(ANMVisitActivity.this);
                            }
                        };

                // Show a dialog that notifies the user they have unsaved changes
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    ///////////////////////////////////////////////////////////////////////////////////


    /*** Prompt the user to confirm that they want to delete this profile.*/
    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the profile.
                deleteProfile();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing the profile.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /*** This method is called when the back button is pressed.*/
    @Override
    public void onBackPressed() {
        // If the profile hasn't changed, continue with handling back button press
        if (!mProfileHasChanged) {
            super.onBackPressed();
            return;
        }

        // Otherwise if there are unsaved changes, setup a dialog to warn the user.
        // Create a click listener to handle the user confirming that changes should be discarded.
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User clicked "Discard" button, close the current activity.
                        finish();
                    }
                };

        // Show dialog that there are unsaved changes
        showUnsavedChangesDialog(discardButtonClickListener);
    }

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

    /*** Perform the deletion of the profile in the database.*/
    private void deleteProfile() {
        // Only perform the delete if this is an existing profile.
        if (mCurrentProfileUri != null) {
            // Call the ContentResolver to delete the profile at the given content URI.
            // Pass in null for the selection and selection args because the mCurrentPetUri
            // content URI already identifies the profile that we want.
            int rowsDeleted = getContentResolver().delete(mCurrentProfileUri, null, null);

            // Show a toast message depending on whether or not the delete was successful.
            if (rowsDeleted == 0) {
                // If no rows were deleted, then there was an error with the delete.
                Toast.makeText(this, getString(R.string.form_delete_profile_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the delete was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.form_delete_profile_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }

        // Close the activity
        finish();
    }

    private void showDate(int year, int month, int day) {
        mAssessmentDateTextView.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
    }
}
