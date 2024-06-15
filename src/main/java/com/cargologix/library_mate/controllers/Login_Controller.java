package com.cargologix.library_mate.controllers;
import com.cargologix.library_mate.database.db;
import com.cargologix.library_mate.controllers.Main_Controller.WindowLoader ;
import com.cargologix.library_mate.misc.MemberInfo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

public class Login_Controller implements Initializable {

    @FXML
    private StackPane loginMain;

    @FXML
    private PasswordField confirmPassR;

    @FXML
    private TextField memberidR;

    @FXML
    private TextField nameR;

    @FXML
    private TextField phonenumR;

    @FXML
    private TextField emailR;

    @FXML
    private AnchorPane loginView;

    @FXML
    private PasswordField passwordL;

    @FXML
    private PasswordField passwordR;

    @FXML
    private AnchorPane registerView;

    @FXML
    private TextField usernameL;

    @FXML
    private TextField usernameR;

    db databaseHandler;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    private void switchToLogin(ActionEvent event) {
        registerView.setVisible(false);
        loginView.setVisible(true);
    }

    @FXML
    private void switchToRegister(ActionEvent event) {
        registerView.setVisible(true);
        loginView.setVisible(false);

    }

    @FXML
    private void cancel(ActionEvent event) {
        System.exit(0);

    }

    @FXML
    private void login(ActionEvent event) {
        databaseHandler = db.getInstance() ;
        String uName = usernameL.getText();
        String pWord = passwordL.getText();

        String qu = "SELECT * FROM ACCOUNT WHERE username = '" + uName + "'" ;
        ResultSet rs = databaseHandler.executeQuery(qu) ;
        try {
            if (rs.next()) {
                try {
                    String passwordCheck = rs.getString("password");
                    if (Objects.equals(pWord, passwordCheck)) {
                        boolean isAdmin = rs.getBoolean("isAdmin") ;
                        if (isAdmin) {
                            loadAdminDashboard() ;
                        } else {
                            MemberInfo.setLogin(true);
                            MemberInfo.setMemberId(rs.getString("id"));
                            loadUserDashboard() ;
                        }
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR) ;
                        alert.setTitle("Attention");
                        alert.setHeaderText("Incorrect Password.");
                        alert.showAndWait();
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION) ;
                alert.setTitle("Attention");
                alert.setHeaderText("Please enter a valid username.");
                alert.showAndWait();
            }
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION) ;
            alert.setTitle("Attention");
            alert.setHeaderText("Please enter a valid username.");
            alert.showAndWait();
        }
    }

    @FXML
    private void register(ActionEvent event) {
        databaseHandler = db.getInstance() ;
        String uName = usernameR.getText() ;
        String pWord = passwordR.getText() ;
        String memberName = nameR.getText() ;
        String mID = memberidR.getText() ;
        String contact = phonenumR.getText() ;
        String email = emailR.getText();
        if (uName.isEmpty() || pWord.isEmpty() || memberName.isEmpty() || mID.isEmpty() || contact.isEmpty() || email.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR) ;
            alert.setHeaderText(null);
            alert.setContentText("Please Enter in all fields");
            alert.showAndWait();
        } else {
            String qu = "SELECT * FROM ACCOUNT WHERE username = '" + uName + "'" ;
            ResultSet rs = databaseHandler.executeQuery(qu) ;
            try {
                if (rs.next()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR) ;
                    alert.setTitle("Attention");
                    alert.setHeaderText("Username already exists.");
                    alert.showAndWait();

                } else {
                    if (Objects.equals(pWord,confirmPassR.getText())) {
                        String action2 = "INSERT INTO MEMBER(name,id,phonenumber,email) VALUES ("
                                + "'" + memberName + "',"
                                + "'" + mID + "',"
                                + "'" + contact + "',"
                                + "'" + email + "')" ;
                        String action = "INSERT INTO ACCOUNT(username,password,id) VALUES ("
                                + "'" + uName + "',"
                                + "'" + pWord + "',"
                                + "'" + mID + "')";
                        if (databaseHandler.executeAction(action2) && databaseHandler.executeAction(action)) {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION) ;
                            alert.setTitle("Attention");
                            alert.setHeaderText("User Registered Successfully!");
                            alert.showAndWait();
                            registerView.setVisible(false);
                            loginView.setVisible(true);
                        } else {
                            Alert alert = new Alert(Alert.AlertType.ERROR) ;
                            alert.setTitle("Attention");
                            alert.setHeaderText("Failed to register user.");
                            alert.showAndWait();
                        }
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR) ;
                        alert.setTitle("Attention");
                        alert.setHeaderText("Passwords do not match.");
                        alert.showAndWait();

                    }
                }
            } catch (SQLException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR) ;
                alert.setTitle("Attention");
                alert.setHeaderText("Failed to register user.");
                alert.showAndWait();
            }
        }
    }

    @FXML
    private void loadAdminDashboard() {
        Stage stage = (Stage) loginMain.getScene().getWindow();
        stage.close();
        WindowLoader windowLoader = new WindowLoader();
        windowLoader.loadWindow("main_window.fxml", "Admin Dashboard");

    }

    @FXML
    private void loadUserDashboard() {
        Stage stage = (Stage) loginMain.getScene().getWindow();
        stage.close();
        WindowLoader windowLoader = new WindowLoader();
        windowLoader.loadWindow("user_dashboard.fxml", "User Dashboard");

    }


}
