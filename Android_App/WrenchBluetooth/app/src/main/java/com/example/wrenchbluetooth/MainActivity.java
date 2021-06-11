package com.example.wrenchbluetooth;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.Random;
import java.util.Set;
import java.util.ArrayList;

import android.widget.Toast;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.TextView;
import android.content.Intent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;



public class MainActivity extends AppCompatActivity {
    private Button searchButton, titleButton;
    private ListView deviceList;
    private ImageView background;
    private BluetoothAdapter bluetoothAdapter = null;
    private Set<BluetoothDevice> pairedDevices;
    private boolean bluetoothEnabled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        searchButton = findViewById(R.id.buttonSearch);
        titleButton = findViewById(R.id.buttonTitle);
        Typeface eightBitTypeFace = Typeface.createFromAsset(getAssets(), "font/eight_bit.ttf");
        searchButton.setTypeface(eightBitTypeFace);
        titleButton.setTypeface(eightBitTypeFace);

        deviceList = findViewById(R.id.listView);
        background = findViewById(R.id.mainBackground);
        setUpBluetooth();
        if (bluetoothEnabled) {
            setBackground();
            searchDevices(null);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        setBackground();
    }

    private void setBackground() {
        int random = new Random().nextInt(5);
        switch (random) {
            case 0:
                background.setImageResource(R.drawable.blue_portrait);
                break;
            case 1:
                background.setImageResource(R.drawable.green_portrait);
                break;
            case 2:
                background.setImageResource(R.drawable.pink_portrait);
                break;
            case 3:
                background.setImageResource(R.drawable.red_portrait);
                break;
            case 4:
                background.setImageResource(R.drawable.purple_portrait);
                break;
        }
    }

    private void setUpBluetooth() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        // Checks if device has bluetooth
        if (bluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(), "Device does not support bluetooth", Toast.LENGTH_SHORT).show();
        }
        else if (!bluetoothAdapter.isEnabled()) {
            // Sends request to turn on bluetooth
            Intent turnOnBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnOnBluetooth,1);
        }
        else {
            bluetoothEnabled = true;
        }
    }

    // Searches for bluetooth paired devices
    public void searchDevices(View view) {
        pairedDevices = bluetoothAdapter.getBondedDevices();
        ArrayList devicesFound = new ArrayList();
        if (pairedDevices.isEmpty()) {
            devicesFound.add("No paired devices" + "\n" + "To find a device go Settings > Bluetooth");
            Toast.makeText(getApplicationContext(), "No devices connected", Toast.LENGTH_SHORT).show();
        }
        else {
            for (BluetoothDevice device : pairedDevices){
                devicesFound.add("Name: " + device.getName() + "\nMAC Address: " + device.getAddress());
            }
            Toast.makeText(getApplicationContext(), devicesFound.size() + " devices found", Toast.LENGTH_SHORT).show();
        }
        // Sets up list view
        final ArrayAdapter adapter = new ArrayAdapter(this,R.layout.list_item, devicesFound);
        deviceList.setAdapter(adapter);
        // Adds a listener to the list view
        deviceList.setOnItemClickListener(listOnClickListener);
    }

    private AdapterView.OnItemClickListener listOnClickListener = new AdapterView.OnItemClickListener()
    {
        public void onItemClick(AdapterView adapterView, View view, int i, long l)
        {
            // Gets MAC address from the list view
            String text = ((TextView) view).getText().toString();
            String address = text.substring(text.length() - 17);
            // Makes an intent to open a new activity
            Intent intent = new Intent(MainActivity.this, LedControlActivity.class);
            //Change the activity
            intent.putExtra("MAC_ADDRESS", address);
            startActivity(intent);
        }
    };

}
