package com.example.wechat.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.wechat.R;
import com.example.wechat.gson.TweetBean;
import com.example.wechat.view.NineGridImageLayout;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class RecyclerViewWithHeadAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final static String TAG = "RecyclerViewAdapter";
    private static final int HEADER_VIEW = 2;
    private Context mContext;
    private List<TweetBean> mList;
    private HashMap<String,String> mHeaderMap;
    protected LayoutInflater inflater;

    public RecyclerViewWithHeadAdapter(Context context) {
        mContext = context;
        inflater = LayoutInflater.from(context);
    }

    public void setList(List<TweetBean> list) {
        mList = filterListInvalidValues(list);
    }

    public void setMap(HashMap<String,String> map) {
        mHeaderMap = map;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;

        if (viewType == HEADER_VIEW) {
            v = LayoutInflater.from(mContext).inflate(R.layout.recyclerview_header, parent, false);
            HeaderViewHolder vh = new HeaderViewHolder(v);
            return vh;
        }

        v = inflater.inflate(R.layout.tweet, parent, false);
        return new NormalViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        try {
            if (holder instanceof NormalViewHolder) {
                NormalViewHolder vh = (NormalViewHolder) holder;
                int pos = position -1;
                List<String> imagesBody = mList.get(pos).getImagesBodyString();
                List<String> commentsBody = mList.get(pos).getCommentsBodyString();

                vh.layout.setIsShowAll(false);
                Glide.with(mContext).load(mList.get(pos).getSenderBody().getAvatar()).placeholder(R.drawable.ic_launcher_background).into(vh.avatar);
                vh.layout.setUrlList(imagesBody);
                vh.senderName.setText(mList.get(pos).getSenderBody().getNickname());
                vh.content.setText(mList.get(pos).getContent());

                if(commentsBody.size() > 0) {
                    vh.comments.setText(commentsBody.toString());
                }else{
                    vh.comments.setText("");
                }

            } else if (holder instanceof HeaderViewHolder) {
                HeaderViewHolder vh = (HeaderViewHolder) holder;
                vh.nickTextView.setText(mHeaderMap.get("nick"));
                Glide.with(mContext).load(mHeaderMap.get("avatar")).placeholder(R.drawable.ic_launcher_background).into(vh.avatarImageView);
                Glide.with(mContext).load(mHeaderMap.get("profile-image")).placeholder(R.drawable.ic_launcher_background).into(vh.profileImageView);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<TweetBean> filterListInvalidValues(List<TweetBean> list) {
        Iterator<TweetBean> iterator = list.iterator();
        while (iterator.hasNext()) {
            TweetBean tweet = iterator.next();
            if(!TextUtils.isEmpty(tweet.getError()) ||
                    !TextUtils.isEmpty(tweet.getUnknown_error())) {
                iterator.remove();
            }
        }

        return list;
    }

    @Override
    public int getItemCount() {
        return getListSize(mList) + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            // This is where we'll add header.
            return HEADER_VIEW;
        }
        return super.getItemViewType(position);
    }

    // Define a ViewHolder for Header view
    public class HeaderViewHolder extends ViewHolder {
        private TextView nickTextView;
        private ImageView profileImageView;
        private ImageView avatarImageView;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            nickTextView = (TextView) itemView.findViewById(R.id.nick);
            avatarImageView = (ImageView) itemView.findViewById(R.id.avatar);
            profileImageView = (ImageView) itemView.findViewById(R.id.profile_image);
        }
    }

    public class NormalViewHolder extends ViewHolder {
        NineGridImageLayout layout;
        TextView content;
        TextView comments;
        TextView senderName;
        ImageView avatar;


        public NormalViewHolder(View itemView) {
            super(itemView);
            layout = (NineGridImageLayout) itemView.findViewById(R.id.layout_nine_grid);
            senderName = (TextView) itemView.findViewById(R.id.senderName);
            content = (TextView) itemView.findViewById(R.id.content);
            comments = (TextView) itemView.findViewById(R.id.comments);
            avatar = (ImageView) itemView.findViewById(R.id.userAvatar);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // Define elements of a row here
        public ViewHolder(View itemView) {
            super(itemView);
            // Find view by ID and initialize here
        }

        public void bindView(int position) {
            // bindView() method to implement actions
        }
    }

    private int getListSize(List<TweetBean> list) {
        if (list == null || list.size() == 0) {
            return 0;
        }
        return list.size();
    }
}