package com.parlakov.medic.fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.parlakov.medic.Global;
import com.parlakov.medic.R;
import com.parlakov.medic.activities.AddEditExaminationActivity;
import com.parlakov.medic.adapters.AdapterFactory;
import com.parlakov.medic.interfaces.ChildFragmentListener;
import com.parlakov.medic.localdata.LocalData;
import com.parlakov.medic.localdata.LocalExaminations;
import com.parlakov.uow.IUowMedic;

import java.util.Calendar;

/**
 * Created by georgi on 13-11-11.
 */
public class ExaminationsListFragment extends ListFragment {

    //<editor-fold desc="members and getters">
    private final Calendar mCurrentDate;
    private IUowMedic mData;

    public IUowMedic getData() {
        if (mData == null) {
            mData = new LocalData(getActivity().getApplicationContext());
        }
        return mData;
    }
    //</editor-fold>

    //<editor-fold desc="constructors">
    public ExaminationsListFragment(){
        this(null);
    }

    public ExaminationsListFragment(Calendar date){
        mCurrentDate = date;
    }
    //</editor-fold>

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    // if a current date is present means that it is today's
    // examinations/appointments and sets the text accordingly
    private void setEmptyTextOnTodaysOrAllExaminations() {
        if(mCurrentDate != null){
            setEmptyText(getString(R.string.emptyText_todaysAppointments));
        } else {
            setEmptyText(getString(R.string.emptyText_examinations));
        }
    }

    @Override
    public void onListItemClick(ListView listView, View v,
                                int position, long id) {
        super.onListItemClick(listView, v, position, id);

        Intent viewExaminationDetails = new Intent(Global.VIEW_EXAMINATION_ACTION);
        viewExaminationDetails.putExtra(
                Global.EXAMINATION_DETAILS_ID_TO_VIEW, id);
        startActivity(viewExaminationDetails);
    }

    //<editor-fold desc="data loading async">
    private void initialize() {

        final Context context = getActivity().getApplicationContext();

        // will do the database read on a background thread
        // and add the list view adapter afterwards
        new AsyncTask<Void, Void, SimpleCursorAdapter>() {
            @Override
            protected SimpleCursorAdapter doInBackground(Void... params) {
                SimpleCursorAdapter adapter = null;
                try {
                    Cursor allExaminations = getCursorExaminations();

                    adapter = AdapterFactory
                            .getExaminationsSimpleCursorAdapter(allExaminations, context);

                }
                catch (SQLiteException ex) {
                    ex.printStackTrace();
                    // will return null
                }
                return adapter;
            }

            @Override
            protected void onPostExecute(SimpleCursorAdapter simpleCursorAdapter) {
                if(simpleCursorAdapter == null){
                    ChildFragmentListener parentActivity =
                            (ChildFragmentListener) getActivity();

                    if(parentActivity != null){
                        parentActivity.showErrorMessageAndExit(
                            R.string.toast_exception_dbNoFoundMaybeSDMissing);
                    }
                    else{
                        getActivity().finish();
                    }
                    return;
                }
                setListAdapter(simpleCursorAdapter);
                closeDataConnection();
            }
        }.execute();

        setEmptyTextOnTodaysOrAllExaminations();
    }

    // if a currentDate is set finds the examinations in 24 hour interval
    // else gives zeroes and the data method returns all of them
    private Cursor getCursorExaminations() {
        LocalExaminations examinations =
                (LocalExaminations) getData().getExaminations();

        long zeroZeroHoursInMillis = 0;
        long zeroZeroHoursNextDayInMillis = 0;
        if(mCurrentDate != null){
            Calendar workingDate = Calendar.getInstance();

            workingDate.set(Calendar.HOUR_OF_DAY, 0);
            workingDate.set(Calendar.MINUTE, 0);
            workingDate.set(Calendar.MILLISECOND, 0);
            zeroZeroHoursInMillis = workingDate.getTimeInMillis();

            workingDate.add(Calendar.HOUR_OF_DAY, 24);
            zeroZeroHoursNextDayInMillis = workingDate.getTimeInMillis();
        }

        return examinations.getAllWithPatientNames(zeroZeroHoursInMillis,
                zeroZeroHoursNextDayInMillis);
    }
    //</editor-fold>

    //<editor-fold desc="Action menu">
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.at_examinations_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Boolean handled = true;

        int selectedItemId = item.getItemId();
        switch (selectedItemId) {
            case R.id.action_addExamination:
                handleAddExamination();
                break;
            default:
                handled = super.onOptionsItemSelected(item);
                break;
        }

        return handled;
    }

    private void handleAddExamination() {
        Intent addExaminationIntent = new Intent(getActivity(),
                AddEditExaminationActivity.class);

        startActivity(addExaminationIntent);
    }
    //</editor-fold>

    //<editor-fold desc="life-cycle management">
    @Override
    public void onStart() {
        super.onStart();

        initialize();
    }

    @Override
    public void onStop() {
        super.onStop();
        closeDataConnection();
    }
    //</editor-fold>

    private void closeDataConnection(){
        if(mData!= null){
            mData.closeDb();
            mData = null;
        }
    }
}
