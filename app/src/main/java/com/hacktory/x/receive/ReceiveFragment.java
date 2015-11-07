package com.hacktory.x.receive;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.hacktory.x.data.Message;
import com.hacktory.x.R;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ReceiveFragment extends Fragment {

    /**
     * Board view object
     */
    @Bind(R.id.fragment_receive_recycler)
    public RecyclerView recyclerView;

    /**
     * Current activity context
     */
    private Context context;

    /**
     * Board view adapter
     */
    private MessageListAdapter messageListAdapter;

    public static ReceiveFragment newInstance() {
        ReceiveFragment fragment = new ReceiveFragment();
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ReceiveFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message_list, container, false);
        ButterKnife.bind(this, view);
        messageListAdapter = new MessageListAdapter(new ArrayList<Message>());
        recyclerView.setAdapter(messageListAdapter);
        messageListAdapter.addNewMessage(new Message("TEST", System.currentTimeMillis()));
        return view;
    }





}
