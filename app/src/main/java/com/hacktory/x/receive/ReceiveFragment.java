package com.hacktory.x.receive;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.hacktory.x.Constants;
import com.hacktory.x.MainActivity;
import com.hacktory.x.data.Message;
import com.hacktory.x.R;
import com.hacktory.x.send.SendFragment;
import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReceiveFragment extends Fragment {

    /**
     * Board view object
     */
    @Bind(R.id.fragment_receive_recycler)
    public RecyclerView recyclerView;

    @Bind(R.id.fab)
    public FloatingActionButton fab;

    /**
     * Current activity context
     */
    private Context context;

    /**
     * Board view adapter
     */
    private MessageListAdapter messageListAdapter;

    /**
     * Recycler layout manager
     */
    private RecyclerView.LayoutManager layoutManager;

    private Timer timer;


    public static ReceiveFragment newInstance() {
        ReceiveFragment fragment = new ReceiveFragment();
        return fragment;
    }

    public ReceiveFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        messageListAdapter = new MessageListAdapter(new ArrayList<Message>());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message_list, container, false);
        ButterKnife.bind(this, view);
        recyclerView.setAdapter(messageListAdapter);
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        startReceivingData();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).setFragment(Constants.FRAGMENT_SEND);
            }
        });
        return view;
    }

    private void getMessages() {
        Log.d("GETMESSAGES", "start");
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Messages");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, com.parse.ParseException e) {
                if (e == null) {
                    Log.d("GETMESSAGES", "got new messages  : " + objects.size());
                    ArrayList<Message> messages = new ArrayList<>();
                    for (ParseObject o : objects) {
                        Message message = new Message(o.getString("message"), o.getCreatedAt().getTime(), false);
                        messages.add(message);
                    }
                    Collections.sort(messages);
                    Constants.messages = messages;
                    messageListAdapter.resetMessages();
                }
                else {
                    Log.d("GETMESSAGES", "error: " + e.getMessage());
                }
            }
        });
    }

    private void  startReceivingData() {
        int delay = 2000; //
        int period = 1000; // repeat every sec.
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {

            public void run() {
                getMessages();
            }

        }, delay, period);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }


}
