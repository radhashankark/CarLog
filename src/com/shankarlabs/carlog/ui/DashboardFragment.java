package com.shankarlabs.carlog.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.actionbarsherlock.app.SherlockFragment;
import com.shankarlabs.carlog.R;

public class DashboardFragment extends SherlockFragment {

    private boolean mDualPane;

    @Override
    public void onCreate(Bundle savedInstanceState) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.dashboard, container, false);
        return fragmentView;
    }

}
