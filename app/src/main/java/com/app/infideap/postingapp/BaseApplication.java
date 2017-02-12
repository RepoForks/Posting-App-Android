package com.app.infideap.postingapp;

import android.app.Application;

import com.app.infideap.notificationapp.DaoMaster;
import com.app.infideap.notificationapp.DaoSession;
import com.app.infideap.postingapp.resource.Api;
import com.google.firebase.database.FirebaseDatabase;

import org.greenrobot.greendao.database.Database;

/**
 * Created by Shiburagi on 14/06/2016.
 */
public class BaseApplication extends Application {
    private static final boolean ENCRYPTED = false;
    private DaoSession daoSession;

    @Override
    public void onCreate() {

        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

//        FirebaseApp.initializeApp(this);

        String databaseName = ENCRYPTED ?
                "notification-encrypted.db" : "notification.db";
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, databaseName);

        String password ="123456";
        Database db = ENCRYPTED ? helper.getEncryptedWritableDb(password) : helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();

        Api.init(FirebaseDatabase.getInstance());

    }

    public DaoSession getDaoSession() {
        return daoSession;
    }
}
