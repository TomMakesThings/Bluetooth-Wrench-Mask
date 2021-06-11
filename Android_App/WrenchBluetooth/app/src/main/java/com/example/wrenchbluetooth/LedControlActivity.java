package com.example.wrenchbluetooth;

import android.graphics.Typeface;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Toast;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.AsyncTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;


public class LedControlActivity extends AppCompatActivity {
    private Button titleButton, disconnectButton, sendButton, ledSendButton, ledClearButton, ledInvertButton;
    private ImageButton leftButton, rightButton;
    private ImageView background, loadingBackground, loadingImage;
    private TextInputEditText textInput;
    private LinearLayout extraFeatures, extraFeatures2, recyclerView;
    private RecyclerView buttonRecyclerView;
    private GridView gridView;
    private GridViewAdapter gridViewAdapter;
    private SeekBar speedSlider, brightnessSlider;
    private String address;
    private ProgressDialog progress;
    private BluetoothAdapter adapter = null;
    private BluetoothSocket bluetoothSocket = null;
    private static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private int currentPage = 2;

    private final Integer[] buttonImages = {
            R.drawable.eyes_x, R.drawable.eyes_caret, R.drawable.eyes_wink, R.drawable.eyes_blink,
            R.drawable.eyes_question, R.drawable.eyes_exclamation, R.drawable.eyes_hashtag, R.drawable.eyes_dollar,
            R.drawable.eyes_bracket, R.drawable.eyes_negation, R.drawable.eyes_equal, R.drawable.eyes_z,
            R.drawable.eyes_n, R.drawable.eyes_u, R.drawable.eyes_inward_slash, R.drawable.eyes_outward_slash,
            R.drawable.eyes_heart, R.drawable.eyes_at, R.drawable.eyes_quote, R.drawable.eyes_apostrophe,
            R.drawable.eyes_ellipsis, R.drawable.eyes_semicolon, R.drawable.eyes_star, R.drawable.eyes_t,
            R.drawable.eyes_round, R.drawable.eyes_round_uneven, R.drawable.eyes_round_full_2, R.drawable.eyes_round_full_3,
            R.drawable.eyes_round_full, R.drawable.eyes_square, R.drawable.eyes_flashing_equare_2, R.drawable.eyes_glitch,
            R.drawable.eyes_9, R.drawable.eyes_music, R.drawable.eyes_plus, R.drawable.eyes_tick,
            R.drawable.eyes_rectangle, R.drawable.eyes_lenny, R.drawable.eyes_female, R.drawable.eyes_male,
            R.drawable.eyes_left, R.drawable.eyes_right, R.drawable.eyes_refresh, R.drawable.eyes_charge_3,
            R.drawable.eyes_pacman, R.drawable.eyes_zodiac_1, R.drawable.eyes_hashtag, R.drawable.eyes_random,
    };

    private final int[] buttonKeyValues = {
            1, 2, 3, 6,
            11, 10, 5, 15,
            19, 12, 4, 9,
            37, 29, 45, 46,
            16, 7, 24, 25,
            14, 8, 20, 21,
            17, 18, 47, 43,
            36, 38, 42, 44,
            13, 23, 27, 34,
            26, 22, 40, 41,
            30, 31, 32, 33,
            35, 39, 28, 0
    };

    private int customLEDRows[][] = {
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
    };

    private final String[] quotes = {
            "Hacking the mainframe... ",
            "Watch out for snakes...",
            "Want me to sing you a hacking song?",
            "Human Stupidity, that's why Hackers always win.",
            "It's time to wake up.",
            "Your digital shadow is already compromised.",
            "HOLY LULZ! IT'S DEDSEC!!"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.led_control_activity);
        getSupportActionBar().hide();

        // Gets MAC address from intent
        address = getIntent().getStringExtra("MAC_ADDRESS");

        titleButton = findViewById(R.id.buttonTitle);
        disconnectButton = findViewById(R.id.buttonDisconnect);
        sendButton = findViewById(R.id.sendButton);
        ledSendButton = findViewById(R.id.ledSendButton);
        ledClearButton = findViewById(R.id.ledClearButton);
        ledInvertButton = findViewById(R.id.ledInvertButton);
        leftButton = findViewById(R.id.leftButton);
        rightButton = findViewById(R.id.rightButton);
        background = findViewById(R.id.ledControlBackground);
        loadingBackground = findViewById(R.id.loadingBackground);
        loadingImage = findViewById(R.id.LoadingImage);
        textInput = findViewById(R.id.textInput);
        extraFeatures = findViewById(R.id.extraFeatures);
        extraFeatures2 = findViewById(R.id.extraFeatures2);
        recyclerView = findViewById(R.id.recyclerView);
        gridView = findViewById(R.id.gridView);
        buttonRecyclerView = findViewById(R.id.buttonRecyclerView);
        speedSlider = findViewById(R.id.speedSlider);
        brightnessSlider = findViewById(R.id.brightnessSlider);

