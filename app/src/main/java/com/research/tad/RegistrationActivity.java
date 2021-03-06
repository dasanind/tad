package com.research.tad;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

//import com.example.research_app.ConsumerActivities.ConsumerDash;
//import com.example.research_app.DonorActivities.DonorHome;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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


public class RegistrationActivity extends AppCompatActivity {
    private EditText phoneNumberEF;
    static final int GOOGLE_SIGN =123;
    private EditText emailEF, passwordEF;
    private Button regB, logInBtn, closePopupBtn;
    private ImageButton regGoogle;
    private FirebaseAuth mAuth;
    private TextView googleSigninB;
    GoogleSignInClient mGoogleSignInClient;
    PopupWindow popupWindow;
    ConstraintLayout regConLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        phoneNumberEF = findViewById(R.id.registration_phoneNumber_EF);
        emailEF = findViewById(R.id.emailRegisterET);
        passwordEF = findViewById(R.id.emailPasswordET);
        regB = findViewById(R.id.createAccountBtn);
        googleSigninB = findViewById(R.id.googleRegBtn);
        logInBtn= findViewById(R.id.gotoLoginBtn);
        logInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
            }
        });
        createRequest();
        mAuth = FirebaseAuth.getInstance();
        regB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
        googleSigninB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInGoogle();
            }
        });
        regConLayout = findViewById(R.id.registration_layout);
        /*findViewById(R.id.registration_continue_BTN).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mobile = phoneNumberEF.getText().toString().trim();

                if(mobile.isEmpty() || mobile.length() < 10){
                    phoneNumberEF.setError("Enter a valid mobile");
                    phoneNumberEF.requestFocus();
                    return;
                }

                Intent intent = new Intent(RegistrationActivity.this, VerifyPhoneActivity.class);
                intent.putExtra("mobile", mobile);
                startActivity(intent);
            }
        });*/

    }



    public void createRequest() {
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder()
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);
    }

    protected void registerUser() {
        String strEmail, strPassword;
        strEmail = emailEF.getText().toString();
        strPassword = passwordEF.getText().toString();
        if(!validateEntry(strEmail, strPassword)) return;
        mAuth.createUserWithEmailAndPassword(strEmail, strPassword).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    user.sendEmailVerification()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        // email sent
                                        Log.d("Status", "EMAIL SENT");
                                        FirebaseAuth.getInstance().signOut();
                                        displayBox();

                                    } else {
                                        // email not sent, so display message and restart the activity or do whatever you wish to do
                                        Log.d("Status", "EMAIL NOT SENT");
                                        //restart this activity
                                        overridePendingTransition(0, 0);
                                        finish();
                                        overridePendingTransition(0, 0);
                                        startActivity(getIntent());

                                    }
                                }
                            });

                    Toast.makeText(getApplicationContext(),"Registration Successful", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(RegistrationActivity.this, CreateProfileActivity.class);
                    startActivity(intent);
                }
                else {
                    FirebaseAuthException error = (FirebaseAuthException)task.getException();
                    Toast.makeText(getApplicationContext(), "Registration Failed: "+error.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });

    }

    public void displayBox()
{
    LayoutInflater layoutInflater = (LayoutInflater) RegistrationActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View customView = layoutInflater.inflate(R.layout.pop_up_registration_complete,null);
    closePopupBtn = (Button) customView.findViewById(R.id.popUpRegComplete_closeBox_BTN);
    popupWindow = new PopupWindow(customView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    ((TextView)popupWindow.getContentView().findViewById(R.id.popUpRegComplete_text_EF)).setText("Registration Completed. You will be taken to the logIn page");
    popupWindow.showAtLocation(regConLayout, Gravity.CENTER, 0, 0);

    closePopupBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v)
        {
            popupWindow.dismiss();
            Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    });
}


    protected boolean validateEntry(String strEmail, String strPassword)
    {

        if(TextUtils.isEmpty(strEmail))
        {
            Toast.makeText(getApplicationContext(), "Email-Id field can not be empty", Toast.LENGTH_LONG).show();
            return false;
        }
        if(TextUtils.isEmpty(strPassword))
        {
            Toast.makeText(getApplicationContext(), "Password field can not be empty", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    void signInGoogle()
    {
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
    private void firebaseAuthWithGoogle(String idToken)
    {
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
        if(user!= null)
        {
            String name = user.getDisplayName();
            String email = user.getEmail();
            String photo = String.valueOf(user.getPhotoUrl());
            Log.d("GSIGNIN", "HERE GO");
        }
    }

    private void startDashboard(FirebaseUser user)
    {
        final String Uid = user.getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users");
        myRef.child(Uid).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists()) {
                    String accType = (String) dataSnapshot.child("accountType").getValue();
                    Toast.makeText(getApplicationContext(), "Acc type is " + accType, Toast.LENGTH_SHORT).show();
//                    if (accType.equals("donor")) {
//                        Intent intent = new Intent(RegistrationActivity.this, DonorHome.class);
//                        startActivity(intent);
//                    } else if (accType.equals("consumer")) {
//                        Intent intent = new Intent(RegistrationActivity.this, ConsumerDash.class);
//                        startActivity(intent);
//                    }
                }
                else
                {
                    Intent intent = new Intent(RegistrationActivity.this, CreateProfileActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "NOTTTT", Toast.LENGTH_SHORT).show();
            }
        });
    }

}

