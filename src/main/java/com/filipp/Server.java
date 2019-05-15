package com.filipp;

import lombok.Getter;

import java.io.Serializable;
import java.util.function.Consumer;

@Getter
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

}
