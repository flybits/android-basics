package com.flybits.samples.android.basics;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import com.flybits.samples.android.basics.fragments.ConnectionFragment;
import com.flybits.samples.android.basics.fragments.ContentFragment;
import com.flybits.samples.android.basics.fragments.ContextDataFragment;
import com.flybits.samples.android.basics.fragments.PushHistoryFragment;
import com.flybits.samples.android.basics.interfaces.IConnection;
import com.flybits.samples.android.basics.workers.PeriodicLocationRetrievalWorker;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, IConnection {

    private String tagSelected = "";
    private static final String TAG_CONNECT = "TAG_FRAGMENT_CONNECTION";
    private static final String TAG_CONTENT = "TAG_FRAGMENT_CONTENT";
    private static final String TAG_PUSH_HISTORY = "TAG_FRAGMENT_PUSH_HISTORY";
    private static final String TAG_CONTEXT = "TAG_FRAGMENT_CONTEXT";

    private TextView txtUserId;

    private static final int REQUEST_LOCATION = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


        View header = navigationView.getHeaderView(0);
        txtUserId = header.findViewById(R.id.txtId);

        onNavigationItemSelected(navigationView.getMenu().findItem(R.id.nav_connection));
        navigationView.setCheckedItem(R.id.nav_connection);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION);
        } else {
            startLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if(requestCode == REQUEST_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocation();
            }
        }
    }

    private void startLocation(){
        System.out.println("Location permissions granted, starting location on periodic worker");

        PeriodicWorkRequest locationWorker =
                new PeriodicWorkRequest.Builder(PeriodicLocationRetrievalWorker.class, 15, TimeUnit.MINUTES)
                        .addTag("LOCATION_WORKER")
                        .build();

        WorkManager.getInstance().enqueueUniquePeriodicWork(
                "LOCATION_WORKER",
                ExistingPeriodicWorkPolicy.REPLACE,
                locationWorker);

        System.out.println("Location permissions granted, starting location on one-time worker");
        OneTimeWorkRequest locationWorkerOneTime =
                new OneTimeWorkRequest.Builder(PeriodicLocationRetrievalWorker.class)
                .build();

        WorkManager.getInstance().enqueueUniqueWork("OYO",
                ExistingWorkPolicy.KEEP,locationWorkerOneTime);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        String tagFragment;
        Fragment fragmentSelected;

        int id = item.getItemId();

        switch (id) {
            case R.id.nav_connection:
            default:
                tagFragment = TAG_CONNECT;
                fragmentSelected = ConnectionFragment.newInstance();
                break;
            case R.id.nav_content:
                tagFragment = TAG_CONTENT;
                fragmentSelected = ContentFragment.newInstance();
                break;
            case R.id.nav_push_history:
                tagFragment = TAG_PUSH_HISTORY;
                fragmentSelected = PushHistoryFragment.newInstance();
                break;
            case R.id.nav_context:
                tagFragment = TAG_CONTEXT;
                fragmentSelected = ContextDataFragment.newInstance();
                break;
        }

        if (id == R.id.nav_connection) {
            // Handle the camera action
        }

        if (!tagSelected.equals(tagFragment)) {

            tagSelected = tagFragment;
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.layoutFragment, fragmentSelected, tagFragment)
                    .commitAllowingStateLoss();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void connected() {
        txtUserId.setText(getString(R.string.infoConnected));
    }

    @Override
    public void disconnected() {
        txtUserId.setText(getString(R.string.infoConnect));
    }
}
