package com.app.infideap.postingapp.resource;

import android.support.v7.widget.RecyclerView;

import com.app.infideap.postingapp.entity.Post;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Shiburagi on 11/02/2017.
 */
public class PostApi {
    public static final String TAG = PostApi.class.getSimpleName();

    String path = "post";

    private final Api api;

    PostApi(Api api) {
        this.api = api;
    }

    public void add(Post post) {
        DatabaseReference reference = api.database.getReference(path)
                .push();
        post.key = reference.getKey();
        reference.setValue(post);


    }

    public void get(String parentKey, final List<Post> posts, final RecyclerView.Adapter adapter) {
        api.database.getReference(path)
                .orderByChild("parentKey")
                .equalTo(parentKey)
                .addChildEventListener(new ChildEventListener() {
                    HashMap<String, Post> map = new HashMap<String, Post>();

                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Post post = dataSnapshot.getValue(Post.class);

                        map.put(dataSnapshot.getKey(), post);
                        posts.add(0, post);

                        adapter.notifyItemInserted(0);
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        Post post = dataSnapshot.getValue(Post.class);

                        Post changedPost = map.get(dataSnapshot.getKey());
                        changedPost.copy(post);
                        adapter.notifyItemChanged(posts.indexOf(changedPost));
                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        Post post = map.remove(dataSnapshot.getKey());
                        int index = posts.indexOf(post);
                        posts.remove(index);

                        adapter.notifyItemRemoved(index);
                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }

                });
    }

    public void getSize(String parentKey, final Api.OnValueChangeListener listener) {
        api.database.getReference(path)
                .orderByChild("parentKey")
                .equalTo(parentKey)
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
