package com.parlakov.medic.interfaces;

/**
 * Created by georgi on 13-11-15.
 */
public interface ChildFragmentListener {

    // when a child fragment that is not in the navigation drawer
    // is closed this enables the parent activity to react
    void onChildFragmentClose();

    void showErrorMessageAndExit(int resourceId);
}
