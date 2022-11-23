package com.example.library2;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;


public class RedlistController implements Initializable{
    @FXML
    private TableView<Redlist> redlistTable;

    @FXML
    private TableColumn<Redlist, Integer> userid;

    @FXML
    private TableColumn<Redlist, String> username;

    @FXML
    private TableColumn<Redlist, String> userlastname;

    @FXML
    private Label redlistLabel;

    @FXML
    private Button logoutButton;

    @FXML
    private Button returnButton;

    @FXML
    private Label nameLabel2;

    private String id2 = null;

    private String category = null;

    public void displayname2(String id){
        id2 = id;
        System.out.println("marche:"+id);
        userid.setCellValueFactory(new PropertyValueFactory<Redlist, Integer>("userid"));
        username.setCellValueFactory(new PropertyValueFactory<Redlist, String>("username"));
        userlastname.setCellValueFactory(new PropertyValueFactory<Redlist, String>("userlastname"));
        System.out.println("serase"+id);
        redlistTable.setItems(getRedlist());
        //BookcontrolTable.getColumns().addAll(title, author, firstpub, isbn, keyword1,keyword2,keyword3,keyword4,keyword5);


        System.out.println("displqynqçe2");

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

    public void redlist(ActionEvent event) throws IOException
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
        System.out.println("initialize");
        //RedlistController sc = new RedlistController(id2);

    }
    public ObservableList<Redlist> getRedlist()
    {
        System.out.println("id2" + id2);
        ObservableList<Redlist> redlist = FXCollections.observableArrayList();
        ResultSet rs;
        PreparedStatement pst;
        Connect_SQL connect = new Connect_SQL();
        System.out.println("pepe"+id2);
        String sql = "SELECT id, name, lastname from redlist where id = '" + id2 + "'";
        try {
            Connection con = Connect_SQL.ConnectDb();

            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            System.out.println(id2);
            if (rs.next()) {
                int userid = rs.getInt("id");
                System.out.println("user id: "+userid);
                String username = rs.getString("name");
                System.out.println("username: "+username);
                String userlastname = rs.getString("lastname");
                System.out.println("userlastname: "+userlastname);
                redlist.add(new Redlist(userid, username, userlastname));
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        System.out.println(redlist);
        return redlist;
    }
}
