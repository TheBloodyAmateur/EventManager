package com.github.eventmanager.filehandlers.config;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SocketEntry {
    String host;
    int port;

    public SocketEntry(String host, int port) {
        this.host = host;
        this.port = port;
    }
}
