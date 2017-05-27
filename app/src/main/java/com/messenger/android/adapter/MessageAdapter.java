package com.messenger.android.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import com.messenger.android.R;
import com.messenger.android.model.Message;
import com.messenger.android.utils.DateFormatter;
import com.messenger.android.utils.SharedData;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private List<Message> mDataList;
    private HashMap<String, Integer> userHasColor;
    private List<Integer> userColors;
    private Context mContext;
    private int userCounter = 0;

    public MessageAdapter(Context context, List<Message> data) {
        this.mDataList = data;
        this.mContext = context;

        userHasColor = new HashMap<>();
        fullUserColorsList();
    }

    private void fullUserColorsList() {
        userColors = new ArrayList<>();
        this.userColors.add(R.color.user1);
        this.userColors.add(R.color.user2);
        this.userColors.add(R.color.user3);
        this.userColors.add(R.color.user4);
        this.userColors.add(R.color.user5);
        this.userColors.add(R.color.user6);
        this.userColors.add(R.color.user7);
        this.userColors.add(R.color.user8);
        this.userColors.add(R.color.user9);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.card_message_my, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(mDataList.get(position));
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mTitleLable, mMessageLabel, mDateLabel, mNameLabel;
        private CircleImageView mCircleImageView;
        private LinearLayout mLinearLayout;

        ViewHolder(View itemView) {
            super(itemView);
            mTitleLable = (TextView)itemView.findViewById(R.id.title);
            mMessageLabel = (TextView) itemView.findViewById(R.id.message);
            mNameLabel = (TextView) itemView.findViewById(R.id.authorName);
            mDateLabel = (TextView) itemView.findViewById(R.id.timestamp);
            mCircleImageView = (CircleImageView) itemView.findViewById(R.id.userCircle);
            mLinearLayout = (LinearLayout) itemView.findViewById(R.id.messageForm);
        }

        void bind(final Message message) {

            for (Message item : mDataList) {
                if (!userHasColor.containsKey(item.getUserId())) {
                    userHasColor.put(item.getUserId(),userColors.get(userCounter));
                    if (userCounter == 8) {
                        userCounter = 0;
                    } else {
                        userCounter++;
                    }

                }
            }

            if(SharedData.currentName.equals(message.getUserName())) {
                CardView card = (CardView) mLinearLayout.getParent();

                LinearLayout.LayoutParams params =
                        new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT);

                params.setMargins(90,8,8,8);
                card.setLayoutParams(params);
                mLinearLayout.setBackgroundColor(ContextCompat.getColor(mContext, R.color.myCard));
            } else {
                CardView card = (CardView) mLinearLayout.getParent();

                LinearLayout.LayoutParams params =
                        new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT);

                params.setMargins(8,8,90,8);
                card.setLayoutParams(params);
            }


            mTitleLable.setText(message.getTitle());
            mMessageLabel.setText(message.getMessage());
            mNameLabel.setText(message.getUserName());
            mDateLabel.setText(DateFormatter.instance.convertDateToString(message.getTimestamp()));
            mCircleImageView.setImageResource(userHasColor.get(message.getUserId()));
        }
    }

}
