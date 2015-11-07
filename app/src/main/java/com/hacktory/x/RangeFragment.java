package com.hacktory.x;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class RangeFragment extends Fragment {
    private MainActivity parentactivity;

    public static RangeFragment newInstance() {
        RangeFragment fragment = new RangeFragment();
        return fragment;
    }

    public RangeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_range, container, false);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        parentactivity = (MainActivity) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }
}
