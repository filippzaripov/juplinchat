package com.filipp;

import javafx.scene.Parent;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import lombok.extern.slf4j.Slf4j;

@Slf4j
class View {

    Parent createContent(TextArea messages, NetworkNode connection, boolean isServer) {
        messages.setPrefHeight(550);
        messages.setEditable(false);

        TextField input = new TextField();
        input.setOnAction(event -> {
            String message = isServer ? "Server : " : "Client : ";
            message += input.getText();
            input.clear();
            messages.appendText(message + "\n");
            try {
                connection.send(message);
            } catch (Exception e) {
                messages.appendText("Failed to send : " + message + "\n");
                log.error("Failed to send : " + message, e);
            }
        });

        VBox root = new VBox(30, messages, input);
        root.setPrefSize(600, 600);
        return root;
    }
}
