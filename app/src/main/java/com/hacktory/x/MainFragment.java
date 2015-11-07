package com.hacktory.x;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainFragment extends Fragment {
    private MainActivity parentactivity;
//    private WhorlView progressBar;

    @Bind(R.id.button_receive)
    public Button buttonReceive;

    @Bind(R.id.button_send)
    public Button buttonSend;

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
        View view =  inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);
        return view;
    }


    @OnClick(R.id.button_receive)
    public void switchToReceive() {
        ((MainActivity) getActivity()).setFragment(Constants.FRAGMENT_RECEIVE);
    }

    @OnClick(R.id.button_send)
    public void switchToSend() {
        ((MainActivity) getActivity()).setFragment(Constants.FRAGMENT_SEND);
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }
}
