package com.example.library2;

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
import javafx.stage.Stage;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class BookcontrolController implements Initializable{
    @FXML
    private TableView<Books> bookcontrolTable;

    @FXML
    private TableColumn<Books, String> title;

    @FXML
    private TableColumn<Books, String> author;

    @FXML
    private TableColumn<Books, String> authorlastname;

    @FXML
    private TableColumn<Books, Integer> firstpub;

    @FXML
    private TableColumn<Books, String> isbn;

    @FXML
    private TableColumn<Books, String> keyword1;

    @FXML
    private TableColumn<Books, String> keyword2;

    @FXML
    private TableColumn<Books, String> keyword3;

    @FXML
    private TableColumn<Books, String> keyword4;

    @FXML
    private TableColumn<Books, String> keyword5;


    @FXML
    private Label bookcontrolLabel;

    @FXML
    private Button logoutButton;

    @FXML
    private Button returnButton;

    @FXML
    private TextField searchbar;

    @FXML
    private Label nameLabel2;

    private String id2 = null;
    private String category = null;

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

    public void bookcontrol(ActionEvent event) throws IOException
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
        title.setCellValueFactory(new PropertyValueFactory<Books, String>("title"));
        author.setCellValueFactory(new PropertyValueFactory<Books, String>("author"));
        authorlastname.setCellValueFactory(new PropertyValueFactory<Books, String>("authorlastname"));
        firstpub.setCellValueFactory(new PropertyValueFactory<Books, Integer>("firstpub"));
        isbn.setCellValueFactory(new PropertyValueFactory<Books, String>("isbn"));
        keyword1.setCellValueFactory(new PropertyValueFactory<Books, String>("keyword1"));
        keyword2.setCellValueFactory(new PropertyValueFactory<Books, String>("keyword2"));
        keyword3.setCellValueFactory(new PropertyValueFactory<Books, String>("keyword3"));
        keyword4.setCellValueFactory(new PropertyValueFactory<Books, String>("keyword4"));
        keyword5.setCellValueFactory(new PropertyValueFactory<Books, String>("keyword5"));

        bookcontrolTable.setItems(getBooks());
        //BookcontrolTable.getColumns().addAll(title, author, firstpub, isbn, keyword1,keyword2,keyword3,keyword4,keyword5);

        //filtered list
        FilteredList<Books> filteredData = new FilteredList<>(getBooks(), b -> true);

        searchbar.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(Books -> {

                if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {
                    return true;
                }
                String searchword = newValue.toLowerCase();

                if (Books.getAuthor().toLowerCase().indexOf(searchword) > -1) {
                    return true;
                } else if (Books.getAuthorlastname().toLowerCase().indexOf(searchword) > -1) {
                    return true;
                } else if (Books.getFirstpub().toString().indexOf(searchword) > -1) {
                    return true;
                } else if (Books.getIsbn().toLowerCase().indexOf(searchword) > -1) {
                    return true;
                } else if (Books.getTitle().toLowerCase().indexOf(searchword) > -1) {
                    return true;
                } else if (Books.getKeyword1().toLowerCase().indexOf(searchword) > -1) {
                    return true;
                } else if (Books.getKeyword2().toLowerCase().indexOf(searchword) > -1) {
                    return true;
                } else if (Books.getKeyword3().toLowerCase().indexOf(searchword) > -1) {
                    return true;
                } else if (Books.getKeyword4().toLowerCase().indexOf(searchword) > -1) {
                    return true;
                } else if (Books.getKeyword5().toLowerCase().indexOf(searchword) > -1) {
                    return true;
                } else
                    return false;
            });
        });

        SortedList<Books> sortedData = new SortedList <>(filteredData);

        sortedData.comparatorProperty().bind(bookcontrolTable.comparatorProperty());

        bookcontrolTable.setItems(sortedData);
    }

    public ObservableList<Books> getBooks()
    {
        ObservableList<Books> books = FXCollections.observableArrayList();
        ResultSet rs;
        PreparedStatement pst;
        Connect_SQL connect = new Connect_SQL();
        String sql = "select authors.authorname, authors.authorlastname, books.title, books.authorid, books.firstpub, books.ISBN, books.keyword1, books.keyword2, books.keyword3, books.keyword4, books.keyword5 from books inner join authors on books.authorid = authors.authorid inner join available on available.bookid = books.bookid";
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
                String authorlastname = rs.getString("authors.authorlastname");
                    //System.out.println("author name: "+author);
                books.add(new Books(title, author, authorlastname, isbn, keyword1, keyword2, keyword3, keyword4, keyword5, firstpub));
                //}
            }

        } catch (Exception e) {
            System.out.println(e);
        }
        System.out.println(books);
        return books;
    }
}
