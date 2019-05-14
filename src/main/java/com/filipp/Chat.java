package com.filipp;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Chat extends Application {

    private boolean isServer = false;
    private final TextArea messages = new TextArea();
    private NetworkNode connection = isServer ? createServer() : createClient();

    private Parent createContent() {
        messages.setPrefHeight(550);
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

    public void start(Stage primaryStage) throws Exception {
        primaryStage.setScene(new Scene(createContent()));
        primaryStage.show();
    }

    public void stop() {
        try {
            connection.closeConnection();
        } catch (Exception e) {
            log.error("Error while closing connection", e);
        }
    }

    @Override
    public void init() throws Exception {
        connection.startConnection();
    }

    private Server createServer() {
        return new Server(55555, data -> Platform.runLater(() -> {
            messages.appendText(data.toString() + "\n");
        }));
    }

    private Client createClient() {
        return new Client("127.0.0.1", 55555, data -> Platform.runLater(() -> {
            messages.appendText(data.toString() + "\n");
        }));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
