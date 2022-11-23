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


public class UsercontrolControlleradm implements Initializable{
    @FXML
    private TableView<Users> usercontrolTable;

    @FXML
    private TableColumn<Users, String> title;

    @FXML
    private TableColumn<Users, Integer> userid;

    @FXML
    private TableColumn<Users, String> username;

    @FXML
    private TableColumn<Users, String> userlastname;

    @FXML
    private TableColumn<Users, String> issuedate;

    @FXML
    private TableColumn<Users, String> isbn;

    @FXML
    private TableColumn<Users, Integer> edition;

    @FXML
    private TableColumn<Users, Integer> loanid;


    @FXML
    private Label usercontrolLabel;

    @FXML
    private Button logoutButton;

    @FXML
    private Button returnButton;

    @FXML
    private Label nameLabel2;

    @FXML
    private TextField searchbar;

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

    public void usercontrol(ActionEvent event) throws IOException
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
        userid.setCellValueFactory(new PropertyValueFactory<Users, Integer>("userid"));
        username.setCellValueFactory(new PropertyValueFactory<Users, String>("username"));
        userlastname.setCellValueFactory(new PropertyValueFactory<Users, String>("userlastname"));
        title.setCellValueFactory(new PropertyValueFactory<Users, String>("title"));
        issuedate.setCellValueFactory(new PropertyValueFactory<Users, String>("issuedate"));
        isbn.setCellValueFactory(new PropertyValueFactory<Users, String>("isbn"));
        edition.setCellValueFactory(new PropertyValueFactory<Users, Integer>("edition"));
        loanid.setCellValueFactory(new PropertyValueFactory<Users, Integer>("loanid"));

        usercontrolTable.setItems(getUsers());
        //BookcontrolTable.getColumns().addAll(title, author, firstpub, isbn, keyword1,keyword2,keyword3,keyword4,keyword5);

        //filtered list
        FilteredList<Users> filteredData = new FilteredList<>(getUsers(), b -> true);

        searchbar.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(Users -> {

                if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {
                    return true;
                }
                String searchword = newValue.toLowerCase();

                if (Users.getUserid().toString().indexOf(searchword) > -1) {
                    return true;
                } else if (Users.getUsername().toLowerCase().indexOf(searchword) > -1) {
                    return true;
                } else if (Users.getUserlastname().toLowerCase().indexOf(searchword) > -1) {
                    return true;
                } else if (Users.getTitle().toLowerCase().indexOf(searchword) > -1) {
                    return true;
                } else if (Users.getIssuedate().toLowerCase().indexOf(searchword) > -1) {
                    return true;
                } else if (Users.getIsbn().toLowerCase().indexOf(searchword) > -1) {
                    return true;
                } else if (Users.getEdition().toString().indexOf(searchword) > -1) {
                    return true;
                } else if (Users.getLoanid().toString().indexOf(searchword) > -1) {
                    return true;
                } else
                    return false;
            });
        });

        SortedList<Users> sortedData = new SortedList <>(filteredData);

        sortedData.comparatorProperty().bind(usercontrolTable.comparatorProperty());

        usercontrolTable.setItems(sortedData);
    }
    public ObservableList<Users> getUsers()
    {
        ObservableList<Users> users = FXCollections.observableArrayList();
        ResultSet rs;
        PreparedStatement pst;
        Connect_SQL connect = new Connect_SQL();
        String sql = "SELECT currentloan.idloan, currentloan.id, admins.name, admins.lastname, books.title, currentloan.loandate, books.isbn, editions.edition from currentloan inner join admins on currentloan.id = admins.id inner join books on currentloan.bookid = books.bookid inner join editions on books.isbn = editions.isbn";
        try {
            Connection con = Connect_SQL.ConnectDb();

            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();

            while (rs.next()) {
                int userid = rs.getInt("currentloan.id");
                System.out.println("user id: "+userid);
                int loanid = rs.getInt("currentloan.idloan");
                System.out.println("user id: "+loanid);
                int edition = rs.getInt("editions.edition");
                System.out.println("edition: "+edition);
                String title = rs.getString("books.title");
                System.out.println("title: "+title);
                String isbn = rs.getString("books.isbn");
                System.out.println("isbn: "+isbn);
                String username = rs.getString("admins.name");
                System.out.println("username: "+username);
                String userlastname = rs.getString("admins.lastname");
                System.out.println("userlastname: "+userlastname);
                String issuedate = rs.getString("currentloan.loandate");
                System.out.println("issuedate: "+issuedate);
                users.add(new Users(userid, username, userlastname, title, issuedate, isbn, edition,loanid));

            }

        } catch (Exception e) {
            System.out.println(e);
        }
        System.out.println(users);
        return users;
    }
}
