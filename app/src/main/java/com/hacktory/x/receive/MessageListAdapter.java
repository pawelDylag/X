package com.hacktory.x.receive;

import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hacktory.x.Constants;
import com.hacktory.x.data.Message;
import com.hacktory.x.R;

import java.util.ArrayList;

/**
 * Adapter for GridView
 */
public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.ViewHolder> {

    ArrayList<Message> messages;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView, timeView;
        public CardView cardView;
        public ViewHolder(View v) {
            super(v);
            textView = (TextView) v.findViewById(R.id.message_card_text);
            timeView = (TextView) v.findViewById(R.id.message_card_time);
            cardView = (CardView) v.findViewById(R.id.message_card);
        }
    }

    public MessageListAdapter(ArrayList<Message> messages) {
        this.messages = Constants.messages;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message_card_view, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Message message = messages.get(position);
        if (message.isEnemyDetected()) holder.cardView.setCardBackgroundColor(Color.RED);
        holder.textView.setText(message.getMessage());
        holder.timeView.setText(message.getReadableTimestamp());
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public void addNewMessage(Message m) {
        this.messages.add(m);
        notifyDataSetChanged();
    }

    public void resetMessages() {
        this.messages = Constants.messages;
        notifyDataSetChanged();
    }
}