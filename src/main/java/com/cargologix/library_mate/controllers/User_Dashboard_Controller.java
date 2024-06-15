package com.cargologix.library_mate.controllers;
import com.cargologix.library_mate.database.db;

import com.cargologix.library_mate.misc.MemberInfo;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import com.jfoenix.effects.JFXDepthManager ;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.collections.ObservableList;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.cargologix.library_mate.database.db.connection;

public class User_Dashboard_Controller implements Initializable {

    @FXML
    private Label bookAuthor;

    @FXML
    private TextField bookIDin;

    @FXML
    private TextField bookIDinput;

    @FXML
    private Pane bookInfo;

    @FXML
    private Label bookName;

    @FXML
    private Label bookStatus;


    @FXML
    private StackPane rootPane;

    @FXML
    private ListView<String> issueDataList;


    db databaseHandler;

    String mName;
    String bName;

    String memberIDpassed;

    Boolean issueDetailsON = false;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        JFXDepthManager.setDepth(bookInfo, 1);
        this.memberIDpassed = MemberInfo.getMemberId();
        databaseHandler = db.getInstance();

    }


    @FXML
    private void loadMemberList(ActionEvent event) {
        WindowLoader windowLoader = new WindowLoader();
        windowLoader.loadWindow("member_list.fxml", "View Members");

    }

    @FXML
    private void loadBookList(ActionEvent event) {
        WindowLoader windowLoader = new WindowLoader();
        windowLoader.loadWindow("book_list.fxml", "View Books");

    }
    @FXML
    private void loadIssueList(ActionEvent event) {
        WindowLoader windowLoader = new WindowLoader();
        windowLoader.loadWindow("issue_list.fxml", "View Issues");

    }

    @FXML
    private void loadReturnList(ActionEvent event) {
        WindowLoader windowLoader = new WindowLoader();
        windowLoader.loadWindow("return_list.fxml", "View Issue Returns");

    }

    @FXML
    private void loadBookInfo(ActionEvent event) {
        String id = bookIDin.getText();
        String qu = "SELECT * FROM BOOK WHERE id = '" + id + "'";
        ResultSet rs = databaseHandler.executeQuery(qu);
        Boolean found = false;
        try {
            while (rs.next() & !found) {
                bName = rs.getString("title");
                String bAuthor = rs.getString("author");
                Boolean bStatus = rs.getBoolean("isAvail");
                String availability = (bStatus) ? "Available" : "Issued";
                bookName.setText("Book Name: " + bName);
                bookAuthor.setText("Author: " + bAuthor);
                bookStatus.setText("Status: " + availability);
                found = true;
            }
            if (!found) {
                bookName.setText("Book Name: " + "N/A");
                bookAuthor.setText("Author: " + "N/A");
                bookStatus.setText("Status: " + "N/A");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Main_Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    @FXML
    private void issueBook(ActionEvent event) {

        String memberID = memberIDpassed;
        String bookID = bookIDin.getText();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Issue");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to issue the book " + bName + "?");

        Optional<ButtonType> response = alert.showAndWait();
        if (response.get() == ButtonType.OK) {
            String str = "INSERT INTO ISSUE(memberID,bookID) VALUES ("
                    + "'" + memberID + "',"
                    + "'" + bookID + "')";
            String str2 = "UPDATE BOOK SET isAvail = '0' WHERE id = '" + bookID + "'";
            System.out.println(str + " and " + str2);
            if (databaseHandler.executeAction(str) && databaseHandler.executeAction(str2)) {
                Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                alert1.setTitle("Success");
                alert1.setHeaderText(null);
                alert1.setContentText("Book Issued Successfully!");
                alert1.showAndWait();
            } else {
                Alert alert2 = new Alert(Alert.AlertType.ERROR);
                alert2.setTitle("Failed");
                alert2.setHeaderText(null);
                alert2.setContentText("Failed to Issue Book!");
                alert2.showAndWait();
            }
        } else {
            Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
            alert1.setTitle("Cancelled");
            alert1.setContentText("Issue Cancelled");
            alert1.showAndWait();
        }
    }

    Boolean bookDetailsOn = false ;

    @FXML
    private void loadBookInfo2(ActionEvent event) {
        issueDetailsON = false;
        ObservableList<String> issueData = FXCollections.observableArrayList();
        String bookid = bookIDinput.getText();
        String qu = "SELECT * FROM ISSUE WHERE bookID = '" + bookid + "'AND memberID = '" + memberIDpassed + "'" ;
        ResultSet rs = databaseHandler.executeQuery(qu);
        try {
            while (rs.next()) {
                String mBookID = bookid;
                String mMemberID = memberIDpassed;
                Timestamp mIssueTime = rs.getTimestamp("issueTime");
                int mRenewCount = rs.getInt("renew_count");

                issueData.add("Issue Date and Time :" + mIssueTime.toString());
                issueData.add("Renew Count: " + mRenewCount);
                issueData.add(" ");
                issueData.add("Book Information: ");

                qu = "SELECT * FROM BOOK WHERE ID = '" + mBookID + "'";
                ResultSet r1 = databaseHandler.executeQuery(qu);

                while (r1.next()) {
                    issueData.add("\tBook Name: " + r1.getString("title"));
                    issueData.add("\tBook ID: " + r1.getString("id"));
                    issueData.add("\tAuthor: " + r1.getString("author"));
                    issueData.add("\tPublisher: " + r1.getString("publisher"));
                }
                issueData.add(" ");
                issueData.add("Member Information: ");

                qu = "SELECT * FROM MEMBER WHERE ID = '" + mMemberID + "'";
                ResultSet r2 = databaseHandler.executeQuery(qu);
                while (r2.next()) {
                    issueData.add("\tMember Name: " + r2.getString("name"));
                    issueData.add("\tMember ID: " + r2.getString("id"));
                    issueData.add("\tContact: " + r2.getString("phonenumber"));
                    issueData.add("\tEmail: " + r2.getString("email"));
                }
                issueDetailsON = true;

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        issueDataList.getItems().setAll(issueData);

    }

    @FXML
    private void renewBook(ActionEvent event) {
        if (!issueDetailsON) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Failed");
            alert.setHeaderText(null);
            alert.setContentText("Please select a book to submit!");
            alert.showAndWait();
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Renewal");
        alert.setHeaderText("Are you sure you want to renew your issue of the book: " + bookIDinput.getText() + "?");
        alert.setContentText(null);

        Optional<ButtonType> response = alert.showAndWait();
        if (response.get() == ButtonType.OK) {
            String BookID = bookIDinput.getText() ;
            String qu = "SELECT * FROM ISSUE WHERE bookID = '" + BookID + "'" ;
            ResultSet rs = databaseHandler.executeQuery(qu);
            try {
                if (rs.next()) {
                    Timestamp issueDate = rs.getTimestamp("issueTime") ;
                    Timestamp thisDate = new Timestamp(System.currentTimeMillis());
                    long milliseconds = thisDate.getTime() - issueDate.getTime();
                    long days = TimeUnit.MILLISECONDS.toDays(milliseconds);
                    if (days > 15) {
                        Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                        alert1.setTitle("Information");
                        alert1.setHeaderText(null);
                        alert1.setContentText("This issue has been pending for more than 15 days. Please settle the overdue fees before reissuing the book.");
                        alert1.showAndWait();
                    } else {
                        String action = "UPDATE ISSUE SET IssueTime = CURRENT_TIMESTAMP, renew_count = renew_count + 1 where bookID = '" +  BookID + "'";
                        System.out.println(action);
                        if (databaseHandler.executeAction(action)) {
                            Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                            alert1.setTitle("Success");
                            alert1.setHeaderText(null);
                            alert1.setContentText("Issue Renewed Successfully!");
                            alert1.showAndWait();
                        } else {
                            Alert alert2 = new Alert(Alert.AlertType.ERROR);
                            alert2.setTitle("Failed");
                            alert2.setHeaderText(null);
                            alert2.setContentText("Failed to Renew Issue!");
                            alert2.showAndWait();

                        }
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        } else {
            Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
            alert1.setTitle("Cancelled");
            alert1.setContentText("Renewal Cancelled");
            alert1.showAndWait();
        }

    }

    @FXML
    private void submitBook(ActionEvent event) {
        if (!issueDetailsON) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Failed");
            alert.setHeaderText(null);
            alert.setContentText("Please select a book to submit!");
            alert.showAndWait();
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Submission");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to return the book?");

        Optional<ButtonType> response = alert.showAndWait();

        if (response.isPresent() && response.get() == ButtonType.OK) {
            String id = bookIDinput.getText();
            String qu = "SELECT * FROM ISSUE WHERE bookID = '" + id + "'";
            ResultSet rs = databaseHandler.executeQuery(qu);
            String action = null ;
            int fine = 0 ;
            try {
                if (rs.next()) {
                    String BookID = id;
                    String MemberID = memberIDpassed;
                    Timestamp IssueTime = rs.getTimestamp("issueTime");
                    Timestamp ReturnTime = new Timestamp(System.currentTimeMillis());
                    long milliseconds = ReturnTime.getTime() - IssueTime.getTime();
                    long days = TimeUnit.MILLISECONDS.toDays(milliseconds);
                    if (days > 15) {
                        fine = (int) (days - 15) * 100;
                    }
                    if (fine > 0) {
                        action = "INSERT INTO `RETURN`(bookID,memberID,issueDate,returnDate,lateSubmitFine) VALUES ('" +
                                BookID + "','" + MemberID + "','" + IssueTime + "','" + ReturnTime + "','" + fine + "')";
                    } else {
                        action = "INSERT INTO `RETURN`(bookID,memberID,issueDate,returnDate) VALUES ('" +
                                BookID + "','" + MemberID + "','" + IssueTime + "','" + ReturnTime + "')";
                    }

                }
            } catch (SQLException ex) {
                Logger.getLogger(Main_Controller.class.getName()).log(Level.SEVERE, null, ex);
            }

            String ac1 = "DELETE FROM ISSUE WHERE bookID = '" + id + "'";
            String ac2 = "UPDATE BOOK SET isAvail = '1' WHERE id = '" + id + "'";

            if (databaseHandler.executeAction(action) && databaseHandler.executeAction(ac1) && databaseHandler.executeAction(ac2)) {
                Alert alert3 = new Alert(Alert.AlertType.INFORMATION);
                alert3.setTitle("Success");
                if (fine > 0) {
                    alert3.setHeaderText("Book Submitted Successfully! ");
                    alert3.setContentText("Please pay the late submission fine of Rs." + fine);
                } else {
                    alert3.setHeaderText("Book Submitted Successfully!");
                    alert3.setContentText(null);
                }

                alert3.showAndWait();
            } else {
                Alert alert3 = new Alert(Alert.AlertType.ERROR);
                alert3.setTitle("Failed");
                alert3.setHeaderText("Failed to Submit Book!");
                alert3.setContentText(null);
                alert3.showAndWait();
            }
        } else {
            Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
            alert1.setTitle("Cancelled");
            alert1.setHeaderText("Submission Cancelled");
            alert1.showAndWait();
        }
    }

    @FXML
    private void logOut(ActionEvent event) {
        Alert alert1 = new Alert(Alert.AlertType.CONFIRMATION);
        alert1.setTitle("Confirm Logout");
        alert1.setHeaderText(null);
        alert1.setContentText("Are you sure you want to logout?");

        Optional<ButtonType> response = alert1.showAndWait();
        if (response.isPresent() & response.get() == ButtonType.OK) {
            MemberInfo.setLogin(false);
            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.close();
            Main_Controller.WindowLoader windowLoader = new Main_Controller.WindowLoader();
            windowLoader.loadWindow("login.fxml", "Library Mate");
        } else {
            return ;
        }
    }

    static class WindowLoader {
        public void loadWindow(String location, String title) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/cargologix/library_mate/" + location));
                Scene scene = new Scene(fxmlLoader.load());
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.setTitle(title);
                stage.show();
            } catch (Exception ex) {
                Logger.getLogger(WindowLoader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}

