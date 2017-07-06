package com.abhijeetdeshmukh.android.mhealthmamta;

import android.content.ContentUris;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.abhijeetdeshmukh.android.mhealthmamta.anm.ANMVisitActivity;
import com.abhijeetdeshmukh.android.mhealthmamta.data.ProfileContract;

public class MainActivity extends AppCompatActivity {

    private Button mProfileButton, mVisit1Button, mVisit2Button, mVisit3Button, mVisit4Button;
    private int mUserType ;

    /** Identifier for the profile data loader */
    private static final int EXISTING_PROFILE_LOADER = 0;

    /** Content URI for the existing profile (null if it's a new profile) */
    private Uri mCurrentProfileUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Examine the intent that was used to launch this activity,
        // in order to figure out if we're creating a new profile or editing an existing one.
        Intent intent = getIntent();
        mCurrentProfileUri = intent.getData();
        mUserType = intent.getExtras().getInt("userType");

        // Find all relevant views that we will need to read user input from
        mProfileButton = (Button) findViewById(R.id.btn_profile);
        mVisit1Button = (Button) findViewById(R.id.btn_anm_visit1);
        mVisit2Button = (Button) findViewById(R.id.btn_anm_visit2);
        mVisit3Button = (Button) findViewById(R.id.btn_anm_visit3);
        mVisit4Button = (Button) findViewById(R.id.btn_anm_visit4);

        mProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FormActivity.class);
                intent.putExtra("userType", mUserType);

                // Set the URI on the data field of the intent
                intent.setData(mCurrentProfileUri);
                startActivity(intent);
            }
        });

        mVisit1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ANMVisitActivity.class);
                intent.putExtra("userType", mUserType);

                // Set the URI on the data field of the intent
                intent.setData(mCurrentProfileUri);
                startActivity(intent);
            }
        });

    }
}
