package com.github.hyota.asciiartboardreader.presentation.main;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.github.hyota.asciiartboardreader.R;
import com.github.hyota.asciiartboardreader.domain.model.BbsInfo;
import com.github.hyota.asciiartboardreader.domain.model.ThreadInfo;
import com.github.hyota.asciiartboardreader.presentation.bbslist.BbsListFragment;
import com.github.hyota.asciiartboardreader.presentation.threadresponselist.ThreadResponseListFragment;
import com.github.hyota.asciiartboardreader.presentation.threadlist.ThreadListFragment;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity
        implements MainContract.View, HasSupportFragmentInjector, NavigationView.OnNavigationItemSelectedListener,
        BbsListFragment.OnBbsSelectListener, ThreadListFragment.OnThreadSelectListener {

    @Inject
    DispatchingAndroidInjector<Fragment> fragmentInjector;
    @Inject
    MainContract.Presenter presenter;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show());

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FragmentManager manager = getSupportFragmentManager();
        if (manager.findFragmentByTag("main") == null) {
            // 初回時には取得できないため初期化する
            manager.beginTransaction()
                    .add(R.id.container, BbsListFragment.newInstance(), "main")
                    .addToBackStack(null)
                    .commit();
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
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

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return fragmentInjector;
    }

    @Override
    public void onBbsSelect(BbsInfo bbsInfo) {
        FragmentManager manager = getSupportFragmentManager();
        String tag = Stream.of(bbsInfo.getHost(), bbsInfo.getCategory(), bbsInfo.getDirectory())
                .filter(it -> it != null)
                .collect(Collectors.joining("/"));
        Fragment fragment = manager.findFragmentByTag(tag);
        if (fragment != null) {
            manager.beginTransaction()
                    .replace(R.id.container, fragment, tag)
                    .addToBackStack(null)
                    .commit();
        } else {
            manager.beginTransaction()
                    .add(R.id.container, ThreadListFragment.newInstance(bbsInfo), tag)
                    .addToBackStack(null)
                    .commit();
        }
    }

    @Override
    public void onThreadSelect(ThreadInfo threadInfo) {
        FragmentManager manager = getSupportFragmentManager();
        String tag = Stream.of(threadInfo.getBbsInfo().getHost(), threadInfo.getBbsInfo().getCategory(), threadInfo.getBbsInfo().getDirectory(), String.valueOf(threadInfo.getUnixTime()))
                .filter(it -> it != null)
                .collect(Collectors.joining("/"));
        Fragment fragment = manager.findFragmentByTag(tag);
        if (fragment != null) {
            manager.beginTransaction()
                    .replace(R.id.container, fragment, tag)
                    .addToBackStack(null)
                    .commit();
        } else {
            manager.beginTransaction()
                    .add(R.id.container, ThreadResponseListFragment.newInstance(threadInfo), tag)
                    .addToBackStack(null)
                    .commit();
        }
    }
}
