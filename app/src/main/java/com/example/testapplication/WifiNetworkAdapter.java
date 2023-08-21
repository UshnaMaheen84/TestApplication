package com.example.testapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class WifiNetworkAdapter extends RecyclerView.Adapter<WifiNetworkAdapter.ViewHolder> {

    private List<WifiNetwork> wifiNetworks;

    public WifiNetworkAdapter(List<WifiNetwork> wifiNetworks) {
        this.wifiNetworks = wifiNetworks;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_wifi_network, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WifiNetwork wifiNetwork = wifiNetworks.get(position);
        holder.ssidTextView.setText(wifiNetwork.getSsid());
        holder.onOffButton.setChecked(wifiNetwork.isEnabled());

        holder.onOffButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            wifiNetwork.setEnabled(isChecked);
            // Implement Wi-Fi network enable/disable logic here
        });
    }

    @Override
    public int getItemCount() {
        return wifiNetworks.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView ssidTextView;
        Switch onOffButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ssidTextView = itemView.findViewById(R.id.ssidTextView);
            onOffButton = itemView.findViewById(R.id.onOffButton);
        }
    }
}

