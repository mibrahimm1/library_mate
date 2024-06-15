package com.cargologix.library_mate.controllers;

import com.cargologix.library_mate.database.db;
import java.text.SimpleDateFormat;

import com.cargologix.library_mate.misc.MemberInfo;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Return_List_Controller implements Initializable {

    ObservableList<Return> returnslist = FXCollections.observableArrayList() ;
    @FXML
    private TableColumn<Return, String> bookIDCol;

    @FXML
    private TableColumn<Return, String> bookTitleCol;

    @FXML
    private TableColumn<Return, Integer> fineCol;

    @FXML
    private TableColumn<Return, String> issueDateCol;

    @FXML
    private TableColumn<Return, String> memberIDCol;

    @FXML
    private TableColumn<Return, String> memberNameCol;

    @FXML
    private TableColumn<Return, String> returnDateCol;

    @FXML
    private AnchorPane rootPane;

    @FXML
    private TableView<Return> tableView;

    @Override
    public void  initialize(URL url, ResourceBundle rb) {
        initCol();
        loadData();
    }

    private void loadData() {
        db DBhandler = db.getInstance();
        String qu ;
        if (MemberInfo.getLoginStatus()) {
            qu = "SELECT * FROM `RETURN` WHERE memberID = '" + MemberInfo.getMemberId() + "'" ;
        } else {
            qu = "SELECT * FROM `RETURN`" ;
        }
        ResultSet rs = DBhandler.executeQuery(qu) ;
        System.out.println(rs);
        try {
            while (rs.next()) {
                String BookID = rs.getString("bookID");
                String bookQuery = "SELECT * FROM BOOK WHERE id = '" + BookID + "'" ;
                ResultSet rs1 = DBhandler.executeQuery(bookQuery) ;
                if (rs1.next()) {
                    String BookTitle = rs1.getString("title");
                    String MemberID = rs.getString("memberID");
                    String memberQuery = "SELECT * FROM MEMBER WHERE id = '" + MemberID + "'";
                    ResultSet rs2 = DBhandler.executeQuery(memberQuery);
                    if (rs2.next()) {
                        String MemberName = rs2.getString("name");
                        Timestamp IssueTime = rs.getTimestamp("issueDate");
                        Timestamp ReturnTime = rs.getTimestamp("returnDate");
                        int LateFine = rs.getInt("lateSubmitFine");

                        returnslist.add(new Return(BookTitle, BookID, MemberName, MemberID, IssueTime, ReturnTime, LateFine));
                    }
                    rs2.close();
                }
                rs1.close();
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(Add_Book_Controller.class.getName()).log(Level.SEVERE, null, ex);
        }

        tableView.getItems().setAll(returnslist) ;
    }

    private void initCol() {
        bookTitleCol.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));
        bookIDCol.setCellValueFactory(new PropertyValueFactory<>("bookID"));
        memberNameCol.setCellValueFactory(new PropertyValueFactory<>("memberName"));
        memberIDCol.setCellValueFactory(new PropertyValueFactory<>("memberID"));
        issueDateCol.setCellValueFactory(new PropertyValueFactory<>("issueDate"));
        returnDateCol.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
        fineCol.setCellValueFactory(new PropertyValueFactory<>("fine"));
    }

    public static class Return {
        private final SimpleStringProperty BookTitle ;
        private final SimpleStringProperty BookID ;
        private final SimpleStringProperty MemberName ;
        private final SimpleStringProperty MemberID ;
        private final SimpleStringProperty issueDate ;
        private final SimpleStringProperty returnDate;
        private final SimpleIntegerProperty fine ;

        public Return(String BookTitle, String BookID, String MemberName, String MemberID, Timestamp IssueTime, Timestamp ReturnTime, int fine) {
            this.BookTitle = new SimpleStringProperty(BookTitle) ;
            this.BookID = new SimpleStringProperty(BookID) ;
            this.MemberName = new SimpleStringProperty(MemberName) ;
            this.MemberID = new SimpleStringProperty(MemberID) ;
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            this.issueDate = new SimpleStringProperty(dateFormat.format(IssueTime)) ;
            this.returnDate = new SimpleStringProperty(dateFormat.format(ReturnTime)) ;
            this.fine = new SimpleIntegerProperty(fine) ;
        }

        public String getBookTitle() {
            return BookTitle.get();
        }
        public String getMemberName() {
            return MemberName.get();
        }
        public String getBookID() {
            return BookID.get();
        }
        public String getMemberID() {
            return MemberID.get();
        }
        public String getIssueDate() {
            return issueDate.get();
        }

        public String getReturnDate() {
            return returnDate.get();
        }
        public int getFine() {
            return fine.get() ;
        }

    }

}

