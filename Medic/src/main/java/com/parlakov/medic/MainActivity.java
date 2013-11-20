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

import com.parlakov.medic.fragments.ExaminationsListFragment;
import com.parlakov.medic.fragments.PatientsListFragment;
import com.parlakov.medic.interfaces.ChildFragmentListener;

import java.util.Calendar;

public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks,
        ChildFragmentListener {

    //TODO - refactor - extract a NavigationDrawerHelper class

    //<editor-fold desc="Constants">
    public static final int TODAYS_APPOINTMENTS_DRAWER_POSITION = 0;
    public static final int PATIENTS_LIST_DRAWER_POSITION = 1;
    public static final int EXAMINATIONS_LIST_DRAWER_POSITION = 2;

    private static final int CHOOSE_LOCATION_REQUEST_CODE = 1000;

    private static final String SETTINGS_ACTION = "com.parlakov.medic.ACTION_SETTINGS";
    //</editor-fold>

    //<editor-fold desc="memebers and getters">
    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    private FragmentManager fragmentManager;

    private FragmentManager getFragmentManagerLazy(){
        if(fragmentManager == null){
            fragmentManager = getSupportFragmentManager();
        }

        return fragmentManager;
    }
    //</editor-fold>

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManagerLazy().findFragmentById(R.id.navigation_drawer);

        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout_main));
    }

    // handles new intents (not the initially creating but successive)
    // in this case intents by search invokation
    @Override
    protected void onNewIntent(Intent intent) {
        handleSearchIntent(intent);    }

    // handles the return from settings data save location
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == CHOOSE_LOCATION_REQUEST_CODE &&
                resultCode == RESULT_CANCELED){
            // if user has not chosen location end the main activity and exit
            finish();
        }
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // if save data location is not chosen an activity
        // will be started to get the user's preference
        if (!isSaveDataLocationChosen()) return;

        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();

        switch (position) {
            case TODAYS_APPOINTMENTS_DRAWER_POSITION:
                /* gives the current date to the instance so that
                *  only appointments/examinations from today are shown*/

                fragmentManager.beginTransaction()
                        .replace(R.id.container_medic_main,
                                new ExaminationsListFragment(Calendar.getInstance()))
                        .commit();
                mTitle = getString(R.string.title_sectionTodaysAppointments);
                break;

            case EXAMINATIONS_LIST_DRAWER_POSITION:
                fragmentManager.beginTransaction()
                        .replace(R.id.container_medic_main, new ExaminationsListFragment())
                        .commit();
                mTitle = getString(R.string.title_sectionExaminationsList);
                break;

            case PATIENTS_LIST_DRAWER_POSITION:
                fragmentManager.beginTransaction()
                        .replace(R.id.container_medic_main, new PatientsListFragment())
                        .commit();
                mTitle = getString(R.string.title_sectionPatientsList);
                break;

            default:
                break;
        }
    }

    //<editor-fold desc="Action bar menu">
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
                Intent settingsIntent = new Intent(SETTINGS_ACTION);
                startActivity(settingsIntent);
                break;
            default:
                handled = super.onOptionsItemSelected(item);
                break;
        }

        return handled;
    }
    //</editor-fold>

    private void handleSearchIntent(Intent startingIntent) {
        if(Intent.ACTION_SEARCH.equals(startingIntent.getAction())){
            String query = startingIntent.getStringExtra(SearchManager.QUERY);

            getFragmentManagerLazy().beginTransaction()
                    .replace(R.id.container_medic_main,
                            new PatientsListFragment(query))
                    .commit();
        }
    }

    private boolean isSaveDataLocationChosen() {
        SharedPreferences pref = getSharedPreferences(Global.PROPERTYS_NAME,
                MODE_PRIVATE);
        int prefferedSaveDataLocation = pref.getInt(Global.PROPERTY_SAVE_LOCATION,
                Global.NOT_CHOSEN_LOCATION);

        if (prefferedSaveDataLocation == Global.NOT_CHOSEN_LOCATION){
            Intent chooseIntent = new Intent(SETTINGS_ACTION);
            startActivityForResult(chooseIntent, CHOOSE_LOCATION_REQUEST_CODE);
            return false;
        }
        return true;
    }

    //<editor-fold desc="Child fragments listener implementation">
    @Override
    public void onChildFragmentClose() {
        onNavigationDrawerItemSelected(
            mNavigationDrawerFragment.getCurrentSelectedPosition());
    }

    @Override
    public void showErrorMessageAndExit(int resourceId) {

        Toast.makeText(this,
                getString(resourceId),
                Toast.LENGTH_LONG)
                .show();

        finish();
    }
    //</editor-fold>
}
