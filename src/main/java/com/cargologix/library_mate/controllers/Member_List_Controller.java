package com.cargologix.library_mate.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import com.cargologix.library_mate.database.db;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Member_List_Controller implements Initializable {
    // CREATING MEMBER LIST TO STORE BOOKS
    ObservableList<Member_List_Controller.Member> memberlist = FXCollections.observableArrayList();

    @FXML
    private AnchorPane rootPane;

    @FXML
    private TableView<Member> tableView;

    @FXML
    private TableColumn<Member, String> emailCol;

    @FXML
    private TableColumn<Member, String> idCol;

    @FXML
    private TableColumn<Member, String> nameCol;

    @FXML
    private TableColumn<Member, String> phoneNumCol;



    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // INITIATING COLUMNS
        initCol();
        // RETRIEVING MEMBERS FROM DB AND STORING IT IN MEMBERS LIST
        loadData();
    }

    private void loadData() {
        // INITIATING DATABASE HANDLER
        db DBhandler = db.getInstance();

        // QUERY TO BE EXECUTE
        String qu = "SELECT * FROM MEMBER" ;
        // EXECUTING QUERY
        ResultSet rs = DBhandler.executeQuery(qu) ;
        System.out.println(rs);
        try {
            while (rs.next()) { // UNTIL RESULT SET IS NOT FINISHED
                // EXTRACTING DATA FROM DATABASE
                String name = rs.getString("name");
                String id = rs.getString("id");
                String phoneNum = rs.getString("phonenumber");
                String email = rs.getString("email");
                // ADDING MEMBER TO THE MEMBER LIST
                memberlist.add(new Member(name,id,phoneNum,email)) ;

            }
        } catch (SQLException ex) {
            Logger.getLogger(Member_List_Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
        // ADDING ITEMS FROM MEMBER LIST TO THE TABLE VIEW
        tableView.getItems().setAll(memberlist) ;
    }

    private void initCol() {
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        phoneNumCol.setCellValueFactory(new PropertyValueFactory<>("phoneNum"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));

    }

    // STRUCTURE OF MEMBER CLASS
    public static class Member {
        private final SimpleStringProperty name;
        private final SimpleStringProperty id;
        private final SimpleStringProperty phoneNum;
        private final SimpleStringProperty email;

        public Member(String name, String id, String phoneNum, String email) {
            this.name = new SimpleStringProperty(name);
            this.id = new SimpleStringProperty(id);
            this.phoneNum = new SimpleStringProperty(phoneNum);
            this.email = new SimpleStringProperty(email);
        }

        public String getName() {
            return name.get();
        }


        public String getId() {
            return id.get();
        }

        public String getPhoneNum() {
            return phoneNum.get();
        }


        public String getEmail() {
            return email.get();
        }

    }
}



