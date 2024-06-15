package com.cargologix.library_mate.controllers;

import com.cargologix.library_mate.misc.MemberInfo;
import javafx.beans.property.SimpleBooleanProperty;
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


public class Book_List_Controller implements Initializable {
    // CREATING BOOK LIST TO STORE BOOKS
    ObservableList<Book> booklist = FXCollections.observableArrayList() ;
    @FXML
    private AnchorPane rootPane;

    @FXML
    private TableView<Book> tableView;

    @FXML
    private TableColumn<Book, String> authorCol;

    @FXML
    private TableColumn<Book, Boolean> availCol;

    @FXML
    private TableColumn<Book, String> idCol;

    @FXML
    private TableColumn<Book, String> publisherCol;

    @FXML
    private TableColumn<Book, String> titleCol;

    @Override
    public void  initialize(URL url, ResourceBundle rb) {
        // INITIATING COLUMNS
        initCol();
        // RETRIEVING BOOKS FROM DB AND STORING IT IN BOOK LIST
        loadData();
    }
    private void loadData() {
        db DBhandler = db.getInstance();
        String qu ;
        if (MemberInfo.getLoginStatus()) {
            qu = "SELECT * FROM BOOK WHERE isAvail = 1" ;
        } else {
            qu = "SELECT * FROM BOOK" ;
        }

        ResultSet rs = DBhandler.executeQuery(qu) ;
        System.out.println(rs);
        try {
            while (rs.next()) {
                String title = rs.getString("title");
                String author = rs.getString("author");
                String id = rs.getString("id");
                String publisher = rs.getString("publisher");
                Boolean avail = rs.getBoolean("isAvail");
                booklist.add(new Book(title,id,author,publisher,avail)) ;

            }
        } catch (SQLException ex) {
            Logger.getLogger(Add_Book_Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
        tableView.getItems().setAll(booklist) ;
    }

    private void initCol() {
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        authorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
        publisherCol.setCellValueFactory(new PropertyValueFactory<>("publisher"));
        availCol.setCellValueFactory(new PropertyValueFactory<>("avail"));
    }

    public static class Book{
        private final SimpleStringProperty title ;
        private final SimpleStringProperty id ;
        private final SimpleStringProperty author ;
        private final SimpleStringProperty publisher ;
        private final SimpleBooleanProperty avail ;

        public Book(String title, String id, String author, String pub, Boolean avail) {
            this.title = new SimpleStringProperty(title) ;
            this.id = new SimpleStringProperty(id) ;
            this.author = new SimpleStringProperty(author) ;
            this.publisher = new SimpleStringProperty(pub) ;
            this.avail = new SimpleBooleanProperty(avail) ;
        }

        public String getTitle() {
            return title.get();
        }

        public String getId() {
            return id.get();
        }

        public String getAuthor() {
            return author.get();
        }

        public String getPublisher() {
            return publisher.get();
        }

        public boolean isAvail() {
            return avail.get();
        }

    }

}

