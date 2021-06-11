package com.example.wrenchbluetooth;

public class customLEDRunnable implements Runnable {
    private int[][] customLEDRows;
    String data = "";

    public customLEDRunnable(int[][] customLEDRows) {
        super();
        this.customLEDRows = customLEDRows;
    }

    @Override
    public void run() {
        StringBuffer buffer = new StringBuffer();
        StringBuffer bufferByte = new StringBuffer();
        for (int i=0; i < 8; i++) {
            for (int j=0; j < 8; j++) {
                // Adds each byte
                bufferByte.append(customLEDRows[j][i]);
            }
            String bits = bufferByte.toString();
            int base10 = Integer.parseInt(bits, 2);
            if (base10 < 100) {
                buffer.append('0');
            }
            if (base10 < 10) {
                buffer.append('0');
            }
            buffer.append(base10);
            bufferByte.delete(0, bufferByte.length());
        }
        this.data = buffer.toString();
    }

    public String getData() {
        return this.data;
    }
}
