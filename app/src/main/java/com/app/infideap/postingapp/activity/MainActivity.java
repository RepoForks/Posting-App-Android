package com.app.infideap.postingapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.app.infideap.notificationapp.NotificationDao;
import com.app.infideap.notificationapp.R;
import com.app.infideap.postingapp.BaseApplication;
import com.app.infideap.postingapp.fragment.CommentFragment;
import com.app.infideap.postingapp.fragment.PostFragment;
import com.app.infideap.postingapp.entity.Notification;
import com.app.infideap.postingapp.entity.Post;
import com.app.infideap.postingapp.entity.User;
import com.app.infideap.postingapp.fragment.NotificationFragment;
import com.app.infideap.postingapp.fragment.CommentBottomSheetFragment;
import com.app.infideap.postingapp.resource.Api;
import com.app.infideap.postingapp.util.IconTextDrawable;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        NotificationFragment.OnListFragmentInteractionListener,
        CommentFragment.OnListFragmentInteractionListener,
        PostFragment.OnListFragmentInteractionListener{

    private Toolbar toolbar;
    private DrawerLayout drawer;
    private Menu menu;
    private EditText usernameEditText;
    private View siginRequiredContent;
    private View loadingOverlayContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        fab.setVisibility(View.GONE);
        initDrawer();
        init();
    }

    private void init() {
        siginRequiredContent = findViewById(R.id.content_signin_required);
        loadingOverlayContent = findViewById(R.id.content_loading_overlay);
        usernameEditText = (EditText)findViewById(R.id.editText_username);
        if (auth.getCurrentUser() == null) {
            siginRequiredContent.setVisibility(View.VISIBLE);
            siginRequiredContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        } else {
            siginRequiredContent.setVisibility(View.GONE);
        }

        loadingOverlayContent.setVisibility(View.GONE);
        loadingOverlayContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        findViewById(R.id.layout_post_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, PostAddActivity.class));
            }
        });

        displayFragment(R.id.container_list, PostFragment.newInstance(1));
    }

    private void initDrawer() {

        final NotificationFragment fragment = NotificationFragment.newInstance(1);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                fragment.setUserVisibleHint(true);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                fragment.setUserVisibleHint(false);
            }
        };
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        toggle.setDrawerIndicatorEnabled(false);
        displayFragment(R.id.nav_view, fragment);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        this.menu = menu;

        onNotificationReceivedIteration(null);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_notification) {
            drawer.openDrawer(GravityCompat.END);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.END);
        return true;
    }

    @Override
    public void onListFragmentInteraction(Notification item) {

    }

    @Override
    public void onNotificationReceivedIteration(Notification notification) {
        BaseApplication application = (BaseApplication) getApplication();
        List<Notification> notifications = application.getDaoSession()
                .getNotificationDao().queryBuilder().where(
                        NotificationDao.Properties.Seen.eq(0)
                ).list();
        int size = notifications.size();
        MenuItem menuItem = menu.findItem(R.id.action_notification);
        if (size == 0)
            menuItem.setIcon(
                    new IconTextDrawable(
                            MainActivity.this, null,
                            R.drawable.ic_notifications_none_white_24dp,
                            0, 0

                    )
            );
        else {
            menuItem.setIcon(
                    new IconTextDrawable(
                            MainActivity.this, String.valueOf(size),
                            R.drawable.ic_notifications_white_24dp,
                            0, 0

                    )
            );
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Notification event) {
        onNotificationReceivedIteration(event);
    }

    public void done(View view) {
        loadingOverlayContent.setVisibility(View.VISIBLE);
        auth.signInAnonymously().addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                User user = new User();
                user.username = usernameEditText.getText().toString();
                user.loginMethod = "anonymously";
                user.uid = authResult.getUser().getUid();
                user.createdDate= System.currentTimeMillis();
                Api.getInstance()
                        .user()
                        .add(
                                user.uid,
                                user,
                                new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                        if (databaseError!=null){
                                            auth.signOut();
                                        }else{
                                            siginRequiredContent.setVisibility(View.GONE);
                                        }

                                        loadingOverlayContent.setVisibility(View.GONE);
                                    }
                                }
                        );
            }
        });
    }

    @Override
    public void onListFragmentInteraction(Post item) {

    }

    @Override
    public void onCommentClickIteration(View view, Post item) {
        CommentBottomSheetFragment.newInstance(item).show(
                getSupportFragmentManager(),
                CommentBottomSheetFragment.TAG
        );
    }

    @Override
    public void onReactClickIteration(View view, Post item, int type) {
        Api.getInstance().react()
                .toggle(item.key, auth.getCurrentUser().getUid(), type);
    }
}
