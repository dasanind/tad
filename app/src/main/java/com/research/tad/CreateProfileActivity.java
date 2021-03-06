package com.research.tad;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

//import com.example.research_app.ui.CurrentDataClass;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CreateProfileActivity extends AppCompatActivity {

    UserDetailsClass newUser = new UserDetailsClass();
    private ImageView avatar;
    private EditText nameEF,countryEF, stateEF, cityEF;
    private EditText firstNameET,lastNameET,address1ET,address2ET, zipCodeET, contactNumberET;
    private Button signOutBtn, saveInformationBtn;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    String firstName,lastName,address1,address2, zipCode, contactNumber;
    String name,email,uid,country,state,city,accType,profilePicture,description;
    boolean error = false;
    private FirebaseDatabase database;
    private static final String TAG = "CreateProfileActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);
        database= FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        avatar = findViewById(R.id.picturebuttonCreateProfileView);
        nameEF = findViewById(R.id.userNameCreateProfileET);
        signOutBtn = findViewById(R.id.signOutBtn);
        countryEF = findViewById(R.id.createProfile_countryName_ET);
        stateEF = findViewById(R.id.createProfile_stateName_ET);
        cityEF = findViewById(R.id.createProfile_cityName_ET);
        firstNameET = findViewById(R.id.createProfile_firstName_ET);
        lastNameET= findViewById(R.id.createProfile_lastName_ET);
        address1ET= findViewById(R.id.createProfile_addressLine1_ET);
        address2ET= findViewById(R.id.createProfile_addressLine2_ET);
        zipCodeET= findViewById(R.id.createProfile_zipCode_ET);
        contactNumberET= findViewById(R.id.createProfile_mobileNumber_ET);
        saveInformationBtn = findViewById(R.id.createProfile_save_BTN);

        saveInformationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(nameEF.getText().toString().isEmpty()) {
                    nameEF.setError("This field can not be empty");
                    error = true;
                }
                if(countryEF.getText().toString().isEmpty()) {
                    countryEF.setError("This field can not be empty");
                    error = true;
                }
                if(stateEF.getText().toString().isEmpty()) {
                    stateEF.setError("This field can not be empty");
                    error = true;
                }

                if(cityEF.getText().toString().isEmpty()) {
                    cityEF.setError("This field can not be empty");
                    error = true;
                }

                if(error == false) {
                    updateProfile();
                }
            }
        });

        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                GoogleSignInOptions gso = new GoogleSignInOptions.
                        Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                        build();

                GoogleSignInClient googleSignInClient= GoogleSignIn.getClient(CreateProfileActivity.this,gso);
                googleSignInClient.signOut();
                Log.d("GSIGNIN", "Signing Out");
                Intent in5 = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(in5);
            }
        });
    }


    public void updateProfile() {
        final String userID = user.getUid();
        Log.d(TAG, userID);
//        DatabaseReference userDetailsRef = database.getReference("users/"+userID);
        DatabaseReference userDetailsRef = database.getReference();
        userDetailsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot dataSnapshot) {
                updateDatabase();
                Intent intent = new Intent(CreateProfileActivity.this, LoginActivity.class);
                startActivity(intent);
            }

            @Override
            public void onCancelled (@NonNull DatabaseError databaseError) {

            }

        });
    }

    protected void updateDatabase() {
        String user_name = nameEF.getText().toString();
        final String Uid = user.getUid();
        Log.d(TAG + "updateDatabase", Uid);
        try {
//            DatabaseReference userRef = database.getReference("users/"+Uid);
            DatabaseReference userRef = database.getReference();
            String email = user.getEmail();
            Log.d(TAG, email);
            UserDetailsClass newUser = new UserDetailsClass();
            newUser.setName(user_name);
            newUser.setFirstName(firstNameET.getText().toString());
            newUser.setLastName(lastNameET.getText().toString());
            newUser.setAddress1(address1ET.getText().toString());
            newUser.setAddress2(address2ET.getText().toString());
            newUser.setContactNumber(contactNumberET.getText().toString());
            newUser.setZipCode(zipCodeET.getText().toString());
            newUser.setCountry(countryEF.getText().toString());
            newUser.setState(stateEF.getText().toString());
            newUser.setCity(cityEF.getText().toString());
            newUser.setEmail(email);
            newUser.setUid(Uid);
//            CurrentDataClass currentData = new CurrentDataClass();
//            currentData.setName(user_name);

            userRef.child("users").child(Uid).setValue(newUser);
            Log.d(TAG, newUser.getEmail());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
