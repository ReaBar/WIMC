package com.example.reabar.wimc;

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
import android.widget.Toast;

import com.example.reabar.wimc.Fragments.HomeScreenFragment;
import com.example.reabar.wimc.Fragments.LoginScreenFragment;
import com.example.reabar.wimc.Fragments.ManageMyCarsScreenFragment;
import com.example.reabar.wimc.Fragments.MySharedCarsScreenFragment;
import com.example.reabar.wimc.Fragments.SettingsScreenFragment;
import com.example.reabar.wimc.Fragments.SignupScreenFragment;
import com.example.reabar.wimc.Model.Model;
import com.example.reabar.wimc.Model.Parking;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, FragmentCommunicator {

    ActionBarDrawerToggle toggle;
    DrawerLayout drawer;
    MyApplication myApplication;

    //Fragments
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    LoginScreenFragment loginFragment;
    SignupScreenFragment signUpFragment;
    HomeScreenFragment homeFragment;
    SettingsScreenFragment settingsFragment;
    ManageMyCarsScreenFragment manageMyCarsFragment;
    MySharedCarsScreenFragment mySharedCarsFragment;


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

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

//        Car car = new Car("112233", "Blue", "2015", "Honda", "rea.bar@gmail.com");
//        String share1 = "tomer_aronovsky@hotmail.com";
//        String share2 = "tasolutions2012@gmail.com";
//        ArrayList<String> s = new ArrayList<String>();
//        s.add(share1);
//        s.add(share2);
//        car.setUsersList(s);
//        Model.getInstance().addCarToDB(car, new Model.AddNewCarListener() {
//            @Override
//            public void success(boolean success) {
//                if (success) {
//                }
//            }
//            @Override
//            public void failed(String message) {
//            }
//        });

        Parking parking = new Parking.ParkingBuilder("123456").city("Tel Aviv").streetNumber(123).build();
        Model.getInstance().parkCar(parking, new Model.SyncListener() {
            @Override
            public void isSuccessful(boolean success) {
                Toast.makeText(MainActivity.this, "added car parking", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void failed(String message) {

            }

            @Override
            public void PassData(Object data) {

            }
        });

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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id){
            case R.id.nav_menu_homepage:
                passString("HomeScreenFragment");
                break;
            case R.id.nav_menu_settings:
                passString("SettingsScreenFragment");
                break;
            case R.id.nav_menu_myCarsNow:
                Toast.makeText(this, "MyCarsNow", Toast.LENGTH_SHORT).show();
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
        if ( isEnabled ) {
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            toggle.setDrawerIndicatorEnabled(true);
            toggle.syncState();

        }
        else {
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
        }
    }

    @Override
    public void passData(Object[] data) {

    }




    //        fragmentManager = getSupportFragmentManager();
//        fragmentTransaction = getSupportFragmentManager().beginTransaction();
//        loginFragment = new LoginScreenFragment();
//        fragmentTransaction.add(R.id.main_frag_container,loginFragment,"loginFragment");
//        fragmentTransaction.show(loginFragment).addToBackStack("loginFragment").commit();

}
