package com.filipp;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ServerChat extends Application {

    private static final String type = "Server";
    private final TextArea messages = new TextArea();
    private NetworkNode connection = createServer();
    private View view = new View();

    public void start(Stage primaryStage) {
        primaryStage.setScene(new Scene(view.createContent(messages, connection, true)));
        primaryStage.setTitle(type);
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
    public void init() {
        connection.startConnection();
    }

    private Server createServer() {
        return new Server(55555, data -> Platform.runLater(() -> messages.appendText(data.toString() + "\n")));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
