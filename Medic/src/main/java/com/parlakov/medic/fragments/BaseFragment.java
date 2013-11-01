package com.parlakov.medic.fragments;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.EditText;

/**
 * Created by georgi on 13-11-1.
 */
public class BaseFragment extends Fragment {

    protected String getTextFromEditView(int id, View rootView){
        EditText view = (EditText) rootView.findViewById(id);
        String text = null;

        if (view != null){
            text = view.getText().toString();
        }

        return text;
    }
}
