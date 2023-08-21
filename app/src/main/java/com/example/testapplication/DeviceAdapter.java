package com.example.testapplication;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.DeviceViewHolder> {

    private List<String> wifiNetworks;
    private List<String> bluetoothDevices;

    public DeviceAdapter(List<String> wifiNetworks, List<String> bluetoothDevices) {
        this.wifiNetworks = wifiNetworks;
        this.bluetoothDevices = bluetoothDevices;
    }

    @NonNull
    @Override
    public DeviceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_view, parent, false);
        return new DeviceViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceViewHolder holder, int position) {
        String deviceName = wifiNetworks.size() > position ? wifiNetworks.get(position) :
                bluetoothDevices.get(position - wifiNetworks.size());
        holder.deviceNameTextView.setText(deviceName);
    }

    @Override
    public int getItemCount() {
        return wifiNetworks.size() + bluetoothDevices.size();
    }

    static class DeviceViewHolder extends RecyclerView.ViewHolder {
        TextView deviceNameTextView;

        public DeviceViewHolder(@NonNull View itemView) {
            super(itemView);
            deviceNameTextView = itemView.findViewById(R.id.nameTextView);
        }
    }
}
