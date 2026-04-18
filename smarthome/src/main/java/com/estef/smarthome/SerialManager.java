package com.estef.smarthome;

import com.fazecast.jSerialComm.SerialPort;
import java.io.*;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class SerialManager {
    private SerialPort port;
    private BufferedReader in;
    private PrintWriter out;
    private Thread readerThread;

    public interface Listener {
        void onMessage(String msg);
    }

    private List<Listener> listeners = new CopyOnWriteArrayList<>();

    public void addListener(Listener l) {
        listeners.add(l);
    }

    public void removeListener(Listener l) {
        listeners.remove(l);
    }

    public boolean open(String portName, int baud) {
        port = SerialPort.getCommPort(portName);
        port.setBaudRate(baud);
        if (!port.openPort())
            return false;
        try {
            in = new BufferedReader(new InputStreamReader(port.getInputStream()));
            out = new PrintWriter(port.getOutputStream(), true);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        readerThread = new Thread(() -> {
            try {
                String line;
                while (!Thread.currentThread().isInterrupted() && (line = in.readLine()) != null) {
                    for (Listener l : listeners)
                        l.onMessage(line);
                }
            } catch (IOException e) {
                /* cerrado */ }
        }, "SerialReader");
        readerThread.start();
        return true;
    }

    public void send(String s) {
        if (out != null) {
            out.print(s + "\n");
            out.flush();
            System.out.println("SerialManager -> Sent: " + s);
        }
    }

    public void close() {
        try {
            if (readerThread != null)
                readerThread.interrupt();
        } catch (Exception ignore) {
        }
        if (port != null && port.isOpen())
            port.closePort();
    }

    public static String[] listPorts() {
        SerialPort[] ps = SerialPort.getCommPorts();
        String[] arr = new String[ps.length];
        for (int i = 0; i < ps.length; i++)
            arr[i] = ps[i].getSystemPortName();
        return arr;
    }
}
