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

    public Api(FirebaseDatabase database) {
        this.database = database;

        post  = new PostApi(this);
        user = new UserApi(this);
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
}
