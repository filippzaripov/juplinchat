package com.filipp;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.function.Consumer;

public abstract class NetworkConnection {
    private ConnectionThread connectionThread = new ConnectionThread();
    private Consumer<Serializable> onRecieveCallback;

    public NetworkConnection(Consumer<Serializable> onRecieveCallback) {
        this.onRecieveCallback = onRecieveCallback;
        connectionThread.setDaemon(true);
    }


    public void startConnection() throws Exception {
        connectionThread.start();
    }

    public void send(Serializable data) throws Exception {
        connectionThread.out.writeObject(data);
    }

    public void closeConnection() throws Exception {
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
                onRecieveCallback.accept("Connection closed");

            }
        }
    }
}
