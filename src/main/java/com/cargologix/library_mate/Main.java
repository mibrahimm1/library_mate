package com.cargologix.library_mate;

import com.cargologix.library_mate.extra.Add_Book_Loader;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Add_Book_Loader.class.getResource("/com/cargologix/library_mate/login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 470, 648);
        stage.setTitle("LibraryMate");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
