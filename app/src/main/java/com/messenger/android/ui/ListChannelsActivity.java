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
import com.messenger.android.R;
import com.messenger.android.adapter.ChannelAdapter;
import com.messenger.android.adapter.MessageAdapter;
import com.messenger.android.database.FBDatabaseManager;
import com.messenger.android.model.Channel;
import com.messenger.android.utils.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.List;

import static com.messenger.android.ui.ListMessagesActivity.EXTRA_ID_CHANNEL;

public class ListChannelsActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ChannelAdapter mAdapter;

    private List<Channel> mDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_channels);

        initUI();

        FBDatabaseManager.getInstance().subscribeForChannels(mDataList, mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.signOut) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, SignInActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void initUI() {

        mDataList = new ArrayList<>();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Channels");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabAddChannel);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ListChannelsActivity.this, PostChannelActivity.class));
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerChannels);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mAdapter = new ChannelAdapter(this, mDataList);

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, mRecyclerView,
                        new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(ListChannelsActivity.this, ListMessagesActivity.class);
                intent.putExtra(EXTRA_ID_CHANNEL, mDataList.get(position).getFirebaseId());
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));

    }
}
