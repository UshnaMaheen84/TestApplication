package com.example.testapplication;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import android.Manifest;
import android.widget.ToggleButton;

import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_LOCATION_PERMISSION = 123;
    private static final int PERMISSION_REQUEST_BLUETOOTH = 1;

    private RecyclerView recyclerView, deviceRecyclerView;
    private WifiNetworkAdapter adapter;
    private List<WifiNetwork> wifiNetworks;


    private BluetoothAdapter bluetoothAdapter;
    private List<BluetoothDeviceModel> deviceList;
    private DeviceListAdapter deviceListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wifiNetworks = new ArrayList<>();
        deviceList = new ArrayList<>();

        recyclerView = findViewById(R.id.deviceRecyclerView);
        deviceRecyclerView = findViewById(R.id.deviceRecyclerView);

        ToggleButton toggle = (ToggleButton) findViewById(R.id.ToggleButton);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    startDeviceDiscovery();
                    scanWifiNetworks();
                } else {
                    // The toggle is disabled

                    wifiNetworks.clear();
                    deviceList.clear();
                    deviceListAdapter.notifyDataSetChanged();
                    adapter.notifyDataSetChanged();

                }
            }
        });
        deviceListAdapter = new DeviceListAdapter(deviceList);
        deviceRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        deviceRecyclerView.setAdapter(deviceListAdapter);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            // Device doesn't support Bluetooth
            // Handle this case
        } else if (!bluetoothAdapter.isEnabled()) {
            // Bluetooth is not enabled, request to enable
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 1);
        } else {
            startDeviceDiscovery();
        }
        adapter = new WifiNetworkAdapter(wifiNetworks);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE_LOCATION_PERMISSION);
        } else {
            scanWifiNetworks();
        }
    }

    private void startDeviceDiscovery() {

        Log.e("log checking","in startDeviceDiscovery");

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            Log.e("log checking","in startDeviceDiscovery checkSelfPermission");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {

                if ((this.checkSelfPermission(Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED)
                        || (this.checkSelfPermission(Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED)) {

                    Log.w(getClass().getName(), "requestBluetoothPermissions() BLUETOOTH_SCAN AND BLUETOOTH_CONNECT permissions needed => requesting them...");

                }
            }
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        bluetoothAdapter.startDiscovery();
        registerReceiver(deviceReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
    }

    private final BroadcastReceiver deviceReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.e("log checking","in BroadcastReceiver");

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device != null) {
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        Log.e("log checking","in BroadcastReceiver checkSelfPermission");

                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    String name = device.getName();
                    String address = device.getAddress();
                    BluetoothDeviceModel deviceModel = new BluetoothDeviceModel(name, address);
                    deviceList.add(deviceModel);
                    deviceListAdapter.notifyDataSetChanged();
                }
            }
        }
    };


    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                scanWifiNetworks();
                Log.e("log checking","in scanWifiNetworks PERMISSION_GRANTED");

            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }

        }
        if (requestCode == PERMISSION_REQUEST_BLUETOOTH) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.e("log checking","in Bluetooth PERMISSION_GRANTED");

                startDeviceDiscovery();
            } else {
                Toast.makeText(this, "Bluetooth permission denied", Toast.LENGTH_SHORT).show();

            }
        }
    }

    private void scanWifiNetworks() {
        Log.e("log checking","in scanWifiNetworks");

        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        if (wifiManager != null) {
            wifiManager.startScan();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                Log.e("log checking","in scanWifiNetworks checkSelfPermission");

                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            List<ScanResult> scanResults = wifiManager.getScanResults();


            for (ScanResult scanResult : scanResults) {
                String ssid = scanResult.SSID;
                boolean isEnabled = scanResult.capabilities.toUpperCase().contains("WPA") ||
                        scanResult.capabilities.toUpperCase().contains("WEP");

                wifiNetworks.add(new WifiNetwork(ssid, isEnabled));
            }

            adapter.notifyDataSetChanged();
        }
    }
}
