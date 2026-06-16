package com.github.eventmanager.outputs;

import com.github.eventmanager.filehandlers.LogHandler;
import com.github.eventmanager.filehandlers.config.SocketEntry;
import com.github.eventmanager.internal.InternalEventLogger;

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
    public void write(InternalEventLogger internalEventLogger, String event) {
        if (!batch.tryAdd(event)) {
            int bytes = batch.getCurrentSizeInBytes();
            int size = batch.getBatch().size();
            internalEventLogger.logInfo("Sending " + size + " events to socket. Total size: " + bytes + " bytes.");
            sendToSocket(internalEventLogger, String.join("\n", batch.getBatch()));
            batch.clearBatch();
            internalEventLogger.logDebug("Cleared batch");
            batch.tryAdd(event);
            return;
        }
        internalEventLogger.logDebug("Current batch size: " + batch.getCurrentSizeInBytes() +
                " bytes. Batch size: " + batch.getBatch().size());
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

    private void sendToSocket(InternalEventLogger internalEventLogger, String event) {
        for (SocketEntry socketEntry : socketSettings) {
            internalEventLogger.logDebug("Sending " + event.length() + " bytes to socket "
                    + socketEntry.getHost() + ":" + socketEntry.getPort());
            try {
                Socket socket = new Socket(socketEntry.getHost(), socketEntry.getPort());
                socket.getOutputStream().write(event.getBytes());
                socket.close();
                internalEventLogger.logDebug("Sent " + event.length() + " bytes to socket "
                        + socketEntry.getHost() + ":" + socketEntry.getPort());
            } catch (Exception e) {
                internalEventLogger.logError("An error occurred in sendToSocket:" + e.getMessage());
            }
        }
    }
}
