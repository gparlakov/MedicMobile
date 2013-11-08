package com.parlakov.medic;


import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;
import android.widget.Toast;

import com.parlakov.medic.activities.ChooseSaveDataLocationActivity;
import com.parlakov.medic.fragments.LoginFragment;
import com.parlakov.medic.fragments.PatientsListFragment;
import com.parlakov.medic.fragments.RegisterFragment;

public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private static final int HOME_DRAWER_POSITION = 0;
    private static final int LOGIN_DRAWER_POSITION = 1;
    private static final int REGISTER_DRAWER_POSITION = 2;

    public static final int SAVE_LOCATION_SD_CARD = 1;
    public static final int SAVE_LOCATION_DEVICE_MEMORY = 2;
    public static final int NOT_CHOSEN_LOCATION = 0;

    public static final String APP_SAVE_DATA_LOCATION = "save data location";
    public static final String APP_SAVE_DATA_LOCATION_EXTRA = "save data location extra";

    private static final int CHOOSE_LOCATION_REQUEST_CODE = 1000;
    public static final String PREFERENCES_NAME = "com.parlakov.medic.APP_PREFERENCES";


    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layoutCustomMy));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == CHOOSE_LOCATION_REQUEST_CODE ){
            switch (resultCode){
                case RESULT_OK:
                    int chosenLocation = data.getIntExtra(APP_SAVE_DATA_LOCATION_EXTRA, NOT_CHOSEN_LOCATION);
                    if (chosenLocation == NOT_CHOSEN_LOCATION){
                        finish();
                        return;
                    }
                    SharedPreferences pref = getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
                    SharedPreferences.Editor edit = pref.edit();
                    edit.putInt(APP_SAVE_DATA_LOCATION, chosenLocation).commit();

                    break;
                case RESULT_CANCELED:
                    // if user has not chosen location end the main activity and exit
                    finish();
                    break;
            }
        }
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        SharedPreferences pref = getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
        int prefferedSaveDataLocation = pref.getInt(APP_SAVE_DATA_LOCATION, NOT_CHOSEN_LOCATION);
        if (prefferedSaveDataLocation == NOT_CHOSEN_LOCATION){
            Intent chooseIntent = new Intent(getApplicationContext(), ChooseSaveDataLocationActivity.class);
            try{
                startActivityForResult(chooseIntent, CHOOSE_LOCATION_REQUEST_CODE);
            }
            catch (Exception ex){
                Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
            }
            return;
        }


        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();

        switch (position) {
            case HOME_DRAWER_POSITION:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, new PatientsListFragment())
                        .commit();
                break;

            case LOGIN_DRAWER_POSITION:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, new LoginFragment())
                        .commit();
                break;

            case REGISTER_DRAWER_POSITION:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, new RegisterFragment())
                        .commit();
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (mNavigationDrawerFragment != null && !mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.global, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
