package com.messenger.android.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import com.messenger.android.R;
import com.messenger.android.adapter.MessageAdapter;
import com.messenger.android.database.FBDatabaseManager;
import com.messenger.android.model.Channel;
import com.messenger.android.model.Message;

public class ListMessagesActivity extends AppCompatActivity {


    public static final String TableNote = "Notes";
    public static final String EXTRA_ID_CHANNEL = "extra_id_channel";

    private RecyclerView mRecyclerView;
    private MessageAdapter mAdapter;

    private ArrayList<Message> mDataList;

    private String channelId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_messages);

        final Intent receivedIntent = getIntent();

        if(receivedIntent != null && receivedIntent.hasExtra(EXTRA_ID_CHANNEL)) {
            initUI();
            channelId = receivedIntent.getStringExtra(EXTRA_ID_CHANNEL);
            FBDatabaseManager.getInstance().
                    subscribeForMessages(mDataList,mAdapter,receivedIntent.getStringExtra(EXTRA_ID_CHANNEL));

            final List<Channel> channelList = new ArrayList<>();

            FBDatabaseManager.getInstance()
                    .subscribeForChannels(channelList, new FBDatabaseManager.FBDatabasePostListener() {
                        @Override
                        public void postSuccessful() {
                            for(Channel channel : channelList) {
                                if(channel.getFirebaseId().equals(receivedIntent.getStringExtra(EXTRA_ID_CHANNEL))) {
                                    setTitle(channel.getTitle());
                                }
                            }
                        }

                        @Override
                        public void postWithError(DatabaseError error) {

                        }
                    });
        } else {
            startActivity(new Intent(this, ListChannelsActivity.class));
            finish();
        }
    }

    private void initUI() {

        mDataList = new ArrayList<>();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListMessagesActivity.this, PostMessageActivity.class);
                intent.putExtra(EXTRA_ID_CHANNEL, channelId);
                startActivity(intent);
            }
        });


        mRecyclerView = (RecyclerView)findViewById(R.id.recycler);
        mAdapter = new MessageAdapter(this, mDataList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

    }

}
