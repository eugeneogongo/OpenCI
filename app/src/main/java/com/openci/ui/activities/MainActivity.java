/*
 * Created by Eugene on $file.created
 * Modified on $file.modified
 * Copyright (c) 2019.
 */

package com.openci.ui.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.openci.R;
import com.openci.apicommunicator.callbacks.IAPICallBack;
import com.openci.apicommunicator.models.UserResponse;
import com.openci.apicommunicator.restservices.UserService;
import com.openci.common.Constants;
import com.openci.core.DrawerNavigator;
import com.openci.core.FragmentNavigator;

import static com.openci.core.DrawerNavigator.changeTitle;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawerLayout;
    private DrawerNavigator drawerNavigator;
    private FragmentNavigator fragmentNavigator;
    TextView profile_name, profile_email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Adding Toolbar to Main screen
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //get Header Textviews on the Navigation
        View header = navigationView.getHeaderView(0);
        profile_email = header.findViewById(R.id.profile_email);
        profile_name = header.findViewById(R.id.profile_name);



        fragmentNavigator = new FragmentNavigator(getSupportFragmentManager());
        drawerNavigator = new DrawerNavigator(this, fragmentNavigator);
        UserService.getProfile(getToken("public_travis_token"), new IAPICallBack() {
            @Override
            public void onSuccess(@NonNull Object value) {
                UserResponse user = (UserResponse) value;
                new Handler(MainActivity.this.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        profile_name.setText(user.getName());
                        profile_email.setText(user.getEmail());
                    }
                });
            }

            @Override
            public void onError(@NonNull String errorMessage) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        //this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.search_button) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        drawerNavigator.selectItem(item);
        // Set action bar title
        if(changeTitle) {
            setTitle(item.getTitle());
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * @return Oath token from sharedprefences
     */
    String getToken(String name) {
        SharedPreferences pref = getSharedPreferences(Constants.PREFS_NAME, 0);
        Log.d("token: ", pref.getString(name, "none"));
        return pref.getString(name, "none");
    }
}
