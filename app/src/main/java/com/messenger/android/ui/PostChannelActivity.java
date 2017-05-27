package com.messenger.android.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.messenger.android.R;
import com.messenger.android.database.FBDatabaseManager;
import com.messenger.android.model.Message;
import com.messenger.android.model.NoteGrade;

import static com.messenger.android.ui.ListMessagesActivity.TableNote;

public class PostChannelActivity extends AppCompatActivity {

    public static final String REQUIRED = "Required";
    public static final String TAG = "PostChannelActivity";

    private EditText mTitleField;
    private Button mPostButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_channel);


        initUI();
    }

    private void initUI() {

        mTitleField = (EditText) findViewById(R.id.channelTitlePost);
        mPostButton = (Button) findViewById(R.id.buttonPostChannel);

        mPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(mTitleField.getText())) {
                    mTitleField.setError(REQUIRED);
                    return;
                }

                View view = getCurrentFocus();
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromInputMethod(view != null ? view.getWindowToken() : null, 0);

                setEditingEnabled(false);
                FBDatabaseManager.getInstance().postChannel(mTitleField.getText().toString(), new FBDatabaseManager.FBDatabasePostListener() {
                    @Override
                    public void postSuccessful() {
                        startActivity(new Intent(PostChannelActivity.this, ListChannelsActivity.class));
                        finish();
                        Log.e(TAG, "successfult posted");
                    }

                    @Override
                    public void postWithError(DatabaseError error) {
                        Toast.makeText(PostChannelActivity.this, "Channel was not posted", Toast.LENGTH_LONG).show();
                        Log.e(TAG, "channel was not posted");
                    }
                });
            }
        });

    }

    private void setEditingEnabled(boolean state) {
        mTitleField.setEnabled(state);
        mPostButton.setEnabled(state);
    }

}
