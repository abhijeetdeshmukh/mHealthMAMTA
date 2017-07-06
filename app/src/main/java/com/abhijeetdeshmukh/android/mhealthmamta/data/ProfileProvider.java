package com.abhijeetdeshmukh.android.mhealthmamta.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.abhijeetdeshmukh.android.mhealthmamta.data.ProfileContract.ProfileEntry;

/*** Created by ABHIJEET on 21-06-2017.*/

/*** {@link ContentProvider} for Pets app.*/
public class ProfileProvider extends ContentProvider {

    /** Tag for the log messages */
    public static final String LOG_TAG = ProfileProvider.class.getSimpleName();

    /** URI matcher code for the content URI for the profiles table */
    private static final int PROFILES = 100;

    /** URI matcher code for the content URI for a single pet in the pets table */
    private static final int PROFILE_ID = 101;

    /**
     * UriMatcher object to match a content URI to a corresponding code.
     * The input passed into the constructor represents the code to return for the root URI.
     * It's common to use NO_MATCH as the input for this case.
     */
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    // Static initializer. This is run the first time anything is called from this class.
    static {

        // The calls to addURI() go here, for all of the content URI patterns that the provider
        // should recognize. All paths added to the UriMatcher have a corresponding code to return
        // when a match is found.

        // The content URI of the form "content://com.abhijeetdeshmukh.android.mhealthmamta/profiles"
        // will map to the integer code {@link #PROFILES}. This URI is used to provide access
        // to MULTIPLE rows of the profiles table.
        sUriMatcher.addURI(ProfileContract.CONTENT_AUTHORITY, ProfileContract.PATH_PROFILES, PROFILES);

        // The content URI of the form "content://com.abhijeetdeshmukh.android.mhealthmamta/profiles/#"
        // will map to the integer code {@link #PROFILE_ID}. This URI is used to provide access to
        // ONE single row of the profiles table.
        //
        // In this case, the "#" wildcard is used where "#" can be substituted for an integer.
        // For example, "content://com.abhijeetdeshmukh.android.mhealthmamta/profiles/3" matches, but
        // "content://com.abhijeetdeshmukh.android.mhealthmamta/profiles" (without a number at the end)
        // doesn't match.
        sUriMatcher.addURI(ProfileContract.CONTENT_AUTHORITY, ProfileContract.PATH_PROFILES + "/#", PROFILE_ID);
    }

