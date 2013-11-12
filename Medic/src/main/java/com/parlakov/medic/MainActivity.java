package com.parlakov.medic;

import android.app.SearchManager;
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
import com.parlakov.medic.fragments.ExaminationsListFragment;
import com.parlakov.medic.fragments.PatientsListFragment;
import com.parlakov.medic.fragments.TodaysAppointmentsFragment;

public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    //TODO - refactor - extract a NavigationDrawerHelper class

    public static final int TODAYS_APPOINTMENTS_DRAWER_POSITION = 0;
    public static final int PATIENTS_LIST_DRAWER_POSITION = 1;
    public static final int EXAMINATIONS_LIST_DRAWER_POSITION = 2;
    public static final int LOGIN_DRAWER_POSITION = 5;
    public static final int REGISTER_DRAWER_POSITION = 4;

    private static final int CHOOSE_LOCATION_REQUEST_CODE = 1000;

    public static final int NOT_CHOSEN_LOCATION = 0;
    public static final int SAVE_LOCATION_SD_CARD = 1;
    public static final int SAVE_LOCATION_DEVICE_MEMORY = 2;

    public static final String APP_SAVE_DATA_LOCATION = "save data location";
    public static final String APP_SAVE_DATA_LOCATION_EXTRA = "save data location extra";
    public static final String PREFERENCES_NAME = "com.parlakov.medic.APP_PREFERENCES";


    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    private String mQuery;
    private FragmentManager fragmentManager;

    private FragmentManager getFragmentManagerLazy(){
        if(fragmentManager == null){
            fragmentManager = getSupportFragmentManager();
        }

        return fragmentManager;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManagerLazy().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout_main));


        Intent startingIntent = getIntent();
        if(Intent.ACTION_SEARCH.equals(startingIntent.getAction())){
            mQuery = startingIntent.getStringExtra(SearchManager.QUERY);

            getFragmentManagerLazy().beginTransaction()
                    .replace(R.id.container_medic_main, new PatientsListFragment(mQuery))
                    .commit();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == CHOOSE_LOCATION_REQUEST_CODE ){
            switch (resultCode){
                case RESULT_OK:
                    int chosenLocation = data
                            .getIntExtra(APP_SAVE_DATA_LOCATION_EXTRA, NOT_CHOSEN_LOCATION);

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
        // if save data location is not chosen an activity
        // will be started to get the user's preference
        if (!isSaveDataLocationChosen()) return;

        //stop the default choice if there is a query
        if(mQuery != null) return;

        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();

        switch (position) {
            case TODAYS_APPOINTMENTS_DRAWER_POSITION:
                fragmentManager.beginTransaction()
                        .replace(R.id.container_medic_main, new TodaysAppointmentsFragment())
                        .commit();
                break;

            case EXAMINATIONS_LIST_DRAWER_POSITION:
                fragmentManager.beginTransaction()
                        .replace(R.id.container_medic_main, new ExaminationsListFragment())
                        .commit();
                break;

            case PATIENTS_LIST_DRAWER_POSITION:
                fragmentManager.beginTransaction()
                        .replace(R.id.container_medic_main, new PatientsListFragment())
                        .commit();
                break;

            default:
                break;
        }
    }

    private boolean isSaveDataLocationChosen() {
        SharedPreferences pref = getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
        int prefferedSaveDataLocation = pref.getInt(APP_SAVE_DATA_LOCATION, NOT_CHOSEN_LOCATION);
        if (prefferedSaveDataLocation == NOT_CHOSEN_LOCATION){
            Intent chooseIntent = new Intent(getApplicationContext(),
                    ChooseSaveDataLocationActivity.class);
            startActivityForResult(chooseIntent, CHOOSE_LOCATION_REQUEST_CODE);
            return false;
        }
        return true;
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
        Boolean handled = true;

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_search_patients:
                onSearchRequested();
                break;
            case R.id.action_settings:
                break;
            default:
                handled = super.onOptionsItemSelected(item);
                break;
        }

        return handled;
    }
}
