package com.app.infideap.postingapp.entity;

import java.io.Serializable;
import java.lang.reflect.Field;

/**
 * Created by Shiburagi on 11/02/2017.
 */
public class Post implements Serializable{
    public String text;
    public String parentKey;
    public long createdDate;
    public long updatedDate;
    public String createdBy;
    public String key;

    public void copy(Post post) {
        Field []fields = Post.class.getFields();
        for (Field field: fields){
            try {
                field.set(this, field.get(post));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
