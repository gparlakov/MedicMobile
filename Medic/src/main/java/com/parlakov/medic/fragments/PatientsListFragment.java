package com.parlakov.medic.fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.parlakov.medic.R;
import com.parlakov.medic.activities.AddPatientActivity;
import com.parlakov.medic.localdata.LocalData;
import com.parlakov.medic.localdata.MedicDbContract;

/**
 * Created by georgi on 13-11-5.
 */
public class PatientsListFragment extends ListFragment {

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        initialize();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        handlePatientItemClicked(id);
    }

    private void handlePatientItemClicked(long id) {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.container,
                new PatientDetailsFragment(id)).commit();
    }

    private void initialize() {
        Context context = getListView().getContext();
        LocalData data = new LocalData(context);
        Cursor patients = data.getPatients().getAll();

        SimpleCursorAdapter adapter = getPatiensSimpleCursorAdapter(context, patients);

        this.setListAdapter(adapter);
        this.setEmptyText(getString(R.string.emptyPatientsList));
    }

    private SimpleCursorAdapter getPatiensSimpleCursorAdapter(Context context, Cursor patients) {
        String[] from = new String[]
                {
                        MedicDbContract.Patient.COLUMN_NAME_IMAGE_PATH,
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

        adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                if (columnIndex ==
                        cursor.getColumnIndex(MedicDbContract.Patient.COLUMN_NAME_IMAGE_PATH)) {
                    String picturePath = cursor.getString(columnIndex);
                    ImageView patientPicture = (ImageView) view;
                    if (picturePath != null && !picturePath.isEmpty()) {
                        patientPicture.setImageDrawable(Drawable.createFromPath(picturePath));
                    } else {
                        patientPicture.setImageResource(R.drawable.ic_default_picture);
                    }
                    return true;
                }

                return false;
            }
        });

        return adapter;
    }

    //<editor-fold desc="action bar management">
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.addpatientmenu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Boolean handled = true;

        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.action_addPatient:
                Intent newPatientIntent = new Intent(getActivity(), AddPatientActivity.class);
                startActivity(newPatientIntent);
                break;
            default:
                handled = super.onOptionsItemSelected(item);
                break;
        }

        return handled;
    }
    //</editor-fold>

    //<editor-fold desc="Save/Restore instance">
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }
    //</editor-fold>
}
