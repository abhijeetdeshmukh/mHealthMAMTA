package com.abhijeetdeshmukh.android.mhealthmamta.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/*** Created by ABHIJEET on 21-06-2017.*/

/*** API Contract for the Profile of each pregnant women.*/
public class ProfileContract {

    /** To prevent someone from accidentally instantiating the contract class, give it an empty constructor.*/
    private ProfileContract() {}

    /**
     * The "Content authority" is a name for the entire content provider, similar to the
     * relationship between a domain name and its website.  A convenient string to use for the
     * content authority is the package name for the app, which is guaranteed to be unique on the
     * device.
     */
    public static final String CONTENT_AUTHORITY = "com.abhijeetdeshmukh.android.mhealthmamta";

    /**
     * Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
     * the content provider.
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    /**
     * Possible path (appended to base content URI for possible URI's)
     * For instance, content://com.abhijeetdeshmukh.android.imhealthmamta/profile/ is a valid path for
     * looking at pet data. content://com.abhijeetdeshmukh.android.imhealthmamta/staff/ will fail,
     * as the ContentProvider hasn't been given any information on what to do with "staff".
     */
    public static final String PATH_PROFILES = "profiles";

    /*** Possible values for user login type.*/
    public static final int LOGIN_ASHA = 0;
    public static final int LOGIN_ANM = 1;
    public static final int LOGIN_MEDICAL_ORGANISATION = 2;

    /**
     * Inner class that defines constant values for the profiles database table.
     * Each entry in the table represents a single profile.
     */
    public static final class ProfileEntry implements BaseColumns {

        /** The content URI to access the profile data in the provider */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_PROFILES);

        /*** The MIME type of the {@link #CONTENT_URI} for a list of profiles.*/
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PROFILES;

        /*** The MIME type of the {@link #CONTENT_URI} for a single profile.*/
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PROFILES;

        /** Name of database table for profiles */
        public final static String TABLE_NAME = "profiles";

        /**
         * Unique ID number for the profile (only for use in the database table).
         *
         * Type: INTEGER
         */
        public final static String _ID = BaseColumns._ID;

        /**
         * Name of the pregnant woman.
         *
         * Type: TEXT
         */
        public final static String COLUMN_PW_1_NAME ="name";

        /***
         * Date of Birth (DOB) of the pregnant women
         *
         * Type: TEXT
         */
        public final static String COLUMN_PW_2_DOB = "dob";

        /***
         * Age of the pregnant woman.
         *
         * Type: TEXT
         */
        public final static String COLUMN_PW_3_AGE = "age";

        /**
         * Name of the Husband/Guardian.
         *
         * Type: TEXT
         */
        public final static String COLUMN_PW_4_HUSBAND_GUARDIAN_NAME = "husbandGuardian";

        /**
         * Address including P.S.
         *
         * Type: TEXT
         */
        public final static String COLUMN_PW_5_ADDRESS = "address";

        /***
         * Gravida (G/P/A/L) of the pregnant women
         *
         * Type: INTEGER
         */
        public final static String COLUMN_PW_6A_GRAVIDA= "gravida";

        /***
         * Para (G/P/A/L) of the pregnant women
         *
         * Type: INTEGER
         */
        public final static String COLUMN_PW_6B_PARA= "para";

        /***
         * Abortion (G/P/A/L) of the pregnant women
         *
         * Type: INTEGER
         */
        public final static String COLUMN_PW_6C_ABORTION= "abortion";

        /***
         * living (G/P/A/L) of the pregnant women
         *
         * Type: INTEGER
         */
        public final static String COLUMN_PW_6D_LIVING= "living";

        /***
         * Last child Birth (LCB) of the pregnant women
         *
         * Type: TEXT
         */
        public final static String COLUMN_PW_7_LCB = "lcb";

        /***
         * LMP of the pregnant women
         *
         * Type: TEXT
         */
        public final static String COLUMN_PW_8_LMP = "lmp";

        /***
         * Expected Date of Delivery (EDD) of the pregnant women
         *
         * Type: TEXT
         */
        public final static String COLUMN_PW_9_EDD = "edd";

        /***
         * MCTS/RCH Number
         *
         * Type: Text
         */
        public final static String COLUMN_PW_10_MCTS_RCH ="mctsRch";

        /***
         * Sub-Centre/Ward
         *
         * Type : TEXT
         */
        public final static String COLUMN_PW_11_SUBCENTRE_WARD = "subCentreWard";

        /***
         * Block/PPC
         *
         * Type : Text
         */
        public final static String COLUMN_PW_12_BLOCK_PPC = "blockPPC";

