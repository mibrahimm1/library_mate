package com.cargologix.library_mate.extra;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Add_Book_Loader extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Add_Book_Loader.class.getResource("add_book.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 330);
        stage.setTitle("LibraryMate");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}