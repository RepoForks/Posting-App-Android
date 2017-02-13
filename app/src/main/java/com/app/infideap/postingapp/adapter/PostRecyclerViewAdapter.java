package com.app.infideap.postingapp.adapter;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.infideap.notificationapp.R;
import com.app.infideap.postingapp.dummy.DummyContent.DummyItem;
import com.app.infideap.postingapp.entity.Post;
import com.app.infideap.postingapp.fragment.PostFragment.OnListFragmentInteractionListener;
import com.app.infideap.postingapp.resource.Api;
import com.app.infideap.postingapp.util.Common;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class PostRecyclerViewAdapter extends RecyclerView.Adapter<PostRecyclerViewAdapter.ViewHolder> {

    private final List<Post> mValues;
    private final OnListFragmentInteractionListener mListener;

    public PostRecyclerViewAdapter(List<Post> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);

        Api.getInstance().user().getName(holder.mItem.createdBy, holder.usernameView);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(holder.mItem.createdDate);
        holder.durationView.setText(
                Common.getUserFriendlyDurationString(holder.mView.getContext(), calendar));

        holder.textView.setText(holder.mItem.text);

        int textAppearance = holder.mItem.text.length() <= 50 ?
                android.support.design.R.style.Base_TextAppearance_AppCompat_Large :
                android.support.design.R.style.Base_TextAppearance_AppCompat_Small;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            holder.textView.setTextAppearance(
                    textAppearance);
        } else
            holder.textView.setTextAppearance(holder.textView.getContext(),
                    textAppearance);

        holder.textView.setTextColor(Color.BLACK);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onCommentClickIteration(v, holder.mItem);
                }
            }
        });


        holder.reactLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onReactClickIteration(view, holder.mItem, 0);
            }
        });

        holder.commentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onCommentClickIteration(view, holder.mItem);
            }
        });

        holder.postInfoLayout.setVisibility(View.GONE);

        holder.reactOverviewLayout.setVisibility(View.GONE);



        holder.commentView.getCompoundDrawables()[0]
                .setColorFilter(holder.commentView.getCurrentTextColor(), PorterDuff.Mode.SRC_ATOP);

        initWatcher(holder);
    }

    private void initWatcher(final ViewHolder holder) {

        Api.getInstance().react().toggle(
                holder.mItem.key,
                FirebaseAuth.getInstance().getCurrentUser().getUid(),
                holder.reactImageView,
                holder.reactTextView);
        Api.getInstance().post().getSize(holder.mItem.key, new Api.OnValueChangeListener() {

            @Override
            public void onSizeChanged(int count) {
                if (count > 0) {
                    String comment;
                    if (count > 1)
                        comment = holder.commentOverviewView.getResources().getString(R.string.comments).toLowerCase();
                    else
                        comment = holder.commentOverviewView.getResources().getString(R.string.comment).toLowerCase();
                    holder.commentOverviewView.setText(
                            String.format(
                                    Locale.getDefault(),
                                    "%d %s",
                                    count,
                                    comment
                            )
                    );
                } else
                    holder.commentOverviewView.setText(null);


                holder.postInfoLayout.setVisibility(
                        holder.commentOverviewView.length() == 0 && holder.reactOverviewView.length() == 0 ?
                                View.GONE : View.VISIBLE);
            }
        });
        Api.getInstance().react().getSize(holder.mItem.key, new Api.OnValueChangeListener() {

            @Override
            public void onSizeChanged(int count) {
                if (count > 0) {
                    String like;
                    if (count > 1)
                        like = holder.reactOverviewView.getResources().getString(R.string.likes).toLowerCase();
                    else
                        like = holder.reactOverviewView.getResources().getString(R.string.like).toLowerCase();
                    holder.reactOverviewView.setText(
                            String.format(
                                    Locale.getDefault(),
                                    "%d %s",
                                    count,
                                    like
                            )
                    );
                    holder.reactOverviewLayout.setVisibility(View.VISIBLE);
                } else {

                    holder.reactOverviewView.setText(null);
                    holder.reactOverviewLayout.setVisibility(View.GONE);
                }


                holder.postInfoLayout.setVisibility(
                        holder.commentOverviewView.length() == 0 && holder.reactOverviewView.length() == 0 ?
                                View.GONE : View.VISIBLE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView usernameView;
        public final TextView durationView;
        public final TextView textView;
        public final View commentLayout;
        public final View reactLayout;
        public final TextView reactTextView;
        public final TextView commentView;
        public final TextView commentOverviewView;
        public final TextView reactOverviewView;
        public final View postInfoLayout;
        public final View reactOverviewLayout;
        public Post mItem;
        public ImageView reactImageView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            usernameView = (TextView) view.findViewById(R.id.textView_username);
            durationView = (TextView) view.findViewById(R.id.textView_duration);
            textView = (TextView) view.findViewById(R.id.textView_text);
            reactImageView = (ImageView) view.findViewById(R.id.imageView_react);
            reactTextView = (TextView) view.findViewById(R.id.textView_react);
            commentView = (TextView) view.findViewById(R.id.textView_comment);
            commentOverviewView = (TextView) view.findViewById(R.id.textView_comment_overview);
            reactOverviewView = (TextView) view.findViewById(R.id.textView_react_overview);

            postInfoLayout = view.findViewById(R.id.layout_post_info);
            commentLayout = view.findViewById(R.id.layout_comment);
            reactLayout = view.findViewById(R.id.layout_react);
            reactOverviewLayout = view.findViewById(R.id.layout_react_overview);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + durationView.getText() + "'";
        }
    }
}
