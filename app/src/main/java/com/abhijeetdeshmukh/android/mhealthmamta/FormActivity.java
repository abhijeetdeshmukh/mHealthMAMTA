package com.abhijeetdeshmukh.android.mhealthmamta;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.abhijeetdeshmukh.android.mhealthmamta.data.ProfileContract.ProfileEntry;

import java.util.Calendar;

/*** Allows user to create a new profile or edit an existing one.*/
public class FormActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor>{

    /** Identifier for the profile data loader */
    private static final int EXISTING_PROFILE_LOADER = 0;

    /** Content URI for the existing profile (null if it's a new profile) */
    private Uri mCurrentProfileUri;

    //////////////////////////////////////////////
    /** EditText field to enter Aadhar number */
    private EditText mAadharEditText ;

   /** EditText fields to enter the pregnant woman's name */
    private EditText mNamePWEditText;

    /** Fields and variables for bate of birth and age */
    private Button mDOBButton ;
    private TextView mAgeTextView ;
    private int mAgeYears ;
    private DatePicker mDatePicker;
    private Calendar mCalendar;
    private int mYear, mMonth, mDay, mCurrentYear;

    /** EditText field to enter the husband/guardian name*/
    private EditText mHusbandGuardianEditText ;

    /** EditText fields to enter G-P-A-L */
    private EditText mGravidaEditText, mParaEditText, mAbortionEditText, mLivingEditText ;

    /** EditText fields to enter the address */
    private EditText mAddressEditText;

    /** Button field to select Last child birth date */
    private Button mLCBButton;

    /** Button field to select LMP date */
    private Button mLMPButton;

    /** Button field to select EDD date */
    private Button mEDDButton;

    /** EditText field to enter the pregnant woman's MCTS/RCH number */
    private EditText mMctsRchEditText;

    /** EditText field to enter the pregnant woman's Sub-center ward */
    private EditText mSubCenterEditText;

    /** EditText field to enter the pregnant woman's Block/PPC */
    private EditText mBlockPPCEditText;

    /** spinner to choose blood group type */
    private Spinner mBloodGroupTypeSpinner;

    /** EditText field to enter the pregnant woman's weight */
    private EditText mWeightEditText;

    /** EditText field to enter the pregnant woman's height */
    private EditText mHeightEditText;

    /** spinner to choose village category type */
    private Spinner mVillageCategorySpinner;

    /*** TextView field to show profile creation date*/
    private TextView mProfileRegistrationDateTextView ;

    /////////////////////////////////////////////////

    /**
     * Blood Group Type. The possible valid values are in the ProfileContract.java file:
     *
     * The only possible values are {@link ProfileEntry#BLOOD_GROUP_AP}, {@link ProfileEntry#BLOOD_GROUP_AN},
     * {@link ProfileEntry#BLOOD_GROUP_BP} , {@link ProfileEntry#BLOOD_GROUP_BN}, {@link ProfileEntry#BLOOD_GROUP_ABP},
     * {@link ProfileEntry#BLOOD_GROUP_ABN}, {@link ProfileEntry#BLOOD_GROUP_OP} , or {@link ProfileEntry#BLOOD_GROUP_ON}
     */
    private int mBloodGroupType = ProfileEntry.BLOOD_GROUP_AP;

    /** Village Category Type. The possible valid values are in the ProfileContract.java file:
     *
     * The only possible values are {@link ProfileEntry#VILLAGE_COMPLETELY}, {@link ProfileEntry#VILLAGE_PARTIALLY}
     */
    private int mVillageType = ProfileEntry.VILLAGE_COMPLETELY ;

    /*** Possible values for setting dates in profile form.*/
    public static final int DATE_PROFILE_CREATION = 0;
    public static final int DATE_DOB = 1;
    public static final int DATE_LCB = 2;
    public static final int DATE_LMP = 3;
    public static final int DATE_EDD = 4;

    private int mDateType = DATE_PROFILE_CREATION ;

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
        setContentView(R.layout.activity_form);

        // Examine the intent that was used to launch this activity,
        // in order to figure out if we're creating a new profile or editing an existing one.
        Intent intent = getIntent();
        mCurrentProfileUri = intent.getData();

