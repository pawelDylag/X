package com.hacktory.x;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainFragment extends Fragment {
    private MainActivity parentactivity;
//    private WhorlView progressBar;

    @Bind(R.id.button_receive)
    public Button buttonReceive;

    private ImageView imageViewFirst, imageViewSecond, imageViewThird,
            imageViewFourth, imageViewFifth;

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
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        getViews(view);

        setAllImagesToColor(Constants.COLOR_GREY);
        ButterKnife.bind(this, view);
        return view;
    }

    private void getViews(View view) {
        
        imageViewFirst = (ImageView) view.findViewById(R.id.imageView_first);
        imageViewSecond = (ImageView) view.findViewById(R.id.imageView_second);
        imageViewThird = (ImageView) view.findViewById(R.id.imageView_third);
        imageViewFourth = (ImageView) view.findViewById(R.id.imageView_fourth);
        imageViewFifth = (ImageView) view.findViewById(R.id.imageView_fifth);

    }

    @OnClick(R.id.button_receive)
    public void switchToReceive() {
        ((MainActivity) getActivity()).setFragment(Constants.FRAGMENT_RECEIVE);
    }

    @OnClick(R.id.button_send)
    public void switchToSend() {
        ((MainActivity) getActivity()).setFragment(Constants.FRAGMENT_SEND);
    }

    public void setAllImagesToColor(int color){
        int image = 0;
        switch (color){
            case Constants.COLOR_GREY:
                image = R.drawable.circle_grey;
                break;
            case Constants.COLOR_LIGHT_GREEN:
                image = R.drawable.circle_greenl;
                break;
            case Constants.COLOR_DARK_GREEN:
                image = R.drawable.circle_greend;
                break;
        }

        imageViewFirst.setImageResource(image);
        imageViewSecond.setImageResource(image);
        imageViewThird.setImageResource(image);
        imageViewFourth.setImageResource(image);
        imageViewFifth.setImageResource(image);

    }

    public void setImageColor (int color, ImageView imageView){

        int image = 0;
        switch (color){
            case Constants.COLOR_GREY:
                image = R.drawable.circle_grey;
                break;
            case Constants.COLOR_LIGHT_GREEN:
                image = R.drawable.circle_greenl;
                break;
            case Constants.COLOR_DARK_GREEN:
                image = R.drawable.circle_greend;
                break;
        }
        
        imageView.setImageResource(image);
    }

    public void setImageColorWithLevel (int level){

        int image = R.drawable.circle_greenl;

        switch (level){
            case 0:
                imageViewFirst.setImageResource(image);
                break;
            case 1:
                imageViewFirst.setImageResource(image);
                imageViewSecond.setImageResource(image);
                break;
            case 2:
                imageViewFirst.setImageResource(image);
                imageViewSecond.setImageResource(image);
                imageViewThird.setImageResource(image);
                break;
            case 3:
                imageViewFirst.setImageResource(image);
                imageViewSecond.setImageResource(image);
                imageViewThird.setImageResource(image);
                imageViewFourth.setImageResource(image);
                break;
            case 4:
                imageViewFirst.setImageResource(image);
                imageViewSecond.setImageResource(image);
                imageViewThird.setImageResource(image);
                imageViewFourth.setImageResource(image);
                imageViewFifth.setImageResource(image);
                break;
        }
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
