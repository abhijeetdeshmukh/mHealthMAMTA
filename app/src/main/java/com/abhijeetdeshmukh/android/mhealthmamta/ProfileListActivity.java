package com.abhijeetdeshmukh.android.mhealthmamta;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.abhijeetdeshmukh.android.mhealthmamta.data.ProfileContract;
import com.abhijeetdeshmukh.android.mhealthmamta.data.ProfileContract.ProfileEntry;

public class ProfileListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    /** Identifier for the profile data loader */
    private static final int PROFILE_LOADER = 0;

    /** Adapter for the ListView */
    ProfileCursorAdapter mCursorAdapter;

    private int mUserType = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_list);

        // Examine the intent that was used to launch this activity,
        // in order to figure out if we're creating a new profile or editing an existing one.
        // or we are filling ANM visit data
        Intent intent = getIntent();
        mUserType = intent.getExtras().getInt("userType");

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_new_profile);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent intent = new Intent(ProfileListActivity.this, FormActivity.class);
                    startActivity(intent);
            }
        });

        // Find the ListView which will be populated with the pet data
        ListView profileListView = (ListView) findViewById(R.id.list_profiles);

        // Find and set empty view on the ListView, so that it only shows when the list has 0 items.
        View emptyView = findViewById(R.id.empty_view);
        profileListView.setEmptyView(emptyView);

        // Setup an Adapter to create a list item for each row of pet data in the Cursor.
        // There is no profile data yet (until the loader finishes) so pass in null for the Cursor.
        mCursorAdapter = new ProfileCursorAdapter(this, null);
        profileListView.setAdapter(mCursorAdapter);

        // Setup the item click listener
        profileListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                if (mUserType == ProfileContract.LOGIN_ASHA) {
                    // Create new intent to go to {@link FormActivity}
                    Intent intent = new Intent(ProfileListActivity.this, FormActivity.class);
                    intent.putExtra("userType", mUserType);

                    // Form the content URI that represents the specific profile that was clicked on,
                    // by appending the "id" (passed as input to this method) onto the
                    // {@link ProfileEntry#CONTENT_URI}.
                    // For example, the URI would be "content://com.abhijeetdeshmukh.android.mhealthmamta/profiles/2"
                    // if the pet with ID 2 was clicked on.
                    Uri currentProfileUri = ContentUris.withAppendedId(ProfileEntry.CONTENT_URI, id);

                    // Set the URI on the data field of the intent
                    intent.setData(currentProfileUri);

                    // Launch the {@link FormActivity} to display the data for the current profile.
                    startActivity(intent);
                }

                else if (mUserType == ProfileContract.LOGIN_ANM){
                    // Create new intent to go to {@link ANMActivity}
                    Intent intent = new Intent(ProfileListActivity.this, MainActivity.class);
                    intent.putExtra("userType", mUserType);

                    // Form the content URI that represents the specific profile that was clicked on,
                    // by appending the "id" (passed as input to this method) onto the
                    // {@linkProfileEntry#CONTENT_URI}.
                    // For example, the URI would be "content://com.abhijeetdeshmukh.android.mhealthmamta/profiles/2"
                    // if the pet with ID 2 was clicked on.
                    Uri currentProfileUri = ContentUris.withAppendedId(ProfileEntry.CONTENT_URI, id);

                    // Set the URI on the data field of the intent
                    intent.setData(currentProfileUri);

                    // Launch the {@link ANMActivity} to display the ANM visit data for the current profile.
                    startActivity(intent);
                }

                else {
                    // Create new intent to go to {@link MainActivity}
                    Intent intent = new Intent(ProfileListActivity.this, MainActivity.class);
                    intent.putExtra("userType", mUserType);

                    // Form the content URI that represents the specific profile that was clicked on,
                    // by appending the "id" (passed as input to this method) onto the
                    // {@link ProfileEntry#CONTENT_URI}.
                    // For example, the URI would be "content://com.abhijeetdeshmukh.android.mhealthmamta/profiles/2"
                    // if the pet with ID 2 was clicked on.
                    Uri currentProfileUri = ContentUris.withAppendedId(ProfileEntry.CONTENT_URI, id);

                    // Set the URI on the data field of the intent
                    intent.setData(currentProfileUri);

                    // Launch the {@link MainActivity} to display the data for the current profile.
                    startActivity(intent);
                }
            }
        });

        // Kick off the loader
        getLoaderManager().initLoader(PROFILE_LOADER, null, this);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // Define a projection that specifies the columns from the table we care about.
        String[] projection = {
                ProfileEntry._ID,
                ProfileEntry.COLUMN_PW_1_NAME,
                ProfileEntry.COLUMN_PW_5_ADDRESS };

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                ProfileEntry.CONTENT_URI,   // Provider content URI to query
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Update {@link PetCursorAdapter} with this new cursor containing updated pet data
        mCursorAdapter.swapCursor(data);
    }
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // Callback called when the data needs to be deleted
        mCursorAdapter.swapCursor(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_profile_list, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                insertProfile();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                deleteAllProfiles();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /*** Helper method to insert hardcoded pet data into the database. For debugging purposes only.*/
    private void insertProfile() {
        // Create a ContentValues object where column names are the keys,
        // and Toto's pet attributes are the values.
        ContentValues values = new ContentValues();
        //TODO
        values.put(ProfileEntry.COLUMN_PW_17_AADHAR, "1234567891011");
        values.put(ProfileEntry.COLUMN_PW_1_NAME, "Geeta Charam Kar");
        values.put(ProfileEntry.COLUMN_PW_2_DOB, "18/06/1884");
        values.put(ProfileEntry.COLUMN_PW_3_AGE, "Age : 33 yrs");
        values.put(ProfileEntry.COLUMN_PW_4_HUSBAND_GUARDIAN_NAME, "Tapan Raut Ray");
        values.put(ProfileEntry.COLUMN_PW_5_ADDRESS, "Bolangir");
        values.put(ProfileEntry.COLUMN_PW_6A_GRAVIDA, "0");
        values.put(ProfileEntry.COLUMN_PW_6B_PARA, "0");
        values.put(ProfileEntry.COLUMN_PW_6C_ABORTION, "0");
        values.put(ProfileEntry.COLUMN_PW_6D_LIVING, "0");
        values.put(ProfileEntry.COLUMN_PW_7_LCB, "unknown");
        values.put(ProfileEntry.COLUMN_PW_8_LMP, "unknown");
        values.put(ProfileEntry.COLUMN_PW_9_EDD, "unknown");
        values.put(ProfileEntry.COLUMN_PW_10_MCTS_RCH, "sample MCTS RCH ");
        values.put(ProfileEntry.COLUMN_PW_11_SUBCENTRE_WARD, "sample sub centre ward ");
        values.put(ProfileEntry.COLUMN_PW_12_BLOCK_PPC, "sample Block PPC");
        values.put(ProfileEntry.COLUMN_PW_14_BLOOD_GROUP, ProfileEntry.BLOOD_GROUP_BP);
        values.put(ProfileEntry.COLUMN_PW_15_WEIGHT, 67);
        values.put(ProfileEntry.COLUMN_PW_16_HEIGHT, 152);
        values.put(ProfileEntry.COLUMN_PW_18_CATEGORY_OF_VILLAGE, ProfileEntry.VILLAGE_COMPLETELY);
        values.put(ProfileEntry.COLUMN_PW_19_DOR, "15/08/1947");

        // Insert a new row for Toto into the provider using the ContentResolver.
        // Use the {@link PetEntry#CONTENT_URI} to indicate that we want to insert
        // into the pets database table.
        // Receive the new content URI that will allow us to access Toto's data in the future.
        Uri newUri = getContentResolver().insert(ProfileEntry.CONTENT_URI, values);
    }

    /*** Helper method to delete all pets in the database.*/
    private void deleteAllProfiles() {
        int rowsDeleted = getContentResolver().delete(ProfileEntry.CONTENT_URI, null, null);
        Log.v("CatalogActivity", rowsDeleted + " rows deleted from pet database");
    }
}
