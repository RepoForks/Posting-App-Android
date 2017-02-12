package com.app.infideap.postingapp.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.app.infideap.postingapp.util.Common;
import com.app.infideap.postingapp.util.DimensionConverter;
import com.app.infideap.postingapp.entity.Notification;
import com.app.infideap.postingapp.fragment.NotificationFragment.OnListFragmentInteractionListener;
import com.app.infideap.notificationapp.R;
import com.app.infideap.stylishwidget.view.Stylish;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class NotificationRecyclerViewAdapter extends RecyclerView.Adapter<NotificationRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = NotificationRecyclerViewAdapter.class.getClass().getSimpleName();
    private final List<Notification> mValues;
    private final OnListFragmentInteractionListener mListener;

    public NotificationRecyclerViewAdapter(List<Notification> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_notification, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
//        Notification notification = Facade.getInstance().getManageNotificationTbl()
//                .get(mValues.get(position).notificationId);
//        Log.e(TAG, "Seen : " + notification.seen + ", " + notification.notificationId);

        holder.mItem = mValues.get(position);
//        holder.mItem.seen = notification.seen;
        holder.mIdView.setText(mValues.get(position).title);
        holder.mContentView.setText(mValues.get(position).message);

        Context context = holder.mView.getContext();

        ColorGenerator generator = ColorGenerator.MATERIAL;
        TextDrawable.IBuilder builder = TextDrawable.builder()
                .beginConfig()
                .withBorder(4)
                .bold().toUpperCase()
                .useFont(Stylish.getInstance().bold(context))
                .fontSize(DimensionConverter.stringToDimensionPixelSize("30sp", context.getResources().getDisplayMetrics()))
                .endConfig()
                .rect();


        holder.iconView.setImageDrawable(
                builder.build(holder.mItem.title.substring(0, 1),
                        generator.getColor(holder.mItem.title))
        );

        Date date = Common.parseDate(holder.mItem.dateReceived);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        holder.receivedView.setText(Common.getUserFriendlyDurationString(context, calendar));

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
        holder.mView.setBackgroundColor(ContextCompat.getColor(holder.mView.getContext(),
                mValues.get(position).seen == 0 ? R.color.colorLightGreen_50 : R.color.colorWhite));

    }


    @Override
    public void registerAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
        super.registerAdapterDataObserver(observer);
        observer.onChanged();
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        private final TextView receivedView;
        private final ImageView iconView;
        public Notification mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
            receivedView = (TextView) view.findViewById(R.id.received);
            iconView = (ImageView) view.findViewById(R.id.imageView_icon);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