        /**
         * Blood Group of Pregnant Woman.
         *
         * The only possible values are {@link #BLOOD_GROUP_AP}, {@link #BLOOD_GROUP_AN},
         * {@link #BLOOD_GROUP_BP} , {@link #BLOOD_GROUP_BN}, {@link #BLOOD_GROUP_ABP},
         * {@link #BLOOD_GROUP_ABN}, {@link #BLOOD_GROUP_OP} , or {@link #BLOOD_GROUP_ON}
         *
         * Type: INTEGER
         */
        public final static String COLUMN_PW_14_BLOOD_GROUP = "bloodGroup";
        /*** Possible values for the blood group.*/
        public static final int BLOOD_GROUP_AP = 0;
        public static final int BLOOD_GROUP_AN = 1;
        public static final int BLOOD_GROUP_BP = 2;
        public static final int BLOOD_GROUP_BN = 3;
        public static final int BLOOD_GROUP_ABP = 4;
        public static final int BLOOD_GROUP_ABN = 5;
        public static final int BLOOD_GROUP_OP = 6;
        public static final int BLOOD_GROUP_ON = 7;
        /**
         * Returns whether or not the given blood group is @link #BLOOD_GROUP_AP}, {@link #BLOOD_GROUP_AN},
         * {@link #BLOOD_GROUP_BP} , {@link #BLOOD_GROUP_BN}, {@link #BLOOD_GROUP_ABP},
         * {@link #BLOOD_GROUP_ABN}, {@link #BLOOD_GROUP_OP} , or {@link #BLOOD_GROUP_ON}
         */
        public static boolean isValidBloodGroup(int bloodGroup) {
            if (bloodGroup == BLOOD_GROUP_AP || bloodGroup == BLOOD_GROUP_AN || bloodGroup == BLOOD_GROUP_BP
                    || bloodGroup == BLOOD_GROUP_BN || bloodGroup == BLOOD_GROUP_ABP || bloodGroup == BLOOD_GROUP_ABN
                    || bloodGroup == BLOOD_GROUP_OP || bloodGroup == BLOOD_GROUP_ON) {
                return true;
            }
            return false;
        }

        /***
         * weight of the pregnant women (in kgs)
         *
         * Type: Integer
         */
        public final static String COLUMN_PW_15_WEIGHT = "weight";

        /***
         * Height of the pregnant women (in cms)
         *
         * Type: integer
         */
        public final static String COLUMN_PW_16_HEIGHT = "height";

        /***
         *Aadhar number of the pregnant women
         *
         * Type: TEXT
         */
        public final static String COLUMN_PW_17_AADHAR = "aadhar";

        /***
         * Category of Village
         *
         * The only possible values are {@link #VILLAGE_COMPLETELY}, {@link #VILLAGE_PARTIALLY}
         *
         * Type : INTEGER
         */
        public final static String COLUMN_PW_18_CATEGORY_OF_VILLAGE = "villageCategory";
        /*** Possible values for the category of village.*/
        public static final int VILLAGE_COMPLETELY = 0;
        public static final int VILLAGE_PARTIALLY = 1;
        /**
         * Returns whether or not the given village category is @link #VILLAGE_COMPLETELY},
         * {@link #VILLAGE_PARTIALLY}
         */
        public static boolean isValidVillageCategory(int villageCategory) {
            if (villageCategory == VILLAGE_COMPLETELY || villageCategory == VILLAGE_PARTIALLY ){
                return true;
            }
            return false;
        }

        /***
         * Date of registration (DOR) of the pregnant women
         *
         * Type: TEXT
         */
        public final static String COLUMN_PW_19_DOR = "dor";

        //////////////////////////////////////////////////////////////////////////////////////////////

        ///////////////////////////////////////////////////////////////////////////////
        //      ANM visit 1
        /***
         * Date of assessment (DOA) for ANM's visit 1 of the pregnant women
         *
         * Type: TEXT
         */
        public final static String COLUMN_PW_ANM1_DOA = "anm1DOA";


        /***
         * Category of More than 4 earlier pregnancies
         *
         * The only possible values are {@link #TRUE}, {@link #FALSE}
         *
         * Type : INTEGER
         */
        public final static String COLUMN_PW_ANM1_EARLIER_PREGNANCIES = "earlierPregnancies";

        /*** Possible values for the checkbox.*/
        public static final int CHECKBOX_FALSE = 0;
        public static final int CHECKBOX_TRUE = 1;

    }

}