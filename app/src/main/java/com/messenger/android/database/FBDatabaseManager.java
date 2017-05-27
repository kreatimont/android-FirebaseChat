package com.messenger.android.database;


import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.messenger.android.adapter.ChannelAdapter;
import com.messenger.android.adapter.MessageAdapter;
import com.messenger.android.model.Channel;
import com.messenger.android.model.Message;
import com.messenger.android.model.User;
import com.messenger.android.ui.ListMessagesActivity;
import com.messenger.android.ui.PostMessageActivity;

import java.util.ArrayList;
import java.util.List;

import static com.messenger.android.ui.ListMessagesActivity.TableNote;

public class FBDatabaseManager {

    public static final String TAG = "FBDatabaseManager";

    public static final String TableMessage = "Message";
    public static final String TableChannel = "Channel";
    public static final String TableUser = "User";

    private static FBDatabaseManager instance;

    private FBDatabaseManager() {
    }

    public static FBDatabaseManager getInstance() {
        if (instance == null) {
            instance = new FBDatabaseManager();
        }
        return instance;
    }


    public void subscribeForChannels(final List<Channel> channelList, final ChannelAdapter adapter) {

        DatabaseReference mDbRef = FirebaseDatabase.getInstance().getReference();

        if(channelList == null || adapter == null) {
            Log.e(TAG, "channelList or adapter == null");
            return;
        }

        mDbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                DataSnapshot tableNote = dataSnapshot.child(TableChannel);

                channelList.clear();

                for (DataSnapshot child: tableNote.getChildren()) {
                    Channel tmpChannel = child.getValue(Channel.class);
                    channelList.add(tmpChannel);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void subscribeForChannels(final List<Channel> channelList, final FBDatabasePostListener listener) {

        DatabaseReference mDbRef = FirebaseDatabase.getInstance().getReference();

        if(channelList == null) {
            Log.e(TAG, "channelList == null");
            return;
        }

        mDbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                DataSnapshot tableNote = dataSnapshot.child(TableChannel);

                channelList.clear();

                for (DataSnapshot child: tableNote.getChildren()) {
                    Channel tmpChannel = child.getValue(Channel.class);
                    channelList.add(tmpChannel);
                }

                listener.postSuccessful();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.postWithError(databaseError);
            }
        });

    }

    public void subscribeForMessages(final List<Message> messagesList, final MessageAdapter adapter, final String tableChannel) {

        DatabaseReference mDbRef = FirebaseDatabase.getInstance().getReference();

        if(messagesList == null || adapter == null) {
            Log.e(TAG, "messagesList or adapter == null");
            return;
        }

        mDbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                DataSnapshot messages = dataSnapshot.child(TableChannel).child(tableChannel).child(TableMessage);

                messagesList.clear();

                for (DataSnapshot child: messages.getChildren()) {
                    Message tmpMessage = child.getValue(Message.class);
                    messagesList.add(tmpMessage);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void subscribeForUsers(final List<User> usersList, final FBDatabasePostListener listener) {

        DatabaseReference mDbRef = FirebaseDatabase.getInstance().getReference();

        if(usersList == null ) {
            Log.e(TAG, "usersList == null");
            return;
        }

        mDbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                DataSnapshot users = dataSnapshot.child(TableUser);

                for (DataSnapshot child: users.getChildren()) {
                    User tmpUser = child.getValue(User.class);
                    usersList.add(tmpUser);
                }

                listener.postSuccessful();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.postWithError(databaseError);
            }
        });

    }

    public void postChannel(String channelTitle, final FBDatabasePostListener listener) {

        DatabaseReference mDbRef = FirebaseDatabase.getInstance().getReference();

        String key = mDbRef.child(TableChannel).push().getKey();

        List<String> channelUsers = new ArrayList<>();

        channelUsers.add(FirebaseAuth.getInstance().getCurrentUser().getUid());

        Channel channelToSave = new Channel(channelUsers, key, channelTitle);

        mDbRef.child(TableChannel).child(key).setValue(channelToSave, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                if (databaseError == null) {
                    listener.postSuccessful();
                } else {
                    listener.postWithError(databaseError);
                }
            }
        });
    }

    public void postMessage(String channelId, Message message, final FBDatabasePostListener listener) {

        DatabaseReference mDbRef = FirebaseDatabase.getInstance().getReference();

        String key = mDbRef.child(TableChannel).child(channelId).child(TableMessage).push().getKey();

        mDbRef.child(TableChannel).child(channelId).child(TableMessage).child(key).setValue(message, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                if (databaseError == null) {
                    listener.postSuccessful();
                } else {
                    listener.postWithError(databaseError);
                }
            }
        });
    }

    public void postUser(String name, String login, final FBDatabasePostListener listener) {

        DatabaseReference mDbRef = FirebaseDatabase.getInstance().getReference();

        String key = mDbRef.child(TableUser).push().getKey();

        User userToSave = new User(FirebaseAuth.getInstance().getCurrentUser().getUid(), login, name);

        mDbRef.child(TableUser).child(key).setValue(userToSave, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                if (databaseError == null) {
                    listener.postSuccessful();
                } else {
                    listener.postWithError(databaseError);
                }
            }
        });
    }

    public interface FBDatabasePostListener {
        void postSuccessful();
        void postWithError(DatabaseError error);
    }


}
