package com.cargologix.library_mate.controllers;
import com.cargologix.library_mate.database.db;

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

public class Main_Controller implements Initializable {

    @FXML
    private Pane bookInfo;

    @FXML
    private Pane memberinfo;

    @FXML
    private Label bookAuthor;

    @FXML
    private TextField bookIDin;

    @FXML
    private BorderPane borderPane;

    @FXML
    private TextField memberIDin;

    @FXML
    private Label bookName;

    @FXML
    private Label bookStatus;

    @FXML
    private Button deleteMemberbtn;

    @FXML
    private ListView<String> memberDetails;

    @FXML
    private TextField memberIDverify;

    @FXML
    private TextField updateMemberContact;

    @FXML
    private TextField updateMemberID;

    @FXML
    private TextField updateMemberName;

    @FXML
    private Button updateMemberbtn;


    @FXML
    private TextField updateEmailAddress;

    @FXML
    private Button submitBtn;

    @FXML
    private Button renewBtn;

    @FXML
    private Button AddBook;

    @FXML
    private Button AddMember;

    @FXML
    private Button BookList;

    @FXML
    private Button IssueList;

    @FXML
    private Button MemberList;

    @FXML
    private StackPane rootPane;

    @FXML
    private Button issueButton;

    @FXML
    private Label memberContact;

    @FXML
    private Label memberName;

    @FXML
    private TextField bookIDinput;

    @FXML
    private ListView<String> issueDataList;
    @FXML
    private ListView<String> bookDetails;
    @FXML
    private VBox bookUpdateForm;
    @FXML
    private Button deleteBookbtn;
    @FXML
    private Button updateBookbtn;

    @FXML
    private TextField bookIDverify;
    @FXML
    private TextField updateBookAuthor;

    @FXML
    private TextField updateBookID;

    @FXML
    private TextField updateBookPublisher;

    @FXML
    private TextField updateBookTitle;



    db databaseHandler;

    String mName;
    String bName;

