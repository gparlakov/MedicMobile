package com.parlakov.medic.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import com.parlakov.medic.R;
import com.parlakov.medic.activities.AddPatientActivity;
import com.parlakov.medic.localdata.LocalData;
import com.parlakov.medic.models.Patient;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Created by georgi on 13-11-5.
 */
public class PatientsListFragment extends ListFragment {

    //<editor-fold desc="patientsList Property">
    private Collection<Patient> patientsList;

    public void setPatientsList(Collection<Patient> patientsList) {
        this.patientsList = patientsList;
    }

    public Collection<Patient> getPatientsList() {
        return patientsList;
    }
    //</editor-fold>

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        try{
            super.onViewCreated(view, savedInstanceState);

            setAdapter(view);

            setHasOptionsMenu(true);
        }
        catch(Exception e){
           Log.e("Exception:", e.getLocalizedMessage());
        }
    }

    private void setAdapter(View view) {
        Context context = view.getContext();
        LocalData data = new LocalData(view.getContext());
        setPatientsList(data.getPatients().getAll());

        List<String> patients = getPatientNames(getPatientsList());

        ListAdapter adapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_list_item_1, patients);

        this.setListAdapter(adapter);
    }

    private List<String> getPatientNames(Collection<Patient> patientsList) {
        List<String> patientsNamesList = new ArrayList<String>();

        for (Iterator<Patient> iterator = patientsList.iterator(); iterator.hasNext(); ) {
            Patient next = iterator.next();
            String name = next.getFirstName() + "" + next.getLastName();
            patientsNamesList.add(name);
        }

        return patientsNamesList;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.addpatientmenu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Boolean handled = true;

        int itemId = item.getItemId();
        switch (itemId){
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
}
