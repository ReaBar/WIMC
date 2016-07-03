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

import com.example.reabar.wimc.Fragments.ForgotPasswordFragment;
import com.example.reabar.wimc.Fragments.LoginScreenFragment;
import com.example.reabar.wimc.Fragments.SignupScreenFragment;
import com.example.reabar.wimc.Model.User;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, FragmentCommunicator {

    ActionBarDrawerToggle toggle;
    DrawerLayout drawer;
    MyApplication myApplication;
    User currentUser;

    //Fragments
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    LoginScreenFragment loginFragment;
    SignupScreenFragment signUpFragment;
    ForgotPasswordFragment forgotPasswordFragment;

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

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        loginFragment = new LoginScreenFragment();
        fragmentTransaction.add(R.id.main_frag_container,loginFragment,"loginFragment");
        fragmentTransaction.show(loginFragment).addToBackStack("loginFragment").commit();

/*        Model.getInstance().getCurrentUser(new Model.GetCurrentUserListener() {
            @Override
            public void onResult(User user) {
                currentUser = user;
            }

            @Override
            public void onCancel() {}
        });*/
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

//        if (id == R.id.nav_camera) {
//            // Handle the camera action
//          }

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
            case "SignUpScreenFragment":
                signUpFragment = new SignupScreenFragment();
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.main_frag_container, signUpFragment, "SignUpScreenFragment");
                fragmentTransaction.addToBackStack(null).commit();
                break;
            case "ForgotPasswordFragment":
                forgotPasswordFragment = new ForgotPasswordFragment();
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.main_frag_container, forgotPasswordFragment, "ForgotPasswordFragment");
                fragmentTransaction.addToBackStack(null).commit();
                break;

        }
    }

    @Override
    public void passData(Object[] data) {

    }
}