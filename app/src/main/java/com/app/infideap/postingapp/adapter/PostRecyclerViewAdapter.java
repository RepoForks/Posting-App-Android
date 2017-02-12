package com.app.infideap.postingapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.infideap.notificationapp.R;
import com.app.infideap.postingapp.fragment.PostFragment.OnListFragmentInteractionListener;
import com.app.infideap.postingapp.dummy.DummyContent.DummyItem;
import com.app.infideap.postingapp.entity.Post;
import com.app.infideap.postingapp.resource.Api;
import com.app.infideap.postingapp.util.Common;

import java.util.Calendar;
import java.util.List;

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

        holder.commentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onCommentClickIteration(view, holder.mItem);
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
        public Post mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            usernameView = (TextView) view.findViewById(R.id.textView_username);
            durationView = (TextView) view.findViewById(R.id.textView_duration);
            textView = (TextView) view.findViewById(R.id.textView_text);

            commentLayout = view.findViewById(R.id.layout_comment);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + durationView.getText() + "'";
        }
    }
}