    /** Database helper object */
    private ProfileDbHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new ProfileDbHelper(getContext());
        return true;
    }

    private Uri insertProfile(Uri uri, ContentValues values) {
        ///////////////////////////////////////////////////////////////////////////
        //TODO :
        // If the adhaar number is provided, check that it's greater than or equal to 0 kg
        // *** considering aadhar number as a string because 12-digit number was crashing app during testing
        String aadhar = values.getAsString(ProfileEntry.COLUMN_PW_17_AADHAR);
        if (aadhar == null ) {
            throw new IllegalArgumentException("Pregnant woman requires valid aadhar number");
        }

        // Check that the name is not null
        String name = values.getAsString(ProfileEntry.COLUMN_PW_1_NAME);
        if (name == null) {
            throw new IllegalArgumentException("Pregnant woman requires a name");
        }

//        No need to Check that for DOB
//        No need to Check that the age is not null

        // Check that the husband/guardian name is not null
        String husbandGuardian = values.getAsString(ProfileEntry.COLUMN_PW_4_HUSBAND_GUARDIAN_NAME);
        if (husbandGuardian == null) {
            throw new IllegalArgumentException("Husband/Guardian requires a name");
        }

        // No need to check the address, any value is valid (including null).
        // no need to check for GPAL field
        // Check that the LCB is not null
        String lcb = values.getAsString(ProfileEntry.COLUMN_PW_7_LCB);
        if (lcb == null) {
            lcb = "unknown LCB Date";
        }

        //no need to check for LMP field
        //no need to check for EDD field
        //no need to check for MCTS/RCH field
        //no need to check for Sub-Centre/Ward field
        //no need to check for Block/PPC field

        // Check that the blood group is valid
        Integer bloodGroup = values.getAsInteger(ProfileEntry.COLUMN_PW_14_BLOOD_GROUP);
        if (bloodGroup == null || !ProfileEntry.isValidBloodGroup(bloodGroup)) {
            throw new IllegalArgumentException("Pregnant women requires valid blood group");
        }

        // If the weight is provided, check that it's greater than or equal to 0 kg
        Integer weight = values.getAsInteger(ProfileEntry.COLUMN_PW_15_WEIGHT);
        if (weight != null && weight < 0) {
            throw new IllegalArgumentException("Pregnant woman requires valid weight");
        }

        // If the height is provided, check that it's greater than or equal to 0 cm
        Integer height = values.getAsInteger(ProfileEntry.COLUMN_PW_16_HEIGHT);
        if (height != null && height < 0) {
            throw new IllegalArgumentException("Pregnant woman requires valid height");
        }

        // Check that the Village category is valid
        Integer villageCategory = values.getAsInteger(ProfileEntry.COLUMN_PW_18_CATEGORY_OF_VILLAGE);
        if (villageCategory == null || !ProfileEntry.isValidVillageCategory(villageCategory)) {
            throw new IllegalArgumentException("pregnant women requires valid Village category");
        }

        //no need to check for Date of registration (DOR) field

        //no need to check for Date of assessment (DOA) field (ANM1)

        ///////////////////////////////////////////////////////////////////////////////////////

        // Get writeable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Insert the new profile with the given values
        long id = database.insert(ProfileEntry.TABLE_NAME, null, values);
        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        // Notify all listeners that the data has changed for the profile content URI
        getContext().getContentResolver().notifyChange(uri, null);

        // Return the new URI with the ID (of the newly inserted row) appended at the end
        return ContentUris.withAppendedId(uri, id);
    }

    /**
     * Update profiles in the database with the given content values. Apply the changes to the rows
     * specified in the selection and selection arguments (which could be 0 or 1 or more profiles).
     * Return the number of rows that were successfully updated.
     */
    private int updateProfile(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        ////////////////////////////////////////////////////////////////////////////////////////////
        //TODO
        // If the {@link ProfileEntry#COLUMN_PW_17_AADHAR} key is present,
        // check that the aadhar number value is valid
        if (values.containsKey(ProfileEntry.COLUMN_PW_17_AADHAR)) {
            // If the adhar number is provided, check that it's greater than or equal to 0
            String aadhar = values.getAsString(ProfileEntry.COLUMN_PW_17_AADHAR);
            if (aadhar == null ) {
                throw new IllegalArgumentException("Human requires valid adhar number");
            }
        }

        // If the {@link ProfileEntry#COLUMN_PW_1_NAME} key is present,
        // check that the name value is not null.
        if (values.containsKey(ProfileEntry.COLUMN_PW_1_NAME)) {
            String name = values.getAsString(ProfileEntry.COLUMN_PW_1_NAME);
            if (name == null) {
                throw new IllegalArgumentException("Pregnant woman requires a name");
            }
        }

//      // no need to check for DOB
//      // No need to check the age because we are calculating it from date of birth.

        // If the {@link ProfileEntry#COLUMN_PW_4_HUSBAND_GUARDIAN_NAME} key is present,
        // check that the name value is not null.
        if (values.containsKey(ProfileEntry.COLUMN_PW_4_HUSBAND_GUARDIAN_NAME)) {
            String name = values.getAsString(ProfileEntry.COLUMN_PW_4_HUSBAND_GUARDIAN_NAME);
            if (name == null) {
                throw new IllegalArgumentException("Husband/Guardian requires a name");
            }
        }

        // No need to check the address, any value is valid (including null).
        //no need to check for GPAL field

        // If the {@link ProfileEntry#COLUMN_PW_7_LCB} key is present,
        // check that the lcb value is not null.
        if (values.containsKey(ProfileEntry.COLUMN_PW_7_LCB)) {
            String lcb = values.getAsString(ProfileEntry.COLUMN_PW_7_LCB);
            if (lcb == null) {
                lcb = "unknown LCB Date";
            }
        }

        //no need to check for LMP field
        //no need to check for EDD field
        //no need to check for MCTS/RCH field
        //no need to check for Sub-Centre/Ward field
        //no need to check for Block/PPC field

        // If the {@link ProfileEntry#COLUMN_PW_14_BLOOD_GROUP} key is present,
        // check that the blood group value is valid.
        if (values.containsKey(ProfileEntry.COLUMN_PW_14_BLOOD_GROUP)) {
            Integer bloodGroup = values.getAsInteger(ProfileEntry.COLUMN_PW_14_BLOOD_GROUP);
            if (bloodGroup == null || !ProfileEntry.isValidBloodGroup(bloodGroup)) {
                throw new IllegalArgumentException("Pregnant woman requires valid gender");
            }
        }

        // If the {@link ProfileEntry#COLUMN_PW_15_WEIGHT} key is present,
        // check that the weight value is valid
        if (values.containsKey(ProfileEntry.COLUMN_PW_15_WEIGHT)) {
            // Check that the weight is greater than or equal to 0 kg
            Integer weight = values.getAsInteger(ProfileEntry.COLUMN_PW_15_WEIGHT);
            if (weight != null && weight < 0) {
                throw new IllegalArgumentException("Pregnant woman valid weight");
            }
        }

        // If the {@link ProfileEntry#COLUMN_PW_18_CATEGORY_OF_VILLAGE} key is present,
        // check that the village category value is valid
        if (values.containsKey(ProfileEntry.COLUMN_PW_18_CATEGORY_OF_VILLAGE)) {
            // Check that the Village category is valid
            Integer villageCategory = values.getAsInteger(ProfileEntry.COLUMN_PW_18_CATEGORY_OF_VILLAGE);
            if (villageCategory == null || !ProfileEntry.isValidVillageCategory(villageCategory)) {
                throw new IllegalArgumentException("Human requires valid Village category");
            }
        }

        //no need to check for Date of registration (DOR) field

        //no need to check for Date of assessment (DOA) field (ANM 1)

        // update above
        ////////////////////////////////////////////////////////////////////////////////////

        // If there are no values to update, then don't try to update the database
        if (values.size() == 0) {
            return 0;
        }

        // Otherwise, get writeable database to update the data
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Perform the update on the database and get the number of rows affected
        int rowsUpdated = database.update(ProfileEntry.TABLE_NAME, values, selection, selectionArgs);

        // If 1 or more rows were updated, then notify all listeners that the data at the
        // given URI has changed
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of rows updated
        return rowsUpdated;
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////
    // completed methods -----------> no need to updated
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        // Get readable database
        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        // This cursor will hold the result of the query
        Cursor cursor;

        // Figure out if the URI matcher can match the URI to a specific code
        int match = sUriMatcher.match(uri);
        switch (match) {
            case PROFILES:
                // For the PROFILES code, query the profiles table directly with the given
                // projection, selection, selection arguments, and sort order. The cursor
                // could contain multiple rows of the profiles table.
                cursor = database.query(ProfileEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case PROFILE_ID:
                // For the PROFILE_ID code, extract out the ID from the URI.
                // For an example URI such as "content://com.abhijeetdeshmukh.android.mhealthmamta/profiles/3",
                // the selection will be "_id=?" and the selection argument will be a
                // String array containing the actual ID of 3 in this case.
                //
                // For every "?" in the selection, we need to have an element in the selection
                // arguments that will fill in the "?". Since we have 1 question mark in the
                // selection, we have 1 String in the selection arguments' String array.

                selection = ProfileEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };


                cursor = database.query(ProfileEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        // Set notification URI on the Cursor,
        // so we know what content URI the Cursor was created for.
        // If the data at this URI changes, then we know we need to update the Cursor.
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        // Return the cursor
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PROFILES:
                return insertProfile(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection,
                      String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PROFILES:
                return updateProfile(uri, contentValues, selection, selectionArgs);
            case PROFILE_ID:
                // For the PROFILE_ID code, extract out the ID from the URI,
                // so we know which row to update. Selection will be "_id=?" and selection
                // arguments will be a String array containing the actual ID.
                selection = ProfileEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updateProfile(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Get writeable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Track the number of rows that were deleted
        int rowsDeleted;

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PROFILES:
                // Delete all rows that match the selection and selection args
                rowsDeleted = database.delete(ProfileEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case PROFILE_ID:
                // Delete a single row given by the ID in the URI
                selection = ProfileEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsDeleted = database.delete(ProfileEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }

        // If 1 or more rows were deleted, then notify all listeners that the data at the
        // given URI has changed
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of rows deleted
        return rowsDeleted;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PROFILES:
                return ProfileEntry.CONTENT_LIST_TYPE;
            case PROFILE_ID:
                return ProfileEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////
}
