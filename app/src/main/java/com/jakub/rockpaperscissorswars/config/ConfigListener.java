package com.jakub.rockpaperscissorswars.config;


public interface ConfigListener {
    void onConfigReady();
    void onConfigFail();
}
