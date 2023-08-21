package com.example.testapplication;

public class WifiNetwork {
    private String ssid;
    private boolean isEnabled;

    public WifiNetwork(String ssid, boolean isEnabled) {
        this.ssid = ssid;
        this.isEnabled = isEnabled;
    }

    public String getSsid() {
        return ssid;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }
}
