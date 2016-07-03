package com.abraxas.spps.alma;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    String mTitle = "Home";
    int section;

    public static final String SECTION = "com.spps.alma.section";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Fragment fragment;

        if(savedInstanceState!=null) {
            section = savedInstanceState.getInt(SECTION);
            fragment = getSupportFragmentManager().findFragmentByTag("tag");
        } else {

            Bundle args = new Bundle();
            switch (section) {
                case 1:
                    fragment = new GradeFragment();
                    mTitle = "Grade";
                    args = this.getIntent().getExtras();
                    break;
                case 2:
                    fragment = new CaldroidFragment();
                    mTitle = "Calendar";
                    Calendar cal = Calendar.getInstance();
                    args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
                    args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
                    args.putBoolean(CaldroidFragment.ENABLE_SWIPE, true);
                    args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, true);
                    break;
                case 0:
                default:
                    fragment = new MainFragment();
                    mTitle = "Home";
                    args = this.getIntent().getExtras();
                    break;
            }
            //noinspection ConstantConditions
            getSupportActionBar().setTitle(mTitle);
            fragment.setArguments(args);
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment, "tag").commit();

        final CaldroidListener listener = new CaldroidListener() {
            @Override
            public void onSelectDate(Date date, View view) {

            }
        };


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        assert drawer != null;
        //noinspection deprecation
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        assert navigationView != null;
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SECTION, section);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        assert drawer != null;
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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id){
            case R.id.action_settings:
                return true;
            case R.id.action_logout:
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.putExtra(LaunchActivity.AUTOLOGIN, false);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment;

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        assert drawer != null;

        int currentPos;
        Bundle args = this.getIntent().getExtras();
        switch (id) {
            case R.id.nav_grade:
                fragment = new GradeFragment();
                mTitle = "Grade";
                currentPos = 1;
                break;
            case R.id.nav_attendance:
                fragment = new CaldroidFragment();
                mTitle = "Calendar";
                Calendar cal = Calendar.getInstance();
                args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
                args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
                args.putBoolean(CaldroidFragment.ENABLE_SWIPE, true);
                args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, true);
                currentPos = 2;
                break;
            case R.id.nav_main:
            default:
                fragment = new MainFragment();
                mTitle = "Home";
                currentPos = 0;
                break;
        }

        if(currentPos!=section){
            //noinspection ConstantConditions
            getSupportActionBar().setTitle(mTitle);
            fragment.setArguments(args);
            fragmentManager.beginTransaction().replace(R.id.container, fragment, "tag").commit();
            section = currentPos;
        }


        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}