        // If the intent DOES NOT contain a profile content URI, then we know that we are
        // creating a new profile.
        if (mCurrentProfileUri == null) {
            // This is a new profile, so change the app bar to say "Add a Profile"
            setTitle(getString(R.string.form_activity_title_new_profile));

            // Invalidate the options menu, so the "Delete" menu option can be hidden.
            // (It doesn't make sense to delete a profile that hasn't been created yet.)
            invalidateOptionsMenu();
        } else {
            // Otherwise this is an existing profile, so change app bar to say "Edit Profile"
            setTitle(getString(R.string.form_activity_title_edit_profile));

            // Initialize a loader to read the profile data from the database
            // and display the current values in the editor
            getLoaderManager().initLoader(EXISTING_PROFILE_LOADER, null, this);
        }

        ////////////////////////////////////////////////////////////////////////////////////////////
        //TODO
        // Find all relevant views that we will need to read user input from
        mAadharEditText = (EditText) findViewById(R.id.et_pw_aadhaar);
        mNamePWEditText = (EditText) findViewById(R.id.et_pw_name);//
        mDOBButton = (Button) findViewById(R.id.btn_pw_dob);
        mAgeTextView = (TextView) findViewById(R.id.tv_pw_age);
        mAgeTextView.setText("Age unknown");
        mHusbandGuardianEditText = (EditText) findViewById(R.id.et_husband_guardian_name) ;
        mAddressEditText = (EditText) findViewById(R.id.et_pw_address);
        mGravidaEditText = (EditText) findViewById(R.id.et_pw_gravida);
        mParaEditText = (EditText) findViewById(R.id.et_pw_para);
        mAbortionEditText = (EditText) findViewById(R.id.et_pw_abortion);
        mLivingEditText = (EditText) findViewById(R.id.et_pw_living);
        mLCBButton = (Button) findViewById(R.id.btn_pw_lcb);
        mLMPButton = (Button) findViewById(R.id.btn_pw_lmp);
        mEDDButton = (Button) findViewById(R.id.btn_pw_edd);
        mMctsRchEditText = (EditText) findViewById(R.id.et_pw_MCTS_RCH);
        mSubCenterEditText = (EditText) findViewById(R.id.et_pw_subCentre_ward);
        mBlockPPCEditText = (EditText) findViewById(R.id.et_pw_Block_PPC);
        mBloodGroupTypeSpinner = (Spinner) findViewById(R.id.spinner_blood_group);
        mWeightEditText = (EditText) findViewById(R.id.et_pw_weight);
        mHeightEditText = (EditText) findViewById(R.id.et_pw_height) ;
        mVillageCategorySpinner = (Spinner) findViewById(R.id.spinner_village_category);
        mProfileRegistrationDateTextView = (TextView) findViewById(R.id.tv_profile_registration_date);

        ///////////////////////////////////////////////////////////////////////////////
        //TODO
        // Setup OnTouchListeners on all the input fields, so we can determine if the user
        // has touched or modified them. This will let us know if there are unsaved changes
        // or not, if the user tries to leave the editor without saving.
        mAadharEditText.setOnTouchListener(mTouchListener);
        mNamePWEditText.setOnTouchListener(mTouchListener);
        mDOBButton.setOnTouchListener(mTouchListener);
        // No need for mAgeTextView
        mHusbandGuardianEditText.setOnTouchListener(mTouchListener);
        mAddressEditText.setOnTouchListener(mTouchListener);
        mGravidaEditText.setOnTouchListener(mTouchListener);
        mParaEditText.setOnTouchListener(mTouchListener);
        mAbortionEditText.setOnTouchListener(mTouchListener);
        mLivingEditText.setOnTouchListener(mTouchListener);
        mLCBButton.setOnTouchListener(mTouchListener);
        mLMPButton.setOnTouchListener(mTouchListener);
        mEDDButton.setOnTouchListener(mTouchListener);
        mMctsRchEditText.setOnTouchListener(mTouchListener);
        mSubCenterEditText.setOnTouchListener(mTouchListener);
        mBlockPPCEditText.setOnTouchListener(mTouchListener);
        mBloodGroupTypeSpinner.setOnTouchListener(mTouchListener);
        mWeightEditText.setOnTouchListener(mTouchListener);
        mHeightEditText.setOnTouchListener(mTouchListener);
        mVillageCategorySpinner.setOnTouchListener(mTouchListener);
        // No need for mProfileRegistrationDateTextView

