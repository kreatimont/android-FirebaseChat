package com.messenger.android.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
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
import com.messenger.android.utils.SharedData;

import static com.messenger.android.ui.ListMessagesActivity.EXTRA_ID_CHANNEL;
import static com.messenger.android.ui.ListMessagesActivity.TableNote;

public class PostMessageActivity extends AppCompatActivity {

    public static final String REQUIRED = "Required";

    private EditText mTitleField, mMessageField;
    private RadioGroup mNoteGradeRadioGroup;
    private Button mPostButton;

    private FirebaseAuth mAuth;

    private String currentChannelId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_note);

        mAuth = FirebaseAuth.getInstance();

        Intent receivedIntent = getIntent();

        if(receivedIntent != null && receivedIntent.hasExtra(EXTRA_ID_CHANNEL)) {
            initUI();
            currentChannelId = receivedIntent.getStringExtra(EXTRA_ID_CHANNEL);
        } else {
            startActivity(new Intent(this, ListChannelsActivity.class));
            finish();
        }

        initUI();
    }

    private void initUI() {

        mTitleField = (EditText) findViewById(R.id.titlePost);
        mMessageField = (EditText) findViewById(R.id.messagePost);
        mNoteGradeRadioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        mPostButton = (Button) findViewById(R.id.buttonPost);

        mPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(mTitleField.getText())) {
                    mTitleField.setError(REQUIRED);
                    return;
                }
                if (TextUtils.isEmpty(mMessageField.getText())) {
                    mMessageField.setError(REQUIRED);
                    return;
                }

                View view = getCurrentFocus();
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromInputMethod(view != null ? view.getWindowToken() : null, 0);

                setEditingEnabled(false);

                FBDatabaseManager.getInstance().postMessage(currentChannelId,
                        new Message(mTitleField.getText().toString(),
                                mMessageField.getText().toString(),
                                mAuth.getCurrentUser().getUid(),
                                SharedData.currentName),
                        new FBDatabaseManager.FBDatabasePostListener() {
                    @Override
                    public void postSuccessful() {
                        Intent intent = new Intent(PostMessageActivity.this, ListMessagesActivity.class);
                        intent.putExtra(EXTRA_ID_CHANNEL, currentChannelId);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void postWithError(DatabaseError error) {

                    }
                });
            }
        });

    }

    private void setEditingEnabled(boolean state) {
        mTitleField.setEnabled(state);
        mMessageField.setEnabled(state);
        mNoteGradeRadioGroup.setEnabled(state);
        mPostButton.setEnabled(state);
    }

}
