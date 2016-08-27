package com.example.reabar.wimc;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.reabar.wimc.Fragments.CarScreenFragment;
import com.example.reabar.wimc.Fragments.HomeScreenFragment;
import com.example.reabar.wimc.Fragments.LoginScreenFragment;
import com.example.reabar.wimc.Fragments.ManageMyCarsScreenFragment;
import com.example.reabar.wimc.Fragments.MyCarsNowScreenFragment;
import com.example.reabar.wimc.Fragments.MySharedCarsScreenFragment;
import com.example.reabar.wimc.Fragments.ParkingPhotoFragment;
import com.example.reabar.wimc.Fragments.ParkingScreenFragment;
import com.example.reabar.wimc.Fragments.SettingsScreenFragment;
import com.example.reabar.wimc.Fragments.SignupScreenFragment;
import com.example.reabar.wimc.Model.Car;
import com.example.reabar.wimc.Model.Model;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, FragmentCommunicator {

    ActionBarDrawerToggle toggle;
    DrawerLayout drawer;
    MyApplication myApplication;

    //Fragments
    FragmentTransaction fragmentTransaction;
    LoginScreenFragment loginFragment;
    SignupScreenFragment signUpFragment;
    HomeScreenFragment homeFragment;
    SettingsScreenFragment settingsFragment;
    ManageMyCarsScreenFragment manageMyCarsFragment;
    MySharedCarsScreenFragment mySharedCarsFragment;
    ParkingScreenFragment parkingFragment;
    MyCarsNowScreenFragment MyCarNowFragment;
    CarScreenFragment carScreenFragment;
    ParkingPhotoFragment parkingPhotoFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        myApplication = new MyApplication(this.getApplicationContext(), this);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

/*        Model.getInstance().getAllCars(new Model.SyncListener() {
            @Override
            public void isSuccessful(boolean success) {

            }

            @Override
            public void failed(String message) {

            }

            @Override
            public void passData(Object data) {
            }
        });*/
        passString("HomeScreenFragment");
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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_menu_homepage:
                passString("HomeScreenFragment");
                break;
            case R.id.nav_menu_settings:
                passString("SettingsScreenFragment");
                break;
            case R.id.nav_menu_myCarsNow:
                passString("MyCarsNowScreenFragment");
                break;
            case R.id.nav_menu_manageMyCars:
                passString("ManageMyCarsScreenFragment");
                break;
            case R.id.nav_menu_manageSahredCars:
                passString("MySharedCarsScreenFragment");
                break;

            case R.id.nav_menu_logoutapp:
                Model.getInstance().logoutUser();
                passString("LoginScreenFragment");
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setDrawerState(boolean isEnabled) {
        if (isEnabled) {
            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);

            if (Model.getInstance().getCurrentUser() != null) {
                String userName = Model.getInstance().getCurrentUser().getEmail();
                View header = navigationView.getHeaderView(0);
                TextView userNameHeader = (TextView) header.findViewById(R.id.nav_drawer_header_user_name);
                userNameHeader.setText(userName);
            }
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            toggle.setDrawerIndicatorEnabled(true);
            toggle.syncState();
        } else {
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            toggle.setDrawerIndicatorEnabled(false);
            toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onSupportNavigateUp();
                }
            });
            toggle.syncState();
        }
    }

    @Override
    public void passString(String text) {
        switch (text) {
            case "cancelDrawer":
                setDrawerState(false);
                break;
            case "enableDrawer":
                setDrawerState(true);
                break;
            case "LoginScreenFragment":
                loginFragment = new LoginScreenFragment();
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.main_frag_container, loginFragment, "LoginFragment");
                fragmentTransaction.addToBackStack(null).commit();
                break;
            case "SignUpScreenFragment":
                signUpFragment = new SignupScreenFragment();
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.main_frag_container, signUpFragment, "SignUpScreenFragment");
                fragmentTransaction.addToBackStack(null).commit();
                break;
            case "HomeScreenFragment":
                homeFragment = new HomeScreenFragment();
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.main_frag_container, homeFragment, "HomeScreenFragment");
                fragmentTransaction.addToBackStack(null).commit();
                break;
            case "SettingsScreenFragment":
                settingsFragment = new SettingsScreenFragment();
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.main_frag_container, settingsFragment, "SettingsScreenFragment");
                fragmentTransaction.addToBackStack(null).commit();
                break;
            case "ManageMyCarsScreenFragment":
                manageMyCarsFragment = new ManageMyCarsScreenFragment();
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.main_frag_container, manageMyCarsFragment, "ManageMyCarsScreenFragment");
                fragmentTransaction.addToBackStack(null).commit();
                break;
            case "MySharedCarsScreenFragment":
                mySharedCarsFragment = new MySharedCarsScreenFragment();
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.main_frag_container, mySharedCarsFragment, "MySharedCarsScreenFragment");
                fragmentTransaction.addToBackStack(null).commit();
                break;
            case "MyCarsNowScreenFragment":
                MyCarNowFragment = new MyCarsNowScreenFragment();
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.main_frag_container, MyCarNowFragment, "MyCarsNowScreenFragment");
                fragmentTransaction.addToBackStack(null).commit();
                break;
        }
    }

    @Override
    public void passData(Object[] data, String text) {

        switch (text) {
            case "ParkingScreenFragment":
                parkingFragment = new ParkingScreenFragment();
                parkingFragment.carID = (String) data[0];
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.main_frag_container, parkingFragment, "ParkingScreenFragment");
                fragmentTransaction.addToBackStack(null).commit();
                break;
            case "MapScreenFragment":
                Uri gmmIntentUri = Uri.parse("google.navigation:q=" + data[0] + "," + data[1] + "&mode=w");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
                break;
            case "CarScreenFragment":
                carScreenFragment = new CarScreenFragment();
                carScreenFragment.car = (Car) data[0];
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.main_frag_container, carScreenFragment, "CarScreenFragment");
                fragmentTransaction.addToBackStack(null).commit();
                break;
            case "ParkingPhotoScreenFragment":
                parkingPhotoFragment = new ParkingPhotoFragment();
                parkingPhotoFragment.photoName = (String) data[0];
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.main_frag_container, parkingPhotoFragment, "ParkingPhotoScreenFragment");
                fragmentTransaction.addToBackStack(null).commit();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