        // update above
        ////////////////////////////////////////////////////////

        mCalendar = Calendar.getInstance();
        mYear = mCalendar.get(Calendar.YEAR);         //current year
        mMonth = mCalendar.get(Calendar.MONTH);       //current month
        mDay = mCalendar.get(Calendar.DAY_OF_MONTH);  //current day

        mCurrentYear = mYear ;
        showDate(mYear, mMonth+1, mDay);

        mDOBButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mDateType = DATE_DOB;
                setDate(v);
            }
        });

        mLCBButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mDateType = DATE_LCB;
                setDate(v);
            }
        });

        mLMPButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mDateType = DATE_LMP;
                setDate(v);
            }
        });

        mEDDButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mDateType = DATE_EDD;
                setDate(v);
            }
        });

        setupBloodGroupSpinner();
        setupVillageCategorySpinner();
    }

    /*** Get user input from form and save profile into database.*/
    private void saveProfile() {

        /////////////////////////////////////////////////////////////////////////////////////////////
        //TODO
        // Read from input fields
        // Use trim to eliminate leading or trailing white space
        String aadharString = mAadharEditText.getText().toString().trim();
        String nameString = mNamePWEditText.getText().toString().trim();
        String dobString = mDOBButton.getText().toString().trim();
        String ageString = mAgeTextView.getText().toString().trim();
        String husbandGuardianString = mHusbandGuardianEditText.getText().toString().trim();
        String addressString = mAddressEditText.getText().toString().trim();
        String gravidaString = mGravidaEditText.getText().toString().trim();
        String paraString = mParaEditText.getText().toString().trim();
        String abortionString = mAbortionEditText.getText().toString().trim();
        String livingString = mLivingEditText.getText().toString().trim();
        String lcbString = mLCBButton.getText().toString().trim();
        String lmpString = mLMPButton.getText().toString().trim();
        String eddString = mEDDButton.getText().toString().trim();
        String mctsRchString = mMctsRchEditText.getText().toString().trim();
        String subCentreWardString = mSubCenterEditText.getText().toString().trim();
        String blockPpcString = mBlockPPCEditText.getText().toString().trim();
        //no need for blood group
        String weightString = mWeightEditText.getText().toString().trim();
        String heightString = mHeightEditText.getText().toString().trim();
        String profileRegistrationDateString = mProfileRegistrationDateTextView.getText().toString().trim();

        //TODO
        // Check if this is supposed to be a new profile
        // and check if all the fields in the editor are blank
        if (mCurrentProfileUri == null &&
                TextUtils.isEmpty(aadharString) &&
                TextUtils.isEmpty(nameString) &&
                TextUtils.isEmpty(dobString) &&
                TextUtils.isEmpty(husbandGuardianString) &&
                TextUtils.isEmpty(addressString) &&
                TextUtils.isEmpty(gravidaString) &&
                TextUtils.isEmpty(paraString) &&
                TextUtils.isEmpty(abortionString) &&
                TextUtils.isEmpty(lcbString) &&
                TextUtils.isEmpty(lmpString) &&
                TextUtils.isEmpty(eddString) &&
                TextUtils.isEmpty(livingString) &&
                TextUtils.isEmpty(mctsRchString) &&
                TextUtils.isEmpty(subCentreWardString) &&
                TextUtils.isEmpty(blockPpcString) &&
                TextUtils.isEmpty(weightString) &&
                TextUtils.isEmpty(heightString) &&
                mVillageType == ProfileEntry.VILLAGE_COMPLETELY &&
                mBloodGroupType == ProfileEntry.BLOOD_GROUP_BP &&
                TextUtils.isEmpty(profileRegistrationDateString) ) {
            // Since no fields were modified, we can return early without creating a new profile.
            // No need to create ContentValues and no need to do any ContentProvider operations.
            return;
        }

        ////////////////////////////////////////////////////////////////////////////////////////////
        // Create a ContentValues object where column names are the keys,
        // and pet attributes from the editor are the values.
        ContentValues values = new ContentValues();
        //TODO
        values.put(ProfileEntry.COLUMN_PW_17_AADHAR, aadharString);
        values.put(ProfileEntry.COLUMN_PW_1_NAME, nameString);
        values.put(ProfileEntry.COLUMN_PW_2_DOB, dobString);
        values.put(ProfileEntry.COLUMN_PW_3_AGE, ageString);
        values.put(ProfileEntry.COLUMN_PW_4_HUSBAND_GUARDIAN_NAME, husbandGuardianString);
        values.put(ProfileEntry.COLUMN_PW_5_ADDRESS, addressString);

        // If the G-P-A-L values is not provided by the user, don't try to parse the string into an
        // integer value. Use 0 by default.
        int gravida = 0 ;
        if (!TextUtils.isEmpty(gravidaString)) {
            gravida = Integer.parseInt(gravidaString);
        }
        values.put(ProfileEntry.COLUMN_PW_6A_GRAVIDA, gravida);
        int  para = 0 ;
        if (!TextUtils.isEmpty(paraString)) {
            para = Integer.parseInt(paraString);
        }
        values.put(ProfileEntry.COLUMN_PW_6B_PARA, para);
        int  abortion = 0 ;
        if (!TextUtils.isEmpty(abortionString)) {
            abortion = Integer.parseInt(abortionString);
        }
        values.put(ProfileEntry.COLUMN_PW_6C_ABORTION, abortion);
        int  living = 0 ;
        if (!TextUtils.isEmpty(livingString)) {
            living = Integer.parseInt(livingString);
        }
        values.put(ProfileEntry.COLUMN_PW_6D_LIVING, living);

        values.put(ProfileEntry.COLUMN_PW_7_LCB, lcbString);
        values.put(ProfileEntry.COLUMN_PW_8_LMP, lmpString);
        values.put(ProfileEntry.COLUMN_PW_9_EDD, eddString);

        values.put(ProfileEntry.COLUMN_PW_10_MCTS_RCH, mctsRchString);
        values.put(ProfileEntry.COLUMN_PW_11_SUBCENTRE_WARD, subCentreWardString);
        values.put(ProfileEntry.COLUMN_PW_12_BLOCK_PPC, blockPpcString);

        values.put(ProfileEntry.COLUMN_PW_14_BLOOD_GROUP, mBloodGroupType);

        // If the weight is not provided by the user, don't try to parse the string into an
        // integer value. Use 0 by default.
        int weight = 0;
        if (!TextUtils.isEmpty(weightString)) {
            weight = Integer.parseInt(weightString);
        }
        values.put(ProfileEntry.COLUMN_PW_15_WEIGHT, weight);

        // If the height is not provided by the user, don't try to parse the string into an
        // integer value. Use 0 by default.
        int height = 0;
        if (!TextUtils.isEmpty(weightString)) {
            height = Integer.parseInt(heightString);
        }
        values.put(ProfileEntry.COLUMN_PW_16_HEIGHT, height);

        values.put(ProfileEntry.COLUMN_PW_18_CATEGORY_OF_VILLAGE, mVillageType);

        values.put(ProfileEntry.COLUMN_PW_19_DOR, profileRegistrationDateString);

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
                ProfileEntry.COLUMN_PW_17_AADHAR,
                ProfileEntry.COLUMN_PW_1_NAME,
                ProfileEntry.COLUMN_PW_2_DOB,
                ProfileEntry.COLUMN_PW_3_AGE,
                ProfileEntry.COLUMN_PW_4_HUSBAND_GUARDIAN_NAME,
                ProfileEntry.COLUMN_PW_5_ADDRESS,
                ProfileEntry.COLUMN_PW_6A_GRAVIDA,
                ProfileEntry.COLUMN_PW_6B_PARA,
                ProfileEntry.COLUMN_PW_6C_ABORTION,
                ProfileEntry.COLUMN_PW_6D_LIVING,
                ProfileEntry.COLUMN_PW_7_LCB,
                ProfileEntry.COLUMN_PW_8_LMP,
                ProfileEntry.COLUMN_PW_9_EDD,
                ProfileEntry.COLUMN_PW_10_MCTS_RCH,
                ProfileEntry.COLUMN_PW_11_SUBCENTRE_WARD,
                ProfileEntry.COLUMN_PW_12_BLOCK_PPC,
                ProfileEntry.COLUMN_PW_14_BLOOD_GROUP,
                ProfileEntry.COLUMN_PW_15_WEIGHT,
                ProfileEntry.COLUMN_PW_16_HEIGHT,
                ProfileEntry.COLUMN_PW_18_CATEGORY_OF_VILLAGE,
                ProfileEntry.COLUMN_PW_19_DOR};

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
            int aadharColumnIndex = cursor.getColumnIndex(ProfileEntry.COLUMN_PW_17_AADHAR);
            int nameColumnIndex = cursor.getColumnIndex(ProfileEntry.COLUMN_PW_1_NAME);
            int dobColumnIndex = cursor.getColumnIndex(ProfileEntry.COLUMN_PW_2_DOB);
            int ageColumnIndex = cursor.getColumnIndex(ProfileEntry.COLUMN_PW_3_AGE);
            int husbandGuardianColumnIndex = cursor.getColumnIndex(ProfileEntry.COLUMN_PW_4_HUSBAND_GUARDIAN_NAME);
            int addressColumnIndex = cursor.getColumnIndex(ProfileEntry.COLUMN_PW_5_ADDRESS);
            int gravidaColumnIndex = cursor.getColumnIndex(ProfileEntry.COLUMN_PW_6A_GRAVIDA);
            int paraColumnIndex = cursor.getColumnIndex(ProfileEntry.COLUMN_PW_6B_PARA);
            int abortionColumnIndex = cursor.getColumnIndex(ProfileEntry.COLUMN_PW_6C_ABORTION);
            int livingColumnIndex = cursor.getColumnIndex(ProfileEntry.COLUMN_PW_6D_LIVING);
            int lcbColumnIndex = cursor.getColumnIndex(ProfileEntry.COLUMN_PW_7_LCB);
            int lmpColumnIndex = cursor.getColumnIndex(ProfileEntry.COLUMN_PW_8_LMP);
            int eddColumnIndex = cursor.getColumnIndex(ProfileEntry.COLUMN_PW_9_EDD);
            int mcthRchColumnIndex = cursor.getColumnIndex(ProfileEntry.COLUMN_PW_10_MCTS_RCH);
            int subcenterWardColumnIndex = cursor.getColumnIndex(ProfileEntry.COLUMN_PW_11_SUBCENTRE_WARD);
            int blockPPCColumnIndex = cursor.getColumnIndex(ProfileEntry.COLUMN_PW_12_BLOCK_PPC);
            int bloodGroupColumnIndex = cursor.getColumnIndex(ProfileEntry.COLUMN_PW_14_BLOOD_GROUP);
            int weightColumnIndex = cursor.getColumnIndex(ProfileEntry.COLUMN_PW_15_WEIGHT);
            int heightColumnIndex = cursor.getColumnIndex(ProfileEntry.COLUMN_PW_16_HEIGHT);
            int villageCategoryColumnIndex = cursor.getColumnIndex(ProfileEntry.COLUMN_PW_18_CATEGORY_OF_VILLAGE);
            int dorColumnIndex = cursor.getColumnIndex(ProfileEntry.COLUMN_PW_19_DOR);

            //TODO :
            // Extract out the value from the Cursor for the given column index
            String aadhar = cursor.getString(aadharColumnIndex);
            String name = cursor.getString(nameColumnIndex);
            String dob = cursor.getString(dobColumnIndex);
            String age = cursor.getString(ageColumnIndex);
            String husbandGuardian = cursor.getString(husbandGuardianColumnIndex);
            String address = cursor.getString(addressColumnIndex);
            int gravida = cursor.getInt(gravidaColumnIndex);
            int para = cursor.getInt(paraColumnIndex);
            int abortion = cursor.getInt(abortionColumnIndex);
            int living = cursor.getInt(livingColumnIndex);
            String lcb = cursor.getString(lcbColumnIndex);
            String lmp = cursor.getString(lmpColumnIndex);
            String edd = cursor.getString(eddColumnIndex);
            String mcthRch = cursor.getString(mcthRchColumnIndex);
            String subcentreWard = cursor.getString(subcenterWardColumnIndex);
            String blockPPC = cursor.getString(blockPPCColumnIndex);
            int bloodGroup = cursor.getInt(bloodGroupColumnIndex);
            int weight = cursor.getInt(weightColumnIndex);
            int height = cursor.getInt(heightColumnIndex);
            int villageCategory = cursor.getInt(villageCategoryColumnIndex);
            String dor = cursor.getString(dorColumnIndex);

            //TODO :
            // Update the views on the screen with the values from the database
            mAadharEditText.setText(aadhar);
            mNamePWEditText.setText(name);
            mDOBButton.setText(dob);
            mAgeTextView.setText(age);
            mHusbandGuardianEditText.setText(husbandGuardian);
            mAddressEditText.setText(address);
            mGravidaEditText.setText(Integer.toString(gravida));
            mParaEditText.setText(Integer.toString(para));
            mAbortionEditText.setText(Integer.toString(abortion));
            mLivingEditText.setText(Integer.toString(living));
            mLCBButton.setText(lcb);
            mLMPButton.setText(lmp);
            mEDDButton.setText(edd);
            mMctsRchEditText.setText(mcthRch);
            mSubCenterEditText.setText(subcentreWard);
            mBlockPPCEditText.setText(blockPPC);

            // BloodGroup is a dropdown spinner, so map the constant value from the database
            // into one of the dropdown options (0 is A+, 1 is A-, so on...).
            // Then call setSelection() so that option is displayed on screen as the current selection.
            switch (bloodGroup) {
                case ProfileEntry.BLOOD_GROUP_AP:
                    mBloodGroupTypeSpinner.setSelection(0);
                    break;
                case ProfileEntry.BLOOD_GROUP_AN:
                    mBloodGroupTypeSpinner.setSelection(1);
                    break;
                case ProfileEntry.BLOOD_GROUP_BP:
                    mBloodGroupTypeSpinner.setSelection(2);
                    break;
                case ProfileEntry.BLOOD_GROUP_BN:
                    mBloodGroupTypeSpinner.setSelection(3);
                    break;
                case ProfileEntry.BLOOD_GROUP_ABP:
                    mBloodGroupTypeSpinner.setSelection(4);
                    break;
                case ProfileEntry.BLOOD_GROUP_ABN:
                    mBloodGroupTypeSpinner.setSelection(5);
                    break;
                case ProfileEntry.BLOOD_GROUP_OP:
                    mBloodGroupTypeSpinner.setSelection(6);
                    break;
                default:
                    mBloodGroupTypeSpinner.setSelection(7);
                    break;
            }

            mWeightEditText.setText(Integer.toString(weight));
            mHeightEditText.setText(Integer.toString(height));

            // VillageCategory is a dropdown spinner, so map the constant value from the database
            // into one of the dropdown options (0 is Village_completely_cutoff, 1 is Village_partially_cutoff).
            // Then call setSelection() so that option is displayed on screen as the current selection.
            switch (bloodGroup) {
                case ProfileEntry.VILLAGE_COMPLETELY:
                    mVillageCategorySpinner.setSelection(0);
                    break;
                default:
                    mVillageCategorySpinner.setSelection(1);
                    break;
            }

            mProfileRegistrationDateTextView.setText(dor);

        }
    }
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // If the loader is invalidated, clear out all the data from the input fields.
        //TODO
        mAadharEditText.setText("");
        mNamePWEditText.setText("");
        mDOBButton.setText("");
        mAgeTextView.setText("");
        mHusbandGuardianEditText.setText("");
        mAddressEditText.setText("");
        mGravidaEditText.setText("");
        mParaEditText.setText("");
        mAbortionEditText.setText("");
        mLivingEditText.setText("");
        mLCBButton.setText("");
        mLMPButton.setText("");
        mEDDButton.setText("");
        mMctsRchEditText.setText("");
        mSubCenterEditText.setText("");
        mBlockPPCEditText.setText("");
        mBloodGroupTypeSpinner.setSelection(0); //
        mWeightEditText.setText("");
        mHeightEditText.setText("");
        mVillageCategorySpinner.setSelection(1);
        mProfileRegistrationDateTextView.setText("");
    }

    ///////////////////////////////////////////////////
    //    Methods for date picker       //
    private void showDate(int year, int month, int day) {
        if (mDateType == DATE_PROFILE_CREATION) {
            mProfileRegistrationDateTextView.setText(new StringBuilder().append(day).append("/")
                    .append(month).append("/").append(year));
        }
        else if (mDateType == DATE_DOB){
            mDOBButton.setText(new StringBuilder().append(day).append("/")
                    .append(month).append("/").append(year));

            //TODO : implement better age calculation function
            mAgeYears = mCurrentYear - year ;
            if (mAgeYears > 0) {
                mAgeTextView.setText(new StringBuilder().append("Age : ").append(mAgeYears).append(" yrs"));
            }else {
                mAgeTextView.setText("Future !!!");
            }
        }
        else if (mDateType == DATE_LCB){
            mLCBButton.setText(new StringBuilder().append(day).append("/")
                    .append(month).append("/").append(year));
        }
        else if (mDateType == DATE_LMP){
            mLMPButton.setText(new StringBuilder().append(day).append("/")
                    .append(month).append("/").append(year));
        }
        else if(mDateType == DATE_EDD){
            mEDDButton.setText(new StringBuilder().append(day).append("/")
                    .append(month).append("/").append(year));
        }
    }
    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
        Toast.makeText(getApplicationContext(), "Pick a Date", Toast.LENGTH_SHORT).show();
    }
    @Override
    protected Dialog onCreateDialog(int id) {
        // TO-DO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, mYear, mMonth, mDay);
        }
        return null;
    }
    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0, int year, int month, int day) {
                    showDate(year, month+1, day);
                }
            };
    //    Methods for date picker       //
    /////////////////////////////////////////////////////////////////


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
                    NavUtils.navigateUpFromSameTask(FormActivity.this);
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
                                NavUtils.navigateUpFromSameTask(FormActivity.this);
                            }
                        };

                // Show a dialog that notifies the user they have unsaved changes
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    ///////////////////////////////////////////////////////////////////////////////////

    /*** Setup the dropdown spinner that allows the user to select the blood group type*/
    private void setupBloodGroupSpinner() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter userTypeSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_blood_group_type_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        userTypeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mBloodGroupTypeSpinner.setAdapter(userTypeSpinnerAdapter);

        // Set the integer mSelected to the constant values
        mBloodGroupTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.blood_group_ap))) {
                        mBloodGroupType = ProfileEntry.BLOOD_GROUP_AP;
                    } else if (selection.equals(getString(R.string.blood_group_an))) {
                        mBloodGroupType = ProfileEntry.BLOOD_GROUP_AN;
                    } else if (selection.equals(getString(R.string.blood_group_bp))) {
                        mBloodGroupType = ProfileEntry.BLOOD_GROUP_BP;
                    } else if (selection.equals(getString(R.string.blood_group_bn))) {
                        mBloodGroupType = ProfileEntry.BLOOD_GROUP_BN;
                    } else if (selection.equals(getString(R.string.blood_group_abp))) {
                        mBloodGroupType = ProfileEntry.BLOOD_GROUP_ABP;
                    } else if (selection.equals(getString(R.string.blood_group_abn))) {
                        mBloodGroupType = ProfileEntry.BLOOD_GROUP_ABN;
                    } else if (selection.equals(getString(R.string.blood_group_op))) {
                        mBloodGroupType = ProfileEntry.BLOOD_GROUP_OP;
                    } else {
                        mBloodGroupType = ProfileEntry.BLOOD_GROUP_ON;
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mBloodGroupType = ProfileEntry.BLOOD_GROUP_AP;
            }
        });
    }

    /*** Setup the dropdown spinner that allows the user to select the category of village*/
    private void setupVillageCategorySpinner() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter userTypeSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_village_category_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        userTypeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mVillageCategorySpinner.setAdapter(userTypeSpinnerAdapter);

        // Set the integer mSelected to the constant values
        mVillageCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.village_type_completely_cutoff))) {
                        mVillageType = ProfileEntry.VILLAGE_COMPLETELY;
                    } else {
                        mVillageType = ProfileEntry.VILLAGE_PARTIALLY;
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mVillageType = ProfileEntry.VILLAGE_COMPLETELY;
            }
        });
    }

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

}