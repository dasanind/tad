package com.research.tad;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

//import com.example.research_app.ConsumerActivities.ConsumerDash;
//import com.example.research_app.DonorActivities.DonorHome;
//import com.example.research_app.DonorActivities.SearchActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    static final int GOOGLE_SIGN =123;
    private EditText emailEF,passwordEF;
    private Button loginBtn, registerBtn;
    private TextView googleSigninB,forgotPasswordTV;
    private FirebaseAuth mAuth;
    GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final String TAG = "LoginActivity";

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        setContentView(R.layout.activity_login);
        emailEF = findViewById(R.id.emailET);
        passwordEF = findViewById(R.id.passwordET);
        loginBtn = findViewById(R.id.logInBtn);
        registerBtn = findViewById(R.id.goToRegBtn);
        googleSigninB = findViewById(R.id.googleBtn);
        forgotPasswordTV = findViewById(R.id.forgotPasswordTV);
        createRequest();

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null)
                {
                    startDashboard(user);
                    Log.d("LoginActivity", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    Log.d("LoginActivity", "onAuthStateChanged:signed_out");
                }
            }
        };

        loginBtn.setOnClickListener(this);
        googleSigninB.setOnClickListener(this);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });

        forgotPasswordTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });
    }

    public void createRequest() {
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder()
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.logInBtn:
                loginUser();
                break;

            case R.id.googleBtn:
                signInGoogle();
                break;
        }
    }

    void signInGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, GOOGLE_SIGN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GOOGLE_SIGN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("GSIGNIN", "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("GSIGNIN", "Google sign in failed", e);

            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("GSIGNIN", "signInWithCredential:success");
                    FirebaseUser user = mAuth.getCurrentUser();
                    startDashboard(user);
                    //updateUI(user);
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("GSIGNIN", "signInWithCredential:failure", task.getException());
                    //Snackbar.make(mBinding.mainLayout, "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                    updateUI(null);
                }

            }
        });
    }

    private void updateUI(FirebaseUser user){
        if(user!= null) {
            String name = user.getDisplayName();
            String email = user.getEmail();
            String photo = String.valueOf(user.getPhotoUrl());
            Log.d("GSIGNIN", "HERE GO");
        }
    }

    public void signOut() {
        mAuth.signOut();
        mGoogleSignInClient.signOut();
    }

    protected void loginUser() {
        String strEmail, strPassword;
        strEmail = emailEF.getText().toString();
        strPassword = passwordEF.getText().toString();
        if(!validateEntry(strEmail, strPassword)) return;
        mAuth.signInWithEmailAndPassword(strEmail, strPassword).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    Log.d(TAG, "signInWithEmail:success");
                    FirebaseUser user = mAuth.getCurrentUser();
                    Log.d(TAG, "onComplete: " + user.getEmail());
//                    boolean emailVerified = user.isEmailVerified();
                    startDashboard(user);

                } else {
                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                    Toast.makeText(LoginActivity.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }

    private void startDashboard(FirebaseUser user) {
        final String Uid = user.getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users");
//        Intent intent = new Intent(LoginActivity.this, UserProfile.class);
//        startActivity(intent);
        Log.d(TAG, "startDashboard: " + myRef);
        myRef.child(Uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange: ");
                if(dataSnapshot.exists()) {
                    Toast.makeText(getApplicationContext(), "Logged In ", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this,RequestVehicleActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(LoginActivity.this, CreateProfileActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "NOTTTT", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private boolean validateEntry(String strEmail, String strPassword) {
        if(TextUtils.isEmpty(strEmail)) {
            Toast.makeText(getApplicationContext(), "Email-Id field can not be empty", Toast.LENGTH_LONG).show();
            return false;
        }
        if(TextUtils.isEmpty(strPassword)) {
            Toast.makeText(getApplicationContext(), "Password field can not be empty", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

}
