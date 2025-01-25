package com.github.thebloodyamateur.eventmanager;

import com.github.thebloodyamateur.eventmanager.filehandlers.ConfigHandler;
import com.github.thebloodyamateur.eventmanager.filehandlers.LogHandler;

public class App {
    public static void main(String[] args) {
        ConfigHandler configHandler = new ConfigHandler();
        LogHandler logHandler = new LogHandler(configHandler);
    }
}