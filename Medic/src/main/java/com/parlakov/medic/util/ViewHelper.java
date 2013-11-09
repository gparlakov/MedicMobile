package com.parlakov.medic.util;

import android.app.Activity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by georgi on 13-11-6.
 */
public class ViewHelper {
    public static String getTextFromEditView(int id, View rootView){
        EditText view = (EditText) rootView.findViewById(id);
        String text = null;

        if (view != null){
            text = view.getText().toString();
        }

        return text;
    }

    public static String getTextFromTextView(int id, View rootView){
        TextView view = (TextView) rootView.findViewById(id);
        String text = null;

        if (view != null){
            text = view.getText().toString();
        }

        return text;
    }

    public static String getTextFromEditView(int id, Activity activity){
        EditText view = (EditText) activity.findViewById(id);
        String text = null;

        if (view != null){
            text = view.getText().toString();
        }

        return text;
    }

    public static void setTextToEditView(int id, Activity activity, String text){
        EditText view = (EditText) activity.findViewById(id);

        if (view != null){
            view.setText(text);
        }
    }

    public static void setTextToTextView(int id, View view, String text){
        TextView textView = (TextView) view.findViewById(id);

        if (textView != null){
            textView.setText(text);
        }
    }

}
