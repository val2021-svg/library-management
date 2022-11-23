package com.example.library2;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;


public class BorrowedController implements Initializable{
    @FXML
    private TableView<Borrowed> borrowedTable;

    @FXML
    private TableColumn<Borrowed, String> title;

    @FXML
    private TableColumn<Borrowed, String> author;

    @FXML
    private TableColumn<Borrowed, Integer> firstpub;

    @FXML
    private TableColumn<Borrowed, String> isbn;

    @FXML
    private TableColumn<Borrowed, String> keyword1;

    @FXML
    private TableColumn<Borrowed, String> keyword2;

    @FXML
    private TableColumn<Borrowed, String> keyword3;

    @FXML
    private TableColumn<Borrowed, String> keyword4;

    @FXML
    private TableColumn<Borrowed, String> keyword5;


    @FXML
    private Label borrowedLabel;

    @FXML
    private Button logoutButton;

    @FXML
    private Button returnButton;

    @FXML
    private Label nameLabel2;

    private String id2 = null;

    private String category = null;

    @FXML
    private TextField searchbar;

    public void displayname2(String id){
        this.id2 =  id;
        ResultSet rs;
        PreparedStatement pst;
        Connect_SQL connect = new Connect_SQL();
        String sql = "select category from admins where id = '" + id + "' ";
        try {
            Connection con = Connect_SQL.ConnectDb();
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                String category = rs.getString("category");
                this.category =  category;
                nameLabel2.setText("ID: "+id+"\n"+category);
            }
        }catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }

    @FXML
    public void logout(ActionEvent event) throws IOException {
        Parent view3 = FXMLLoader.load(getClass().getResource("hello-view.fxml"));
        Scene scene3 = new Scene(view3);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene3);
        window.show();
    }

    @FXML
    public void returning(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("menu-view.fxml"));
        Parent view3 = loader.load();

        MenuController menuController = loader.getController();
        menuController.displayname(id2);

        //Parent view3 = FXMLLoader.load(getClass().getResource("menu-view.fxml"));
        Scene scene3 = new Scene(view3);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene3);
        window.show();
    }

    public void borrowed(ActionEvent event) throws IOException
    {
        Parent tableViewParent = FXMLLoader.load(getClass().getResource("menu-view.fxml"));
        Scene tableViewScene = new Scene(tableViewParent);

        //This line gets the Stage information
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

        window.setScene(tableViewScene);
        window.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        title.setCellValueFactory(new PropertyValueFactory<Borrowed, String>("title"));
        author.setCellValueFactory(new PropertyValueFactory<Borrowed, String>("author"));
        firstpub.setCellValueFactory(new PropertyValueFactory<Borrowed, Integer>("firstpub"));
        isbn.setCellValueFactory(new PropertyValueFactory<Borrowed, String>("isbn"));
        keyword1.setCellValueFactory(new PropertyValueFactory<Borrowed, String>("keyword1"));
        keyword2.setCellValueFactory(new PropertyValueFactory<Borrowed, String>("keyword2"));
        keyword3.setCellValueFactory(new PropertyValueFactory<Borrowed, String>("keyword3"));
        keyword4.setCellValueFactory(new PropertyValueFactory<Borrowed, String>("keyword4"));
        keyword5.setCellValueFactory(new PropertyValueFactory<Borrowed, String>("keyword5"));

        borrowedTable.setItems(getBorrowed());
        //BookcontrolTable.getColumns().addAll(title, author, firstpub, isbn, keyword1,keyword2,keyword3,keyword4,keyword5);

        //filtered list
        FilteredList<Borrowed> filteredData = new FilteredList<>(getBorrowed(), b -> true);

        searchbar.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(Borrowed -> {

                if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {
                    return true;
                }
                String searchword = newValue.toLowerCase();

                if (Borrowed.getAuthor().toLowerCase().indexOf(searchword) > -1) {
                    return true;
                } else if (Borrowed.getFirstpub().toString().indexOf(searchword) > -1) {
                    return true;
                } else if (Borrowed.getIsbn().toLowerCase().indexOf(searchword) > -1) {
                    return true;
                } else if (Borrowed.getTitle().toLowerCase().indexOf(searchword) > -1) {
                    return true;
                } else if (Borrowed.getKeyword1().toLowerCase().indexOf(searchword) > -1) {
                    return true;
                } else if (Borrowed.getKeyword2().toLowerCase().indexOf(searchword) > -1) {
                    return true;
                } else if (Borrowed.getKeyword3().toLowerCase().indexOf(searchword) > -1) {
                    return true;
                } else if (Borrowed.getKeyword4().toLowerCase().indexOf(searchword) > -1) {
                    return true;
                } else if (Borrowed.getKeyword5().toLowerCase().indexOf(searchword) > -1) {
                    return true;
                } else
                    return false;
            });
        });

        SortedList<Borrowed> sortedData = new SortedList <>(filteredData);

        sortedData.comparatorProperty().bind(borrowedTable.comparatorProperty());

        borrowedTable.setItems(sortedData);
    }
    public ObservableList<Borrowed> getBorrowed()
    {
        ObservableList<Borrowed> borrowed = FXCollections.observableArrayList();
        ResultSet rs;
        PreparedStatement pst;
        Connect_SQL connect = new Connect_SQL();
        String sql = "select authors.authorname, books.title, books.authorid, books.firstpub, books.ISBN, books.keyword1, books.keyword2, books.keyword3, books.keyword4, books.keyword5 from books inner join authors on books.authorid = authors.authorid inner join currentloan on currentloan.bookid = books.bookid";
        //String sql = "select title, authorid, firstpub, ISBN, keyword1, keyword2, keyword3, keyword4, keyword5 from books";
        //String sql1 = "select authors.authorname from authors inner join books on books.authorid = authors.authorid where authors.authorid = ?";
        try {
            Connection con = Connect_SQL.ConnectDb();


            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();

            while (rs.next()) {
                int authorid = rs.getInt("books.authorid");
                System.out.println("author id: "+authorid);
                int firstpub = rs.getInt("books.firstpub");
                String title = rs.getString("books.title");
                String isbn = rs.getString("books.ISBN");
                String keyword1 = rs.getString("books.keyword1");
                String keyword2 = rs.getString("books.keyword2");
                String keyword3 = rs.getString("books.keyword3");
                String keyword4 = rs.getString("books.keyword4");
                String keyword5 = rs.getString("books.keyword5");
                //pst1 = con.prepareStatement(sql1);
                //pst1.setInt(1, authorid);
                //rs1 = pst1.executeQuery();
                //if (rs1.next()) {
                String author = rs.getString("authors.authorname");
                    //System.out.println("author name: "+author);
                borrowed.add(new Borrowed(title, author, isbn, keyword1, keyword2, keyword3, keyword4, keyword5, firstpub));
                //}
            }

        } catch (Exception e) {
            System.out.println(e);
        }
        System.out.println(borrowed);
        return borrowed;
    }
}
