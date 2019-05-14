package com.filipp;

import java.io.Serializable;
import java.util.function.Consumer;

public class Client extends NetworkNode {

    private String ip;
    private int port;

    public Client(String ip, int port, Consumer<Serializable> onRecieveCallback) {
        super(onRecieveCallback);
        this.ip = ip;
        this.port = port;
    }

    @Override
    protected boolean isServer() {
        return false;
    }

    @Override
    protected String getIp() {
        return ip;
    }

    @Override
    protected int getPort() {
        return port;
    }
}
