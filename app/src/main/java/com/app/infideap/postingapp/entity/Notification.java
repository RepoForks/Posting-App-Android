package com.app.infideap.postingapp.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;

/**
 * Created by Shiburagi on 10/01/2017.
 */
@Entity(nameInDb = "Notification", indexes = {

})
public class Notification implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final String TAG = Notification.class.getSimpleName();

    @Id(autoincrement = true)
    public Long notificationId;

    public String title;

    public String message;

    public String icon;

    public Integer seen = 0;

    public Integer read = 0;

    public String dateReceived;

    @Generated(hash = 138942332)
    public Notification(Long notificationId, String title, String message,
            String icon, Integer seen, Integer read, String dateReceived) {
        this.notificationId = notificationId;
        this.title = title;
        this.message = message;
        this.icon = icon;
        this.seen = seen;
        this.read = read;
        this.dateReceived = dateReceived;
    }

    @Generated(hash = 1855225820)
    public Notification() {
    }

    public Long getNotificationId() {
        return this.notificationId;
    }

    public void setNotificationId(Long notificationId) {
        this.notificationId = notificationId;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getIcon() {
        return this.icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getSeen() {
        return this.seen;
    }

    public void setSeen(Integer seen) {
        this.seen = seen;
    }

    public Integer getRead() {
        return this.read;
    }

    public void setRead(Integer read) {
        this.read = read;
    }

    public String getDateReceived() {
        return this.dateReceived;
    }

    public void setDateReceived(String dateReceived) {
        this.dateReceived = dateReceived;
    }


}
