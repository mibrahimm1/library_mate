package com.cargologix.library_mate.extra;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Issue_List_Loader extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Book_List_Loader.class.getResource("issue_list.fxml"));
        Scene scene = new Scene(fxmlLoader.load()) ;
        stage.setScene(scene);
        stage.setTitle("View Issues List");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}