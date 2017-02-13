package com.app.infideap.postingapp.resource;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Shiburagi on 11/02/2017.
 */
public class Api {
    private static Api instance;
    protected final FirebaseDatabase database;
    private final PostApi post;
    private final UserApi user;
    private final ReactApi react;

    private Api(FirebaseDatabase database) {
        this.database = database;

        post  = new PostApi(this);
        user = new UserApi(this);
        react = new ReactApi(this);
    }

    public static void init(FirebaseDatabase database){
        instance = new Api(database);
    }
    public static Api getInstance() {
        return instance;
    }

    public PostApi post() {
        return post;
    }

    public UserApi user() {
        return user;
    }

    public ReactApi react() {
        return react;
    }

    public interface OnValueChangeListener {

        public void onSizeChanged(int size);
    }
}
