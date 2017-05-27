package com.messenger.android.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import com.messenger.android.R;
import com.messenger.android.model.Channel;

public class ChannelAdapter extends RecyclerView.Adapter<ChannelAdapter.ViewHolder> {

    private List<Channel> mDataList;
    private HashMap<String, Integer> channelHasColor;
    private List<Integer> channelColors;
    private Context mContext;
    private int channelCounter = 0;

    public ChannelAdapter(Context context, List<Channel> data) {
        this.mDataList = data;
        this.mContext = context;

        channelHasColor = new HashMap<>();
        fullUserColorsList();
    }

    private void fullUserColorsList() {
        channelColors = new ArrayList<>();
        this.channelColors.add(R.color.user1);
        this.channelColors.add(R.color.user2);
        this.channelColors.add(R.color.user3);
        this.channelColors.add(R.color.user4);
        this.channelColors.add(R.color.user5);
        this.channelColors.add(R.color.user6);
        this.channelColors.add(R.color.user7);
        this.channelColors.add(R.color.user8);
        this.channelColors.add(R.color.user9);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.card_channel, parent, false);
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

        private TextView mTitleLable, mLastMessageLabel;
        private CircleImageView mCircleImageView;

        ViewHolder(View itemView) {
            super(itemView);
            mTitleLable = (TextView)itemView.findViewById(R.id.titleChannel);
            //mLastMessageLabel = (TextView) itemView.findViewById(R.id.message);
            mCircleImageView = (CircleImageView) itemView.findViewById(R.id.channelCircleIcon);
        }

        void bind(final Channel channel) {

            for (Channel item : mDataList) {
                if (!channelHasColor.containsKey(item.getFirebaseId())) {
                    channelHasColor.put(item.getFirebaseId(), channelColors.get(channelCounter));
                    if (channelCounter == 8) {
                        channelCounter = 0;
                    } else {
                        channelCounter++;
                    }

                }
            }

            mTitleLable.setText(channel.getTitle());
            mCircleImageView.setImageResource(channelHasColor.get(channel.getFirebaseId()));

            //TODO: add last message info

        }
    }

}
