package com.app.infideap.postingapp.resource;

import android.widget.TextView;

import com.app.infideap.postingapp.entity.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Shiburagi on 11/02/2017.
 */
public class UserApi {
    private final Api api;
    private String path="user";

    public UserApi(Api api) {
        this.api = api;
    }

    public void add(String uid, User user, DatabaseReference.CompletionListener listener) {
        api.database.getReference(path)
                .child(uid).setValue(user, listener);
    }

    public void getName(String uid, final TextView textView) {
        api.database.getReference(path)
                .child(uid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot!=null){
                            User user = dataSnapshot.getValue(User.class);
                            textView.setText(user.username);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }
}
