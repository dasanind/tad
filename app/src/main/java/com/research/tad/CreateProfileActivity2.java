//package com.research.tad;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.example.research_app.ConsumerActivities.ConsumerDash;
//import com.example.research_app.DonorActivities.DonorHome;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//public class CreateProfileActivity2 extends AppCompatActivity {
//
//    UserDetailsClass newUser = new UserDetailsClass();
//    private EditText firstNameET,lastNameET,address1ET,address2ET, zipCodeET, contactNumberET;
//    String firstName,lastName,address1,address2, zipCode, contactNumber;
//    String name,email,uid,country,state,city,accType,profilePicture,description;
//    Button saveInformationBtn;
//    Bundle bund;
//    boolean error = false;
//    private FirebaseAuth mAuth;
//    FirebaseDatabase database;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_create_profile2);
//        database= FirebaseDatabase.getInstance();
//        bund = this.getIntent().getExtras();
//        if (bund != null)
//            newUser = bund.getParcelable("userDetails");
//        firstNameET = findViewById(R.id.createProfile2_firstName_ET);
//        lastNameET= findViewById(R.id.createProfile2_lastName_ET);
//        address1ET= findViewById(R.id.createProfile2_addressLine1_ET);
//        address2ET= findViewById(R.id.createProfile2_addressLine2_ET);
//        zipCodeET= findViewById(R.id.createProfile2_zipCode_ET);
//        contactNumberET= findViewById(R.id.createProfile2_mobileNumber_ET);
//        saveInformationBtn = findViewById(R.id.createProfile2_save_BTN);
//
//        saveInformationBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                updateProfile();
//            }
//        });
//
//    }
//
//    public void updateProfile()
//    {
//        contactNumber = contactNumberET.getText().toString();
//        error = false;
//        final DatabaseReference myRef = database.getReference("register numbers");
//        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange( DataSnapshot dataSnapshot)
//            {
//                if (dataSnapshot.exists())
//                {
//                    for (DataSnapshot dsChild : dataSnapshot.getChildren())
//                    {
//                        String number = dsChild.getValue().toString();
//                        Log.e("contactNumber", "number is: "+number);
//                        Log.e("contactNumber", "error is: "+error);
//                        if (contactNumber.equals(number))
//                        {
//                            error = true;
//                            setError();
//                            Log.e("contactNumber", "1a;ready exists");
//                            break;
//                        }
//                    }
//                    if (error == false)
//                    {
//                        updateDatabase();
//                    }
//                }
//
//                else
//                {
//                    updateDatabase();
//                }
//            }
//
//            @Override
//            public void onCancelled (@NonNull DatabaseError databaseError)
//            {
//
//            }
//
//        });
//    }
//
//    public void updateDatabase()
//    {
//        DatabaseReference userRef = database.getReference("users");
//        final DatabaseReference myRef = database.getReference("register numbers");
//        firstName = firstNameET.getText().toString();
//        lastName = lastNameET.getText().toString();
//        address1 = address1ET.getText().toString();
//        address2 = address2ET.getText().toString();
//        zipCode = zipCodeET.getText().toString();
//
//        name = newUser.getName();
//        email = newUser.getEmail();
//        uid = newUser.getUid();
//        country = newUser.getCountry();
//        state = newUser.getState();
//        city = newUser.getCity();
//        accType = newUser.getAccType();
//        description = newUser.getDescription();
//        newUser.setFirstName(firstName);
//        newUser.setLastName(lastName);
//        newUser.setAddress1(address1);
//        newUser.setAddress2(address2);
//        newUser.setContactNumber(contactNumber);
//        newUser.setZipCode(zipCode);
//        Log.e("AType", "acct type is: " + accType);
//        if (accType.equals("donor")) {
//
//            userRef.child(uid).child("accountType").setValue("donor");
//            DatabaseReference donorRef = database.getReference("donors");
//            donorRef.child(uid).setValue(newUser);
//            Intent intent = new Intent(com.example.research_app.CreateProfileActivity2.this, DonorHome.class);
//            startActivity(intent);
//        }
//        else if (accType.equals("consumer")) {
//
//            userRef.child(uid).child("accountType").setValue("consumer");
//            userRef.child(uid).child("string").setValue("");
//            DatabaseReference consumerRef = database.getReference("consumers");
//            consumerRef.child(uid).setValue(newUser);
//            Intent intent = new Intent(com.example.research_app.CreateProfileActivity2.this, ConsumerDash.class);
//            startActivity(intent);
//        }
//        myRef.push().setValue(contactNumber);
//    }
//
//    public void setError()
//    {
//        contactNumberET.setError("Contact number already exists");
//        Log.e("contactNumber", "a;ready exists");
//    }
//}
