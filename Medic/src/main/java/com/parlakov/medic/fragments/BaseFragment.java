package com.parlakov.medic.fragments;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by georgi on 13-11-1.
 */
public abstract class BaseFragment extends Fragment {

    protected String getTextFromEditView(int id, View rootView){
        EditText view = (EditText) rootView.findViewById(id);
        String text = null;

        if (view != null){
            text = view.getText().toString();
        }

        return text;
    }

    protected void showToastMessage(String message, int length, View view){
        Toast resultToast = Toast.makeText(view.getContext(), message, length);
        resultToast.show();
    }
}
