package com.abhijeetdeshmukh.android.mhealthmamta.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.abhijeetdeshmukh.android.mhealthmamta.data.ProfileContract.ProfileEntry;

/*** Created by ABHIJEET on 21-06-2017.*/

/*** Database helper for  app. Manages database creation and version management.*/
public class ProfileDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = ProfileDbHelper.class.getSimpleName();

    /** Name of the database file */
    private static final String DATABASE_NAME = "rmrc&mamta.db";

    /*** Database version. If you change the database schema, you must increment the database version.*/
    private static final int DATABASE_VERSION = 1;

    /**
     * Constructs a new instance of {@link ProfileDbHelper}.
     *
     * @param context of the app
     */
    public ProfileDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /*** This is called when the database is created for the first time.*/
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create a String that contains the SQL statement to create the profiles table
        //TODO
        String SQL_CREATE_PETS_TABLE =  "CREATE TABLE "
                + ProfileEntry.TABLE_NAME + " ("
                + ProfileEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ProfileEntry.COLUMN_PW_17_AADHAR + " TEXT , "                 //considered as text because 12digit integer was causing a problem
                + ProfileEntry.COLUMN_PW_1_NAME + " TEXT NOT NULL, "
                + ProfileEntry.COLUMN_PW_2_DOB + " TEXT, "
                + ProfileEntry.COLUMN_PW_3_AGE + " TEXT , "
                + ProfileEntry.COLUMN_PW_4_HUSBAND_GUARDIAN_NAME + " TEXT NOT NULL, "
                + ProfileEntry.COLUMN_PW_5_ADDRESS + " TEXT, "
                + ProfileEntry.COLUMN_PW_6A_GRAVIDA + " INTEGER DEFAULT 0, "
                + ProfileEntry.COLUMN_PW_6B_PARA + " INTEGER DEFAULT 0, "
                + ProfileEntry.COLUMN_PW_6C_ABORTION + " INTEGER DEFAULT 0, "
                + ProfileEntry.COLUMN_PW_6D_LIVING + " INTEGER DEFAULT 0, "
                + ProfileEntry.COLUMN_PW_7_LCB + " TEXT, "
                + ProfileEntry.COLUMN_PW_8_LMP + " TEXT, "
                + ProfileEntry.COLUMN_PW_9_EDD + " TEXT, "
                + ProfileEntry.COLUMN_PW_10_MCTS_RCH + " TEXT, "
                + ProfileEntry.COLUMN_PW_11_SUBCENTRE_WARD + " TEXT, "
                + ProfileEntry.COLUMN_PW_12_BLOCK_PPC + " TEXT, "
                + ProfileEntry.COLUMN_PW_14_BLOOD_GROUP + " INTEGER NOT NULL DEFAULT 0, "
                // column_pw_15_ at the end of this string
                + ProfileEntry.COLUMN_PW_16_HEIGHT + " INTEGER DEFAULT 0, "
                //cloumn_pw_17_  at start of this string
                + ProfileEntry.COLUMN_PW_18_CATEGORY_OF_VILLAGE + " INTEGER DEFAULT 0, "
                + ProfileEntry.COLUMN_PW_19_DOR + " TEXT, "
                + ProfileEntry.COLUMN_PW_ANM1_DOA + " TEXT, "
                + ProfileEntry.COLUMN_PW_ANM1_EARLIER_PREGNANCIES + " INTEGER DEFAULT 0, "
                + ProfileEntry.COLUMN_PW_15_WEIGHT + " INTEGER NOT NULL DEFAULT 0);";

        // Execute the SQL statement
        db.execSQL(SQL_CREATE_PETS_TABLE);
    }

    /*** This is called when the database needs to be upgraded.*/
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // The database is still at version 1, so there's nothing to do be done here.
    }

}
