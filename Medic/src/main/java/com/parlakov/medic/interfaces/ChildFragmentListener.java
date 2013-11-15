package com.parlakov.medic.interfaces;

/**
 * Created by georgi on 13-11-15.
 */
public interface ChildFragmentListener {

    void onChildFragmentClose();

    void showErrorMessageAndExit(int resourceId);
}
