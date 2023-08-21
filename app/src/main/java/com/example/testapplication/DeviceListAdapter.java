package com.example.testapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DeviceListAdapter extends RecyclerView.Adapter<DeviceListAdapter.ViewHolder> {
    private List<BluetoothDeviceModel> devices;

    public DeviceListAdapter(List<BluetoothDeviceModel> devices) {
        this.devices = devices;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.device_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BluetoothDeviceModel device = devices.get(position);
        holder.deviceNameTextView.setText(device.getName());
        holder.deviceAddressTextView.setText(device.getAddress());
    }

    @Override
    public int getItemCount() {
        return devices.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView deviceNameTextView;
        TextView deviceAddressTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            deviceNameTextView = itemView.findViewById(R.id.deviceNameTextView);
            deviceAddressTextView = itemView.findViewById(R.id.deviceAddressTextView);
        }
    }
}

