//package com.parlakov.medic.adapters;
//
//import android.content.Context;
//import android.database.Cursor;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseExpandableListAdapter;
//
///**
// * Created by georgi on 13-11-19.
// */
//public class ExaminationsAdapter extends BaseExpandableListAdapter {
//
//    private final Cursor mCursor;
//    private final Context mContext;
//
//    public ExaminationsAdapter(Context context,
//                               Cursor examinationsGroupedByDateCursor){
//
//        mCursor = examinationsGroupedByDateCursor;
//        mContext = context;
//    }
//
//    @Override
//    public int getGroupCount() {
//        return mCursor.getCount();
//    }
//
//    @Override
//    public int getChildrenCount(int groupPosition) {
//        return 0;
//    }
//
//    @Override
//    public Object getGroup(int groupPosition) {
//        mCursor.moveToFirst();
//        mCursor.move(groupPosition);
//
//        return mCursor.get;
//    }
//
//    @Override
//    public Object getChild(int groupPosition, int childPosition) {
//        return null;
//    }
//
//    @Override
//    public long getGroupId(int groupPosition) {
//        return 0;
//    }
//
//    @Override
//    public long getChildId(int groupPosition, int childPosition) {
//        return 0;
//    }
//
//    @Override
//    public boolean hasStableIds() {
//        return false;
//    }
//
//    @Override
//    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
//        return null;
//    }
//
//    @Override
//    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
//        return null;
//    }
//
//    @Override
//    public boolean isChildSelectable(int groupPosition, int childPosition) {
//        return false;
//    }
//}
