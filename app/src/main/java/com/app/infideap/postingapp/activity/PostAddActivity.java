package com.app.infideap.postingapp.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.app.infideap.notificationapp.R;
import com.app.infideap.postingapp.entity.Post;
import com.app.infideap.postingapp.resource.Api;

public class PostAddActivity extends BaseActivity {

    private EditText postEditText;
    private TextView usernameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_add);

        init();
    }

    private void init() {
        postEditText = (EditText) findViewById(R.id.editText_post);
        usernameEditText = (TextView) findViewById(R.id.textView_username);

        Api.getInstance().user()
                .getName(auth.getCurrentUser().getUid(), usernameEditText);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_post_add, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_post:
                Api.getInstance().post().add(wrap());
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private Post wrap() {
        Post post = new Post();
        post.text = postEditText.getText().toString();
        post.parentKey = null;
        post.createdBy = auth.getCurrentUser().getUid();
        post.createdDate = System.currentTimeMillis();
        post.updatedDate = post.createdDate;
        return post;
    }
}
