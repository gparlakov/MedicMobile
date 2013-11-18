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

import com.parlakov.medic.interfaces.ChildFragmentListener;
import com.parlakov.medic.R;
import com.parlakov.medic.activities.AddEditPatientActivity;
import com.parlakov.medic.activities.PatientManagementActivity;
import com.parlakov.medic.localdata.LocalData;
import com.parlakov.medic.localdata.LocalPatients;
import com.parlakov.medic.localdata.MedicDbContract;
import com.parlakov.medic.viewbinders.PatientsListViewBinder;
import com.parlakov.uow.IUowMedic;

/**
 * Created by georgi on 13-11-5.
 */
public class PatientsListFragment extends ListFragment {

    //<editor-fold desc="members and getters">
    private final String mQuery;
    private IUowMedic mData;
    private Cursor mPatientsCursor;

    public String getQuery() {
        return mQuery;
    }

    public IUowMedic getData() {
        if(mData == null){
            mData = new LocalData(getActivity());
        }
        return mData;
    }
    //</editor-fold>

    //<editor-fold desc="constructors">
    public PatientsListFragment() {
        this(null);
    }

    public PatientsListFragment(String query) {
        mQuery = query;
    }
    //</editor-fold>

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setHasOptionsMenu(true);

        if(mQuery != null){
            setEmptyText(getString(R.string.search_nothing_found)
                    + "\"" + mQuery + "\"");
        }else{
            setEmptyText(getString(R.string.emptyText_patientsList));
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        handlePatientItemClicked(id);
    }

    private void handlePatientItemClicked(long id) {
        Intent showPatientDetails = new Intent(getActivity(), PatientManagementActivity.class);
        showPatientDetails.putExtra(PatientManagementActivity.PATIENT_ID_EXTRA, id);
        startActivity(showPatientDetails);
    }

    private void initialize() {

        final Context context = getListView().getContext();

        new AsyncTask<Void, Void, SimpleCursorAdapter>() {
            @Override
            protected SimpleCursorAdapter doInBackground(Void... params) {
                Cursor patientsCursor;
                try{
                    if(getQuery() == null){
                        // not a search-started fragment
                        patientsCursor = (Cursor) getData().getPatients().getAll();
                    }
                    else{
                        // a search-started fragment
                        LocalPatients patientsData = (LocalPatients) getData().getPatients();
                        patientsCursor = patientsData.searchByName(getQuery());
                    }

                    SimpleCursorAdapter adapter =
                            getPatientsSimpleCursorAdapter(context, patientsCursor);

                    return  adapter;
                }
                catch(SQLiteException ex){
                    ex.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(SimpleCursorAdapter adapter) {
                if(adapter == null){
                    ChildFragmentListener parentActivity =
                            (ChildFragmentListener) getActivity();
                    if(parentActivity != null){
                        parentActivity.showErrorMessageAndExit(
                                R.string.toast_exception_dbNoFoundMaybeSDMissing);
                    } else {
                        getActivity().finish();
                    }
                    return;
                }
                setListAdapter(adapter);
            }
        }.execute();

    }

    private SimpleCursorAdapter getPatientsSimpleCursorAdapter(
            Context context, Cursor patients) {

        String[] from = new String[]
                {
                        MedicDbContract.Patient.COLUMN_NAME_PHOTO_PATH,
                        MedicDbContract.Patient.COLUMN_NAME_FIRST_NAME,
                        MedicDbContract.Patient.COLUMN_NAME_LAST_NAME,
                };

        int[] to = new int[]
                {
                        R.id.listItemPatientImage,
                        R.id.listItemPatientFirstName,
                        R.id.listItemPatientLastName
                };

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(context,
                R.layout.item_patient,
                patients,
                from,
                to,
                0);

        adapter.setViewBinder(new PatientsListViewBinder());

        return adapter;
    }

    //<editor-fold desc="action bar management">
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if(mQuery != null){
            inflater.inflate(R.menu.at_patients_search_result, menu);

        } else {
            inflater.inflate(R.menu.at_patients_list, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Boolean handled = true;

        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.action_addPatient:
                Intent newPatientIntent = new Intent(getActivity(), AddEditPatientActivity.class);
                startActivity(newPatientIntent);
                break;
            case R.id.action_returnToPatientsList:
                ChildFragmentListener listener = (ChildFragmentListener) getActivity();
                listener.onChildFragmentClose();
                break;
            default:
                handled = super.onOptionsItemSelected(item);
                break;
        }

        return handled;
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

    private void closeDataConnection() {
        if(mData != null){
            mData.closeDb();
            mData = null;
        }

        if(mPatientsCursor != null){
            mPatientsCursor.close();
            mPatientsCursor = null;
        }
    }
}
