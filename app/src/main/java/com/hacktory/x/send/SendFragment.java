package com.hacktory.x.send;

import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hacktory.x.R;

import butterknife.ButterKnife;


public class SendFragment extends Fragment {

    public static SendFragment newInstance() {
        SendFragment fragment = new SendFragment();
        return fragment;
    }

    public SendFragment() {
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_send, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
