package com.research.tad;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserProfile extends BaseDrawerActivity {
    private ImageView avatar;
    private ImageButton editName, editAddress;
    private TextView userEmail, userType;
    EditText userName, userAddress;
    private FirebaseAuth mAuth;
    Button saveBtn;
    final FirebaseDatabase db = FirebaseDatabase.getInstance();String name;
    private static final String TAG = "UserProfile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.fragment_container); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_user_profile, contentFrameLayout);
        saveBtn =findViewById(R.id.saveBtn);
        saveBtn.setVisibility(View.INVISIBLE);
        userName = findViewById(R.id.nameET);
        userName.setEnabled(false);
        userAddress = findViewById(R.id.addressET);
        userAddress.setEnabled(false);
        userEmail = findViewById(R.id.emailET);
        editName = findViewById(R.id.updateNameImgBtn);
        editName.setVisibility(View.INVISIBLE);
        editAddress = findViewById(R.id.updateAddressImgBtn);
        editAddress.setVisibility(View.INVISIBLE);
        editName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editBtnClicked(userName);
            }
        });
        editAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(checkTextFields()) {
                    updateNameDB();
                }
            }
        });

    }

    protected void onStart() {
        super.onStart();
        getUserData();
    }

    protected void updateNameDB() {
        final String newName = userName.getText().toString();
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final String userId = user.getUid();
        final DatabaseReference userDetailsRef = database.getReference("users/"+userId);
        userDetailsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userDetailsRef.child("name").setValue(newName);
                userName.setEnabled(false);
                userName.setBackgroundColor(Color.WHITE);
                userName.setText(newName);
                saveBtn.setVisibility(View.INVISIBLE);
                //Intent intent = new Intent(DonorProfile.this, DonorProfile.class);
                //startActivity(intent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    protected boolean checkTextFields() {
        TextInputLayout nameLayout = findViewById(R.id.nameLayout);
        nameLayout.setError(null);
        if(userName.getText().toString().equals("")) {
            Log.d("ERROR", "put msg");

            nameLayout.setError("Enter a name");
            return false;
        }
        return true;
    }

    protected  void editBtnClicked(EditText btnName) {
        btnName.setEnabled(true);
        btnName.setText("");
        btnName.setHint("Enter New User Name");
        btnName.setBackgroundColor(Color.GRAY);
        saveBtn.setVisibility(View.VISIBLE);
    }

    protected void getUserData() {
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final String userID = user.getUid();
        Log.d(TAG, userID);
        DatabaseReference userDetailsRef = database.getReference("users/"+userID);
//        DatabaseReference userDetailsRef = database.getReference().child("users");
        Log.d(TAG, String.valueOf(userDetailsRef.get()));
        userDetailsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = (String) dataSnapshot.child("name") .getValue();
                name = name.substring(0, 1).toUpperCase() + name.substring(1);
                String email = (String) dataSnapshot.child("email") .getValue();
                String state = (String) dataSnapshot.child("state").getValue();
                state = state.substring(0, 1).toUpperCase() + state.substring(1);
                String country = (String) dataSnapshot.child("country").getValue();
                country = country.substring(0, 1).toUpperCase() + country.substring(1);
                String city = (String) dataSnapshot.child("city").getValue();
                city = city.substring(0, 1).toUpperCase() + city.substring(1);
                userName.setText(name);
                userAddress.setText(city+" ,"+state+" ,"+country);
                userEmail.setText(email);
                editName.setVisibility(View.VISIBLE);
                editAddress.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
