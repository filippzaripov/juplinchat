package com.filipp;

import lombok.extern.slf4j.Slf4j;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.function.Consumer;

@Slf4j
public abstract class NetworkNode {
    private ConnectionThread connectionThread = new ConnectionThread();
    private Consumer<Serializable> onRecieveCallback;

    NetworkNode(Consumer<Serializable> onRecieveCallback) {
        this.onRecieveCallback = onRecieveCallback;
        connectionThread.setDaemon(true);
    }


    void startConnection() {
        connectionThread.start();
    }

    void send(Serializable data) throws Exception {
        connectionThread.out.writeObject(data);
    }


    void closeConnection() throws Exception {
        connectionThread.socket.close();
    }

    protected abstract boolean isServer();

    protected abstract String getIp();

    protected abstract int getPort();

    private class ConnectionThread extends Thread {
        private Socket socket;
        private ObjectOutputStream out;

        @Override
        public void run() {
            try (ServerSocket server = isServer() ? new ServerSocket(getPort()) : null;
                 Socket socket = isServer() ? server.accept() : new Socket(getIp(), getPort());
                 ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                 ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

                this.socket = socket;
                this.out = out;
                socket.setTcpNoDelay(true);

                while (true) {
                    Serializable data = (Serializable) in.readObject();
                    onRecieveCallback.accept(data);
                }


            } catch (Exception ex) {
                log.error("Error while running Server or Client", ex);
                onRecieveCallback.accept("Connection closed");
            }
        }
    }
}
