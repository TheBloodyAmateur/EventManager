package com.github.eventmanager.outputs;

import com.github.eventmanager.InternalEventManager;
import com.github.eventmanager.filehandlers.LogHandler;
import com.github.eventmanager.filehandlers.config.SocketEntry;

import java.net.Socket;
import java.util.List;

public class SocketOutput implements Output {
    private List<SocketEntry> socketSettings;
    private final Batch batch = new Batch(65536);

    public SocketOutput(List<SocketEntry> socketSettings) {
        this.socketSettings = socketSettings;
    }

    @Override
    public void write(LogHandler loghandler, String event) {
        if(!batch.tryAdd(event)) {
            sendToSocket(String.join("\n", batch.getBatch()));
            batch.clearBatch();
            batch.tryAdd(event);
        }
    }

    @Override
    public void write(InternalEventManager internalEventManager, String event) {
        if (!batch.tryAdd(event)) {
            int bytes = batch.getCurrentSizeInBytes();
            int size = batch.getBatch().size();
            internalEventManager.logInfo("Sending " + size + " events to socket. Total size: " + bytes + " bytes.");
            sendToSocket(internalEventManager, String.join("\n", batch.getBatch()));
            batch.clearBatch();
            batch.tryAdd(event);
        }
    }

    private void sendToSocket(String event) {
        for (SocketEntry socketEntry : socketSettings) {
            try {
                Socket socket = new Socket(socketEntry.getHost(), socketEntry.getPort());
                socket.getOutputStream().write(event.getBytes());
                socket.close();
            } catch (Exception e) {
                System.out.println("An error occurred in sendToSocket:" + e.getMessage());
            }
        }
    }

    private void sendToSocket(InternalEventManager internalEventManager, String event) {
        for (SocketEntry socketEntry : socketSettings) {
            try {
                Socket socket = new Socket(socketEntry.getHost(), socketEntry.getPort());
                socket.getOutputStream().write(event.getBytes());
                socket.close();
            } catch (Exception e) {
                internalEventManager.logError("An error occurred in sendToSocket:" + e.getMessage());
            }
        }
    }
}
