package com.app.infideap.postingapp.resource;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.infideap.notificationapp.R;
import com.app.infideap.postingapp.entity.React;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Shiburagi on 13/02/2017.
 */
public class ReactApi {
    private static final String TAG = ReactApi.class.getSimpleName();
    private final Api api;
    private String path = "react";

    public ReactApi(Api api) {
        this.api = api;
    }

    public void toggle(final String key, final String userKey, final int type) {
        final DatabaseReference reference = api.database.getReference(path)
                .child(key)
                .child(userKey);
        reference
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.e(TAG, "data : " + dataSnapshot.getValue());
                        if (dataSnapshot.getValue() == null) {
                            reference.setValue(wrap(key, userKey, type));
                        } else {
                            reference.setValue(null);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    public void toggle(final String key, final String userKey, final ImageView imageView, final TextView textView) {
        final DatabaseReference reference = api.database.getReference(path)
                .child(key)
                .child(userKey);
        reference
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int color;
                        if (textView != null || imageView != null) {
                            Context context = textView == null ? imageView.getContext() : textView.getContext();
                            if (dataSnapshot.getValue() == null) {
                                color = ContextCompat.getColor(context, R.color.colorGrey_600);
                            } else {
                                color = ContextCompat.getColor(context, R.color.colorLightBlue_600);
                            }
                            if (textView != null)
                                textView.setTextColor(color);
                            if (imageView != null)
                                imageView.setColorFilter(textView != null ? textView.getCurrentTextColor() : color);
                        }
//
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private React wrap(String key, String userKey, int type) {
        React react = new React();
        react.userKey = userKey;
        react.parentKey = key;
        react.type = type;
        react.createdDate = System.currentTimeMillis();
        return react;
    }


    public void getSize(String key, final Api.OnValueChangeListener listener) {
        api.database.getReference(path)
                .child(key)
                .addChildEventListener(new ChildEventListener() {
                    int count = 0;

                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        count++;
                        listener.onSizeChanged(count);
                    }


                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        count--;
                        listener.onSizeChanged(count);
                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }
}
