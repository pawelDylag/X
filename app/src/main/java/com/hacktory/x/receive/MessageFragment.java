package com.hacktory.x.receive;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.CoordinatorLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.hacktory.x.R;

public class MessageFragment extends Fragment {

    /**
     * Board view object
     */
    private GridView boardView;

    /**
     * Coordinator layout for displaying messages on screen
     */
    private CoordinatorLayout coordinatorLayout;

    /**
     * Current activity context
     */
    private Context context;

    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private ListAdapter mAdapter;

    public static MessageFragment newInstance(String param1, String param2) {
        MessageFragment fragment = new MessageFragment();
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MessageFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void initBoardView() {
        // set adapter for grid view
        boardView.setAdapter(boardAdapter);
        // set grid view params
        boardView.setNumColumns(boardSize);
        boardView.setColumnWidth(BOARD_CELL_SIZE_DP);
        // initial animation
        Animation cellAnimation = AnimationUtils.loadAnimation(context, R.anim.board_cell_appear);
        GridLayoutAnimationController animController = new GridLayoutAnimationController(cellAnimation);
        animController.setDirectionPriority(GridLayoutAnimationController.PRIORITY_NONE);
        animController.setColumnDelay(0.6f / boardSize);
        animController.setRowDelay(0.6f / boardSize);
        boardView.setLayoutAnimation(animController);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);



        return view;
    }


}
