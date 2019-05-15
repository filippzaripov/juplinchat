package com.filipp;

import lombok.Getter;

import java.io.Serializable;
import java.util.function.Consumer;

@Getter
public class Client extends NetworkNode {

    private String ip;
    private int port;

    Client(String ip, int port, Consumer<Serializable> onRecieveCallback) {
        super(onRecieveCallback);
        this.ip = ip;
        this.port = port;
    }

    @Override
    protected boolean isServer() {
        return false;
    }

}
