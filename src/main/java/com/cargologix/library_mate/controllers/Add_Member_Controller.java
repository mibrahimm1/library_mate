package com.cargologix.library_mate.controllers;

import com.cargologix.library_mate.database.db;
import javafx.fxml.Initializable;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Add_Member_Controller implements Initializable {
    @FXML
    private VBox add_member_Form;
    @FXML
    private Button cancelBtn;
    @FXML
    private TextField email;
    @FXML
    private TextField id;
    @FXML
    private AnchorPane mainContainer;
    @FXML
    private TextField name;
    @FXML
    private TextField phoneNum;
    @FXML
    private StackPane rootPane;
    @FXML
    private Button saveBtn;
    @FXML
    private TextField username;
    @FXML
    private TextField password;

    db databaseHandler ;

    public void addMember(ActionEvent event) {
        String Mid = id.getText();
        String Mname = name.getText();
        String MphoneNum = phoneNum.getText();
        String Memail = email.getText();
        String userN = username.getText();
        String pWord = password.getText();
        if (Mid.isEmpty() || Mname.isEmpty() || MphoneNum.isEmpty() || Memail.isEmpty() || userN.isEmpty() || pWord.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please Enter in all fields");
            alert.showAndWait();
            return;
        } else {
            String qu = "SELECT * FROM ACCOUNT WHERE username = '" + userN + "'";
            ResultSet rs = databaseHandler.executeQuery(qu);
            try {
                if (rs.next()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Attention");
                    alert.setHeaderText("Username already exists.");
                    alert.showAndWait();

                } else {
                    String action1 = "INSERT INTO MEMBER VALUES (" +
                            "'" + Mid + "'," +
                            "'" + Mname + "'," +
                            "'" + MphoneNum + "'," +
                            "'" + Memail + "'" +
                            ")";
                    String action = "INSERT INTO ACCOUNT(username,password,id) VALUES ("
                            + "'" + userN + "',"
                            + "'" + pWord + "',"
                            + "'" + Mid + "')";
                    if (databaseHandler.executeAction(action1) && databaseHandler.executeAction(action)) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setHeaderText(null);
                        alert.setContentText("Member Added Successfully!");
                        alert.showAndWait();
                        Stage stage = (Stage) rootPane.getScene().getWindow();
                        stage.close();

                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setHeaderText(null);
                        alert.setContentText("Failed to Add Member!");
                        alert.showAndWait();

                    }
                }
            } catch (SQLException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Attention");
                alert.setHeaderText("Failed to Add Member!.");
                alert.showAndWait();
            }
        }
    }


    public void cancel(ActionEvent event) {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close() ;
    }


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        databaseHandler = db.getInstance() ;

    }
}
