package com.example.wrenchbluetooth;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import java.util.ArrayList;

public class GridViewAdapter extends BaseAdapter {
    private Context context;
    ArrayList<Button> buttons = new ArrayList<>();

    public GridViewAdapter(Context context) {
        super();
        this.context = context;
    }

    public int getCount() {
        return 64;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View view, ViewGroup viewGroup) {
        View button;
        if (view == null) {
            button = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.led_layout, viewGroup, false);
            // Binary value, x coordinate, y coordinate
            this.buttons.add((Button)button.findViewById(R.id.ledButton));
            button.findViewById(R.id.ledButton).setContentDescription("0" + (position%8) + "" + (position/8));
        }
        else
        {
            button = view;
        }
        return button;
    }

    public ArrayList<Button> getButtons() {
        return this.buttons;
    }
}
