package com.research.tad;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class BaseDrawerActivity extends AppCompatActivity
{
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    public TextView userNameNav;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_drawer);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        userNameNav = findViewById(R.id.userNameNav);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout =  findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this ,drawerLayout, toolbar,R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.nav_profile:
                        Intent in1 = new Intent(getApplicationContext(), UserProfile.class);
                        startActivity(in1);
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.nav_home:
                        //TODO: Here the functionality goes
//                        Intent in2 = new Intent(getApplicationContext(), UserHome.class);
//                        startActivity(in2);
                        drawerLayout.closeDrawers();
                        break;
//                    case R.id.nav_search:
//                        Intent in3 = new Intent(getApplicationContext(), DonationQueriesActivity.class);
//                        startActivity(in3);
//                        drawerLayout.closeDrawers();
//                        break;
//                    case R.id.nav_cart:
//                        Intent in4 = new Intent(getApplicationContext(), DonorCart.class);
//                        startActivity(in4);
//                        drawerLayout.closeDrawers();
//                        break;
                    case R.id.nav_signOut:
                        signOut();
                        Intent in5 = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(in5);
                        drawerLayout.closeDrawers();
                        break;
                }
                return false;
            }
        });

    }
    public void setName(String name)
    {
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.userNameNav);
        navUsername.setText(name);
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    public void signOut()
    {
        FirebaseAuth.getInstance().signOut();
        GoogleSignInOptions gso = new GoogleSignInOptions.
                Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                build();

        GoogleSignInClient googleSignInClient= GoogleSignIn.getClient(this,gso);
        googleSignInClient.signOut();
        Log.d("GSIGNIN", "Signing Out");
    }
}