        loadingBackground.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        extraFeatures.setVisibility(View.GONE);
        extraFeatures2.setVisibility(View.GONE);
        leftButton.setVisibility(View.VISIBLE);
        rightButton.setVisibility(View.VISIBLE);
        Typeface eightBitTypeFace = Typeface.createFromAsset(getAssets(), "font/eight_bit.ttf");
        disconnectButton.setTypeface(eightBitTypeFace);
        titleButton.setTypeface(eightBitTypeFace);
        sendButton.setTypeface(eightBitTypeFace);
        ledSendButton.setTypeface(eightBitTypeFace);
        ledClearButton.setTypeface(eightBitTypeFace);
        ledInvertButton.setTypeface(eightBitTypeFace);



        // Seek bar sends data when value is changed
        speedSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar arg0) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar arg0) {
            }

            @Override
            public void onProgressChanged(SeekBar arg0, int progress, boolean arg2) {
                sendData("speed" + String.valueOf(125 - (speedSlider.getProgress()*25)));
            }
        });

        brightnessSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar arg0) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar arg0) {
            }

            @Override
            public void onProgressChanged(SeekBar arg0, int progress, boolean arg2) {
                sendData("bright" + String.valueOf(brightnessSlider.getProgress()));
            }
        });

        new ConnectBluetooth().execute();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(getApplicationContext(), "Connection lost", Toast.LENGTH_LONG).show();
        disconnect();
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

    private ArrayList<ButtonImage> setButtons() {
        ArrayList<ButtonImage> buttons = new ArrayList<>();
        for (int i = 0; i < buttonImages.length; i++) {
            ButtonImage buttonImage = new ButtonImage("key" + buttonKeyValues[i], buttonImages[i]);
            buttons.add(buttonImage);
        }
        return buttons;
    }

    private void setRecyclerView() {
        buttonRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),4);
        buttonRecyclerView.setLayoutManager(layoutManager);

        ArrayList<ButtonImage> buttons = setButtons();
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(buttons, getApplicationContext());
        buttonRecyclerView.setAdapter(adapter);
    }

    private void setGridView() {
        gridViewAdapter = new GridViewAdapter(getApplicationContext());
        gridView.setAdapter(gridViewAdapter);
    }

    public void key (View view) {
        String key = view.getContentDescription().toString();
        if (key.equals("key0")) {
            int random = new Random().nextInt(47);
            key = "key" + random;
        }
        sendData(key);
    }

    public void customLED (View view) {
        String contents = view.getContentDescription().toString();
        int x = Character.getNumericValue(contents.charAt(1));
        int y = Character.getNumericValue(contents.charAt(2));
        if (contents.charAt(0) == '0') {
            view.setContentDescription("1" + contents.substring(1));
            view.setBackgroundResource(R.color.white);
            customLEDRows[x][y] = 1;
        }
        else if (contents.charAt(0) == '1') {
            view.setContentDescription("0" + contents.substring(1));
            view.setBackgroundResource(R.color.black);
            customLEDRows[x][y] = 0;
        }
    }

    public void sendCustom (View view) {
        customLEDRunnable runnable = new customLEDRunnable(customLEDRows);
        Thread thread = new Thread(runnable);
        thread.start();
        boolean waiting = true;
        while (waiting) {
            if (!thread.isAlive()) {
                waiting = false;
                sendData("led" + runnable.getData());
            }
        }
    }

    public void sendMessage (View view) {
        if (textInput.getText().length() != 0) {
            String text = textInput.getText().toString();
            sendData("message" + text);
        }
        else {
            Toast.makeText(getApplicationContext(), "Enter a message", Toast.LENGTH_SHORT).show();
        }
    }

    public void sendData(String data) {
        String message = "(" + data + ")";
        if (bluetoothSocket != null) {
            try {
                bluetoothSocket.getOutputStream().write(message.getBytes());
                Toast.makeText(this, "Sent message", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Connection error", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                this.startActivity(intent);
            }
        }
        else {
            Toast.makeText(this, "No bluetooth socket", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            this.startActivity(intent);
        }
    }

    public void buttonDisconnect (View view) {
        disconnect();
        Intent intent = new Intent(LedControlActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void buttonClear(View view) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                customLEDRows[i][j] = 0;
            }
        }
        for (Button b : gridViewAdapter.getButtons()) {
            b.setContentDescription("0" + b.getContentDescription().toString().substring(1));
            b.setBackgroundResource(R.color.black);
        }
    }

    public void buttonInvert(View view) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (customLEDRows[i][j] == 0) {
                    customLEDRows[i][j] = 1;
                }
                else {
                    customLEDRows[i][j] = 0;
                }
            }
        }
        for (Button b : gridViewAdapter.getButtons()) {
            if (b.getContentDescription().toString().charAt(0) == '0') {
                b.setContentDescription("1" + b.getContentDescription().toString().substring(1));
                b.setBackgroundResource(R.color.white);
            }
            else {
                b.setContentDescription("0" + b.getContentDescription().toString().substring(1));
                b.setBackgroundResource(R.color.black);
            }
        }
    }

    public void buttonLeftArrow (View view) {
        if (currentPage == 2) {
            currentPage = 1;
            recyclerView.setVisibility(View.GONE);
            extraFeatures2.setVisibility(View.VISIBLE);
            leftButton.setVisibility(View.GONE);
        }
        else if (currentPage == 3) {
            currentPage = 2;
            recyclerView.setVisibility(View.VISIBLE);
            extraFeatures.setVisibility(View.GONE);
            rightButton.setVisibility(View.VISIBLE);
        }
    }

    public void buttonRightArrow (View view) {
        if (currentPage == 1) {
            currentPage = 2;
            recyclerView.setVisibility(View.VISIBLE);
            extraFeatures2.setVisibility(View.GONE);
            leftButton.setVisibility(View.VISIBLE);
        }
        else if (currentPage == 2) {
            currentPage = 3;
            recyclerView.setVisibility(View.GONE);
            extraFeatures.setVisibility(View.VISIBLE);
            rightButton.setVisibility(View.GONE);
            leftButton.setVisibility(View.VISIBLE);
        }
    }

    public void disconnect() {
        if (bluetoothSocket != null) {
            try {
                bluetoothSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private class ConnectBluetooth extends AsyncTask<Void, Void, Void>
    {
        private boolean connectionFound = true;
        private int random;

        @Override
        protected void onPreExecute(){
            // Displays a progress dialog
            int random = new Random().nextInt(quotes.length-1);
            progress = ProgressDialog.show(LedControlActivity.this, "Connecting to bluetooth device", quotes[random]);
            loadingImage.setVisibility(View.VISIBLE);
            setLoadingBackground();
        }

        @Override
        protected Void doInBackground(Void... devices) {
            try {
                if (bluetoothSocket == null) {
                    adapter = BluetoothAdapter.getDefaultAdapter();
                    BluetoothDevice bluetoothDevice = adapter.getRemoteDevice(address);
                    bluetoothSocket = bluetoothDevice.createInsecureRfcommSocketToServiceRecord(myUUID);
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    bluetoothSocket.connect();
                }
            }
            catch (IOException e) {
                connectionFound = false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {
            super.onPostExecute(result);

            if (!connectionFound)
            {
                Toast.makeText(getApplicationContext(), "Connection failed", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LedControlActivity.this, MainActivity.class);
                startActivity(intent);
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Bluetooth connected", Toast.LENGTH_LONG).show();
                recyclerView.setVisibility(View.VISIBLE);
                setBackground();
                setRecyclerView();
                setGridView();
            }
            try {
                progress.dismiss();

            }
            catch (NullPointerException npe) {}
            loadingImage.setVisibility(View.GONE);
            loadingBackground.setVisibility(View.GONE);
        }

        private void setLoadingBackground() {
            random = new Random().nextInt(2);
            switch (random) {
                case 0:
                    loadingBackground.setImageResource(R.drawable.loading_background_reaper);
                    break;
                case 1:
                    loadingBackground.setImageResource(R.drawable.loading_background_skull);
                    break;
            }
            random = new Random().nextInt(2);
            switch (random) {
                case 0:
                    loadingImage.setImageResource(R.drawable.dedsec_guy);
                    break;
                case 1:
                    loadingImage.setImageResource(R.drawable.dedsec_woman);
                    break;
            }
        }


    }


}
