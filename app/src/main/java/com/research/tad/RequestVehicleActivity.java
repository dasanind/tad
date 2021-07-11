package com.research.tad;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class RequestVehicleActivity extends BaseDrawerActivity
{
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ImageButton keywordSearchBtn, locationSearchBtn, itemSearchBtn;
    private FirebaseAuth mAuth;
    final FirebaseDatabase db = FirebaseDatabase.getInstance();
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.fragment_container);
        getLayoutInflater().inflate(R.layout.activity_request_vehicle, contentFrameLayout);
        mAuth = FirebaseAuth.getInstance();;
        FirebaseUser user = mAuth.getCurrentUser();
        final String uId = user.getUid();
        final DatabaseReference myRef2 = db.getReference("users/" + uId);
        myRef2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    name = dataSnapshot.child("name").getValue().toString();
                    setName(name);
                    Toast.makeText(getApplicationContext(), "xx"+name, Toast.LENGTH_LONG);
                    Log.d("NAME","name is"+name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //get the spinner from the xml.
        Spinner dropdown = findViewById(R.id.disabilitySpinner);
        //create a list of items for the spinner.
        String[] items = new String[]{"Speech", "Mobility", "Vision"};

        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        //set the spinners adapter to the previously created one.
        dropdown.setAdapter(adapter);
//        keywordSearchBtn = findViewById(R.id.keywordSearchBtn);
//        locationSearchBtn = findViewById(R.id.locationSearchBtn);
//        itemSearchBtn = findViewById(R.id.itemSearchBtn);
//        keywordSearchBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v)
//            {
//                Intent i = new Intent(v.getContext(),SearchActivity.class);
//                i.putExtra("position", 0);
//                v.getContext().startActivity(i);
//            }
//        });
//        locationSearchBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(v.getContext(), SelectCity.class);
//                v.getContext().startActivity(i);
//
//            }
//        });
//        itemSearchBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(v.getContext(), SearchActivity.class);
//                i.putExtra("position", 2);
//                v.getContext().startActivity(i);
//
//            }
//        });


    }
}
