package com.hacktory.x;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tt.whorlviewlibrary.WhorlView;


public class MainFragment extends Fragment {
    private MainActivity parentactivity;
    private WhorlView progressBar;

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        return fragment;
    }

    public MainFragment() {
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
        View view = inflater.inflate(R.layout.fragment_range, container, false);
        progressBar = (WhorlView) view.findViewById(R.id.progressBarRanging);
        return view;
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
