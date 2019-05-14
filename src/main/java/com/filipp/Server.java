package com.filipp;

import java.io.Serializable;
import java.util.function.Consumer;

public class Server extends NetworkNode {

    private int port;

    Server(int port, Consumer<Serializable> onRecieveCallback) {
        super(onRecieveCallback);
        this.port = port;
    }

    @Override
    protected boolean isServer() {
        return true;
    }

    @Override
    protected String getIp() {
        return null;
    }

    @Override
    protected int getPort() {
        return port;
    }
}
