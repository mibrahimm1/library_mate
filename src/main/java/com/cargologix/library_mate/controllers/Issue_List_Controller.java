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

public class Issue_List_Controller implements Initializable {

    ObservableList<Issue> issuelist = FXCollections.observableArrayList() ;
    @FXML
    private TableColumn<Issue, String> bookIDCol;
    @FXML
    private TableColumn<Issue, String> bookTitleCol;

    @FXML
    private TableColumn<Issue, String> issueDateCol;

    @FXML
    private TableColumn<Issue, String> memberIDCol;

    @FXML
    private TableColumn<Issue, String> memberNameCol;

    @FXML
    private TableColumn<Issue, Integer> renewCol;

    @FXML
    private TableView<Issue> tableView;


    @Override
    public void  initialize(URL url, ResourceBundle rb) {
        initCol();
        loadData();
    }

    private void loadData() {
        db DBhandler = db.getInstance();
        String qu ;
        if (MemberInfo.getLoginStatus()) {
            qu = "SELECT * FROM ISSUE WHERE memberID = '" + MemberInfo.getMemberId() + "'" ;
        } else {
            qu = "SELECT * FROM ISSUE" ;
        }
        ResultSet rs = DBhandler.executeQuery(qu) ;
        System.out.println(rs);
        try {
            while (rs.next()) {
                String BookID = rs.getString("bookID");
                qu = "SELECT * FROM BOOK WHERE id = '" + BookID + "'" ;
                ResultSet rs1 = DBhandler.executeQuery(qu) ;
                if (rs1.next()) {
                    String BookTitle = rs1.getString("title");
                    String MemberID = rs.getString("memberID");
                    qu = "SELECT * FROM MEMBER WHERE id = '" + MemberID + "'";
                    ResultSet rs2 = DBhandler.executeQuery(qu);
                    if (rs2.next()) {
                        String MemberName = rs2.getString("name");
                        Timestamp IssueTime = rs.getTimestamp("issueTime");
                        int RenewCount = rs.getInt("renew_count");

                        issuelist.add(new Issue_List_Controller.Issue(BookTitle, BookID, MemberName, MemberID, IssueTime, RenewCount));
                    }
                    rs2.close();
                }
                rs1.close();
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(Add_Book_Controller.class.getName()).log(Level.SEVERE, null, ex);
        }

        tableView.getItems().setAll(issuelist) ;
    }

    private void initCol() {
        bookTitleCol.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));
        bookIDCol.setCellValueFactory(new PropertyValueFactory<>("bookID"));
        memberNameCol.setCellValueFactory(new PropertyValueFactory<>("memberName"));
        memberIDCol.setCellValueFactory(new PropertyValueFactory<>("memberID"));
        issueDateCol.setCellValueFactory(new PropertyValueFactory<>("issueDate"));
        renewCol.setCellValueFactory(new PropertyValueFactory<>("renewCount"));
    }

    public static class Issue {
        private final SimpleStringProperty BookTitle ;
        private final SimpleStringProperty BookID ;
        private final SimpleStringProperty MemberName ;
        private final SimpleStringProperty MemberID ;
        private final SimpleStringProperty issueDate ;
        private final SimpleIntegerProperty Renew_Count;

        public Issue(String BookTitle, String BookID, String MemberName, String MemberID, Timestamp IssueTime, int RenewCount) {
            this.BookTitle = new SimpleStringProperty(BookTitle) ;
            this.BookID = new SimpleStringProperty(BookID) ;
            this.MemberName = new SimpleStringProperty(MemberName) ;
            this.MemberID = new SimpleStringProperty(MemberID) ;
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            this.issueDate = new SimpleStringProperty(dateFormat.format(IssueTime)) ;
            this.Renew_Count = new SimpleIntegerProperty(RenewCount) ;
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

        public int getRenewCount() {
            return Renew_Count.get();
        }

    }

}

