package com.example.wrenchbluetooth;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;

class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private ArrayList<ButtonImage> buttons;
    private Context context;

    public RecyclerViewAdapter(ArrayList<ButtonImage> buttons, Context context) {
        super();
        this.buttons = buttons;
        this.context = context;
    }

    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.button_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapter.ViewHolder viewHolder, int i) {
        viewHolder.button.setBackgroundResource(buttons.get(i).getImage());
        viewHolder.button.setContentDescription(buttons.get(i).getKey());
    }

    @Override
    public int getItemCount() {
        return buttons.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private Button button;
        public ViewHolder(View view) {
            super(view);
            button = view.findViewById(R.id.button);
        }
    }

}
