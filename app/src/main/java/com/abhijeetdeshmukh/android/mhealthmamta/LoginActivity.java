package com.abhijeetdeshmukh.android.mhealthmamta;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.abhijeetdeshmukh.android.mhealthmamta.data.ProfileContract;

public class LoginActivity extends AppCompatActivity {

    /**  Spinner to enter the user login type */
    private Spinner mUserTypeSpinner;

    /*** Type of the user. The possible valid values are in the ProfileContract.java file:*/
    private int mUserType = ProfileContract.LOGIN_ASHA;

    private EditText mUsernameEditText;
    private EditText mPasswordEditText;
    private Button mLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Find all relevant views that we will need to read user input from
        mUserTypeSpinner = (Spinner) findViewById(R.id.spinner_login_type);
        mUsernameEditText = (EditText) findViewById(R.id.et_username);
        mPasswordEditText = (EditText) findViewById(R.id.et_password);
        mLoginButton = (Button) findViewById(R.id.btn_login);

        //TODO : login process

        setupLoginTypeSpinner();

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ProfileListActivity.class);
                intent.putExtra("userType", mUserType);
                startActivity(intent);
            }
        });

    }

    /*** Setup the dropdown spinner that allows the user to select the user login type*/
    private void setupLoginTypeSpinner() {

        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter userTypeSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_user_type_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        userTypeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mUserTypeSpinner.setAdapter(userTypeSpinnerAdapter);

        // Set the integer mSelected to the constant values
        mUserTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.user_asha))) {
                        mUserType = ProfileContract.LOGIN_ASHA;
                    } else if (selection.equals(getString(R.string.user_anm))) {
                        mUserType = ProfileContract.LOGIN_ANM;
                    } else {
                        mUserType = ProfileContract.LOGIN_MEDICAL_ORGANISATION;
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mUserType = ProfileContract.LOGIN_ASHA;
            }
        });
    }

    //menu for Admin Login
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.menu_admin_login:
                //TODO
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
