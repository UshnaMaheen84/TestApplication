package com.example.testapplication;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private WifiManager wifiManager;
    private BluetoothAdapter bluetoothAdapter;
    private Button wifiToggleButton, bluetoothToggleButton;
    private RecyclerView deviceRecyclerView;
    private DeviceAdapter deviceAdapter;
    private List<String> wifiNetworks = new ArrayList<>();
    private List<String> bluetoothDevices = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        wifiToggleButton = findViewById(R.id.wifiToggleButton);
        bluetoothToggleButton = findViewById(R.id.bluetoothToggleButton);
        deviceRecyclerView = findViewById(R.id.deviceRecyclerView);

        wifiToggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleWiFi();
            }
        });

        bluetoothToggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleBluetooth();
            }
        });

        deviceAdapter = new DeviceAdapter(wifiNetworks, bluetoothDevices);
        deviceRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        deviceRecyclerView.setAdapter(deviceAdapter);
    }

    private void toggleWiFi() {
        if (wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(false);
            wifiNetworks.clear();
            deviceAdapter.notifyDataSetChanged();
        } else {
            wifiManager.setWifiEnabled(true);
            scanWifiNetworks();
        }
    }

    private void toggleBluetooth() {
        if (bluetoothAdapter.isEnabled()) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT)
                    != PackageManager.PERMISSION_GRANTED) {

                return;
            }
            bluetoothAdapter.disable();
            bluetoothDevices.clear();
            deviceAdapter.notifyDataSetChanged();
        } else {
            bluetoothAdapter.enable();
            scanBluetoothDevices();
        }
    }

    private void scanWifiNetworks() {

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

        }
        List<ScanResult> results = wifiManager.getScanResults();
        wifiNetworks.clear();
        for (ScanResult result : results) {
            wifiNetworks.add(result.SSID);
        }
        deviceAdapter.notifyDataSetChanged();
    }
    private void scanBluetoothDevices() {
        bluetoothDevices.clear();
        // Add code to scan for Bluetooth devices and update the list
        // This might involve using a BroadcastReceiver to listen for Bluetooth discovery results
        deviceAdapter.notifyDataSetChanged();
    }

}
