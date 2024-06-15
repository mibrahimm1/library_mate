package com.cargologix.library_mate.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import com.cargologix.library_mate.database.db;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Add_Book_Controller implements Initializable {


    @FXML
    private VBox add_book_Form;

    @FXML
    private AnchorPane mainContainer;

    @FXML
    private StackPane rootPane;
    @FXML
    private TextField author;

    @FXML
    private Button cancelBtn;

    @FXML
    private TextField id;

    @FXML
    private TextField publisher;

    @FXML
    private Button saveBtn;

    @FXML
    private TextField title;

    db databaseHandler;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
           databaseHandler = db.getInstance() ;
    }

    @FXML
    private void addBook(ActionEvent event) {
        String Bid = id.getText() ;
        String Btitle = title.getText() ;
        String Bpublisher = publisher.getText() ;
        String Bauthor = author.getText() ;
        if (Bid.isEmpty() || Btitle.isEmpty() || Bpublisher.isEmpty() || Bauthor.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR) ;
            alert.setHeaderText(null);
            alert.setContentText("Please Enter in all fields");
            alert.showAndWait();
            return;
        }

        String qu = "INSERT INTO BOOK VALUES (" +
                "'" + Bid + "'," +
                "'" + Btitle + "'," +
                "'" + Bauthor + "'," +
                "'" + Bpublisher + "'," +
                "1" +
                ")";
        if (databaseHandler.executeAction(qu))
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION) ;
            alert.setHeaderText(null);
            alert.setContentText("Book Added Successfully!");
            alert.showAndWait();

        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR) ;
            alert.setHeaderText(null);
            alert.setContentText("Failed to Add Book!");
            alert.showAndWait();

        }

    }
    @FXML
    private void checkData() throws SQLException {
        String qu = "SELECT title FROM BOOK" ;
        ResultSet rs = databaseHandler.executeQuery(qu) ;
        try {
            while (rs.next()) {
                String titlex = rs.getString("title");
                System.out.println(titlex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Add_Book_Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @FXML
    private void cancel(ActionEvent event) {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close() ;

    }

}