    Boolean issueDetailsON = false;
    Boolean memberDetailsOn = false ;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        JFXDepthManager.setDepth(bookInfo, 1);
        JFXDepthManager.setDepth(memberinfo, 1);
        databaseHandler = db.getInstance();

    }

    @FXML
    private void loadAddMember(ActionEvent event) {
        WindowLoader windowLoader = new WindowLoader();
        windowLoader.loadWindow("member_add.fxml", "Add New Member");


    }

    @FXML
    private void loadAddBook(ActionEvent event) {
        WindowLoader windowLoader = new WindowLoader();
        windowLoader.loadWindow("add_book.fxml", "Add New Book");

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
    private void loadMemberInfo(ActionEvent event) {
        String id = memberIDin.getText();
        String qu = "SELECT * FROM MEMBER WHERE id = '" + id + "'";
        ResultSet rs = databaseHandler.executeQuery(qu);
        Boolean found = false;
        try {
            while (rs.next() & !found) {
                mName = rs.getString("name");
                String mContact = rs.getString("phonenumber");
                memberName.setText("Member Name: " + mName);
                memberContact.setText("Contact: " + mContact);
                found = true;
            }
            if (!found) {
                memberName.setText("Member Name: " + "N/A");
                memberContact.setText("Contact: " + "N/A");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Main_Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    @FXML
    private void issueBook(ActionEvent event) {
        String memberID = memberIDin.getText();
        String bookID = bookIDin.getText();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Issue");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to issue the book " + bName + "\n to " + mName + "?");

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
    private void loadBookDetails(ActionEvent event) {
        bookDetailsOn = false ;
        String id = bookIDverify.getText();
        String qu = "SELECT * FROM BOOK WHERE id = '" + id + "'";
        ResultSet rs = databaseHandler.executeQuery(qu);
        ObservableList<String> bookData = FXCollections.observableArrayList();
        try {
            while (rs.next()) {
                bookData.add("Book Information: \n") ;
                bookData.add("\tBook ID: " + rs.getString("id"));
                bookData.add("\tBook Name: " + rs.getString("title"));
                bookData.add("\tAuthor: " + rs.getString("author"));
                bookData.add("\tPublisher: " + rs.getString("publisher"));
            }
            bookDetailsOn = true ;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        bookDetails.getItems().setAll(bookData);

    }

    @FXML
    private void loadMemberDetails(ActionEvent event) {
        memberDetailsOn = false ;
        String id = memberIDverify.getText();
        String qu = "SELECT * FROM MEMBER WHERE id = '" + id + "'";
        ResultSet rs = databaseHandler.executeQuery(qu);
        ObservableList<String> memberData = FXCollections.observableArrayList();
        try {
            while (rs.next()) {
                memberData.add("Member Information: \n") ;
                memberData.add("\tMember ID: " + rs.getString("id"));
                memberData.add("\tName: " + rs.getString("name"));
                memberData.add("\tContact: " + rs.getString("phonenumber"));
                memberData.add("\tEmail: " + rs.getString("email"));
            }
            memberDetailsOn = true ;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        memberDetails.getItems().setAll(memberData);

    }

    @FXML
    private void loadBookInfo2(ActionEvent event) {
        issueDetailsON = false;
        ObservableList<String> issueData = FXCollections.observableArrayList();
        String bookid = bookIDinput.getText();
        String qu = "SELECT * FROM ISSUE WHERE bookID = '" + bookid + "'";
        ResultSet rs = databaseHandler.executeQuery(qu);
        try {
            while (rs.next()) {
                String mBookID = bookid;
                String mMemberID = rs.getString("memberID");
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
                    String MemberID = rs.getString("memberID");
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
    private void deleteBook(ActionEvent event) {
        if (!bookDetailsOn) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Failed");
            alert.setHeaderText(null);
            alert.setContentText("Please enter in a Book ID to view the details");
            alert.showAndWait();
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Deletion");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete the book?");

        Optional<ButtonType> response = alert.showAndWait();

        if (response.isPresent() && response.get() == ButtonType.OK) {
            String ac1 = "DELETE FROM BOOK WHERE id = '" + bookIDverify.getText() + "'";
            String ac2 = "DELETE FROM ISSUE WHERE bookID = '" + bookIDverify.getText() + "'";
            String ac3 = "DELETE FROM `RETURN` WHERE bookID = '" + bookIDverify.getText() + "'";

            if (databaseHandler.executeAction(ac3) && databaseHandler.executeAction(ac1) && databaseHandler.executeAction(ac2)) {
                Alert alert3 = new Alert(Alert.AlertType.INFORMATION);
                alert3.setTitle("Success");
                alert3.setHeaderText("Book Deleted Successfully!");
                alert3.setContentText(null);
                alert3.showAndWait();
            } else {
                Alert alert3 = new Alert(Alert.AlertType.ERROR);
                alert3.setTitle("Failed");
                alert3.setHeaderText("Failed to Delete Book!");
                alert3.setContentText(null);
                alert3.showAndWait();
            }
        } else {
            Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
            alert1.setTitle("Cancelled");
            alert1.setHeaderText("Deletion Cancelled");
            alert1.showAndWait();
        }

    }

    @FXML
    private void deleteMember(ActionEvent event) {
        if (!memberDetailsOn) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Failed");
            alert.setHeaderText(null);
            alert.setContentText("Please enter in a Member ID to view the details");
            alert.showAndWait();
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Deletion");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete the member?");

        Optional<ButtonType> response = alert.showAndWait();

        if (response.isPresent() && response.get() == ButtonType.OK) {
            String ac1 = "DELETE FROM MEMBER WHERE id = '" + memberIDverify.getText() + "'";
            String ac2 = "DELETE FROM ISSUE WHERE memberID = '" + memberIDverify.getText() + "'";
            String ac3 = "DELETE FROM `RETURN` WHERE memberID = '" + memberIDverify.getText() + "'";

            if (databaseHandler.executeAction(ac3) && databaseHandler.executeAction(ac1) && databaseHandler.executeAction(ac2)) {
                Alert alert3 = new Alert(Alert.AlertType.INFORMATION);
                alert3.setTitle("Success");
                alert3.setHeaderText("Member Deleted Successfully!");
                alert3.setContentText(null);
                alert3.showAndWait();
            } else {
                Alert alert3 = new Alert(Alert.AlertType.ERROR);
                alert3.setTitle("Failed");
                alert3.setHeaderText("Failed to Delete Member!");
                alert3.setContentText(null);
                alert3.showAndWait();
            }
        } else {
            Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
            alert1.setTitle("Cancelled");
            alert1.setHeaderText("Deletion Cancelled");
            alert1.showAndWait();
        }

    }

    @FXML
    private void updateBook(ActionEvent event) {
        if (!bookDetailsOn) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Failed");
            alert.setHeaderText(null);
            alert.setContentText("Please enter in a Book ID to view the details");
            alert.showAndWait();
            return;
        }
        Alert alert1 = new Alert(Alert.AlertType.CONFIRMATION);
        alert1.setTitle("Confirm Deletion");
        alert1.setHeaderText(null);
        alert1.setContentText("Are you sure you want to update the book details?");

        Optional<ButtonType> response = alert1.showAndWait();

        if (response.isPresent() && response.get() == ButtonType.OK) {
            String newId = updateBookID.getText() ;
            String newTitle = updateBookTitle.getText() ;
            String newAuthor = updateBookAuthor.getText();
            String newPublisher = updateBookPublisher.getText() ;
            String oldId = bookIDverify.getText() ;
            if (newId.isEmpty() || newTitle.isEmpty() || newAuthor.isEmpty() || newPublisher.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR) ;
                alert.setHeaderText(null);
                alert.setContentText("Please Enter in all fields");
                alert.showAndWait();
                return;
            }

            try {
                // Start transaction
                connection.setAutoCommit(false);
                // Update BOOK table
                String updateBookQuery = "UPDATE BOOK SET id = '" + newId + "', title = '" + newTitle + "', author = '" + newAuthor + "', publisher = '" + newPublisher + "' WHERE id = '" + oldId + "'";
                if (!databaseHandler.executeAction(updateBookQuery)) {
                    throw new SQLException("Failed to update BOOK table");
                }

                String updateIssueQuery = "UPDATE ISSUE SET bookID = '" + newId + "' WHERE bookID = '" + oldId + "'";
                if (!databaseHandler.executeAction(updateIssueQuery)) {
                    throw new SQLException("Failed to update ISSUE table");
                }
                String updateReturnQuery = "UPDATE `RETURN` SET bookID = '" + newId + "' WHERE bookID = '" + oldId + "'";
                if (!databaseHandler.executeAction(updateReturnQuery)) {
                    throw new SQLException("Failed to update `RETURN` table");
                }

                connection.commit();
                connection.setAutoCommit(true);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(null);
                alert.setContentText("Book updated successfully!");
                alert.showAndWait();

            } catch (SQLException e) {
                // Rollback transaction if any update fails
                try {
                    connection.rollback();
                    connection.setAutoCommit(true);
                } catch (SQLException rollbackEx) {
                    e.printStackTrace();
                }

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("Failed to update book: " + e.getMessage());
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Cancelled");
            alert.setHeaderText("Update Cancelled");
            alert.showAndWait();
        }
    }
    @FXML
    private void updateMemberInfo(ActionEvent event) {
        if (!memberDetailsOn) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Failed");
            alert.setHeaderText(null);
            alert.setContentText("Please enter in a Member ID to view the details");
            alert.showAndWait();
            return;
        }
        Alert alert1 = new Alert(Alert.AlertType.CONFIRMATION);
        alert1.setTitle("Confirm Deletion");
        alert1.setHeaderText(null);
        alert1.setContentText("Are you sure you want to update the member details?");

        Optional<ButtonType> response = alert1.showAndWait();

        if (response.isPresent() && response.get() == ButtonType.OK) {
            String newId = updateMemberID.getText() ;
            String newName = updateMemberName.getText() ;
            String newContact = updateMemberContact.getText();
            String newEmail = updateEmailAddress.getText() ;
            String oldId = memberIDverify.getText();
            if (newId.isEmpty() || newName.isEmpty() || newContact.isEmpty() || newEmail.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR) ;
                alert.setHeaderText(null);
                alert.setContentText("Please Enter in all fields");
                alert.showAndWait();
                return;
            }

            String qu = "UPDATE MEMBER SET id = " +
                    "'" + newId + "', name = " +
                    "'" + newName + "', phonenumber = " +
                    "'" + newContact + "', email = " +
                    "'" + newEmail + "' WHERE id = '" + memberIDverify.getText() + "'";
            String updateIssueQuery = "UPDATE ISSUE SET memberID = '" + newId + "' WHERE bookID = '" + oldId + "'";
            if (!databaseHandler.executeAction(updateIssueQuery)) {
                try {
                    throw new SQLException("Failed to update ISSUE table");
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            String updateReturnQuery = "UPDATE `RETURN` SET memberID = '" + newId + "' WHERE memberID = '" + oldId + "'";
            if (!databaseHandler.executeAction(updateReturnQuery)) {
                try {
                    throw new SQLException("Failed to update `RETURN` table");
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            System.out.println(qu);
            if (databaseHandler.executeAction(qu))
            {
                Alert alert = new Alert(Alert.AlertType.INFORMATION) ;
                alert.setHeaderText(null);
                alert.setContentText("Member Updated Successfully!");
                alert.showAndWait();

            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR) ;
                alert.setHeaderText(null);
                alert.setContentText("Failed to Update Member!");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Cancelled");
            alert.setHeaderText("Update Cancelled");
            alert.showAndWait();
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
            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.close();
            WindowLoader windowLoader = new WindowLoader();
            windowLoader.loadWindow("login.fxml", "Library Mate");
        } else {
            return ;
        }
    }

    static class WindowLoader {
        public User_Dashboard_Controller loadWindow(String location, String title) {
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
            return null;
        }
    }
}
