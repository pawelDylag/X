package com.hacktory.x.send;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.hacktory.x.Constants;
import com.hacktory.x.R;
import com.hacktory.x.data.Message;
import com.parse.ParseObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SendFragment extends Fragment {

    @Bind(R.id.fragment_send_input)
    EditText editText;

    @Bind(R.id.fragment_send_button)
    ImageView sendButton;


    public static SendFragment newInstance() {
        return new SendFragment();
    }

    public SendFragment() {
    }

    @OnClick(R.id.fragment_send_button)
    public void sendMessage(){
        Log.d("SENDMESSAGE", "send");
        Message message = new Message(editText.getText().toString(), System.currentTimeMillis(), false);
        ParseObject parseObject = new ParseObject("Messages");
        parseObject.put("message", message.getMessage());
        parseObject.put("deviceId", String.valueOf(Constants.APP_ID));
        parseObject.saveInBackground();
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
