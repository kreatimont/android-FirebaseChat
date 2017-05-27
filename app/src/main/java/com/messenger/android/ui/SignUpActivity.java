package com.messenger.android.ui;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.messenger.android.R;
import com.messenger.android.database.FBDatabaseManager;
import com.messenger.android.utils.SharedData;

import static com.messenger.android.ui.PostMessageActivity.REQUIRED;

public class SignUpActivity extends AppCompatActivity {

    public static final String TAG = "SignUpActivity";

    private FirebaseAuth mAuth;

    private EditText mEmailField, mPassField, mNameField;
    private Button mSignUpButton;
    private ProgressBar mProgressBar;
    private LinearLayout mSignUpLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initUI();

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser != null) {
            startListActivityWithUser(currentUser);
        }
    }

    private void initUI() {
        mEmailField = (EditText) findViewById(R.id.loginEmail);
        mPassField = (EditText) findViewById(R.id.loginPassword);
        mNameField = (EditText) findViewById(R.id.signUpName);
        mSignUpButton = (Button) findViewById(R.id.buttonSignUp);

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mSignUpLayout = (LinearLayout) findViewById(R.id.loginForm);

        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(mEmailField.getText().toString())) {
                    mEmailField.setError(REQUIRED);
                    return;
                }
                if (TextUtils.isEmpty(mPassField.getText().toString())) {
                    mPassField.setError(REQUIRED);
                    return;
                }
                if (TextUtils.isEmpty(mNameField.getText().toString())) {
                    mNameField.setError(REQUIRED);
                    return;
                }

                showProgressBar(true);
                signUp(mEmailField.getText().toString(), mPassField.getText().toString(), mNameField.getText().toString());

                View view = getCurrentFocus();
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromInputMethod(view != null ? view.getWindowToken() : null, 0);
            }
        });
    }

    private void signUp(final String email, String password, final String name) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                showProgressBar(false);
                if (task.isSuccessful()) {
                    Log.d(TAG, "createUserWithEmail:success");
                    FBDatabaseManager.getInstance().postUser(name, email, new FBDatabaseManager.FBDatabasePostListener() {
                        @Override
                        public void postSuccessful() {
                            Log.d(TAG, "userSavedToDb:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            SharedData.currentName = name;
                            startListActivityWithUser(user);
                            finish();
                        }

                        @Override
                        public void postWithError(DatabaseError error) {
                            Toast.makeText(SignUpActivity.this, "Try again.", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                    Toast.makeText(SignUpActivity.this, "Registration failed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void startListActivityWithUser(FirebaseUser user) {
        startActivity(new Intent(this, ListChannelsActivity.class));
    }

    private void showProgressBar(boolean state) {

        if (state) {
            mProgressBar.setVisibility(View.VISIBLE);
            mSignUpLayout.setVisibility(View.GONE);
        } else {
            mProgressBar.setVisibility(View.GONE);
            mSignUpLayout.setVisibility(View.VISIBLE);
        }

    }


}
