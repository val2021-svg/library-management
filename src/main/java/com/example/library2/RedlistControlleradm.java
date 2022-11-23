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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;


public class RedlistControlleradm implements Initializable{

    ResultSet rs,rs2 = null;
    PreparedStatement ps, ps2=null;
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
    private Button addButton;

    @FXML
    private Button removeButton;

    @FXML
    private TextField useridtxt;

    @FXML
    private TextField usernametxt;

    @FXML
    private TextField userlastnametxt;

    @FXML
    private Label nameLabel2;

    @FXML
    private Label errorLabel;

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
    public void add(ActionEvent event) throws IOException {
        Connect_SQL connect = new Connect_SQL();

        try {

            Connection con = Connect_SQL.ConnectDb();

            String userid = useridtxt.getText().trim();
            String username = usernametxt.getText().trim();
            String userlastname = userlastnametxt.getText().trim();

            if (userid.isEmpty() || username.isEmpty() || userlastname.isEmpty()) {
                errorLabel.setText("Please complete all the\n fields");
            } else {
                errorLabel.setText("");
                String sql = "select * from library2.admins where id = ? and name=? and lastname=?";
                ps = con.prepareStatement(sql);
                ps.setString(1, userid);
                ps.setString(2, username);
                ps.setString(3, userlastname);

                ResultSet rs = ps.executeQuery();
                if (!rs.next()) {
                    errorLabel.setText("This user is not registered\n up in the library");
                } else {
                    errorLabel.setText("");
                    Redlist redlist = new Redlist(Integer.parseInt(useridtxt.getText()),
                            usernametxt.getText(),
                            userlastnametxt.getText());
                    ObservableList<Redlist> redlistadd = redlistTable.getItems();
                    redlistadd.add(redlist);
                    redlistTable.setItems(redlistadd);

                    String sql2 = "insert into library2.redlist (id,name,lastname) values(?,?,?)";
                    ps2 = con.prepareStatement(sql2);

                    ps2.setString(1, useridtxt.getText().trim());
                    ps2.setString(2, usernametxt.getText().trim());
                    ps2.setString(3, userlastnametxt.getText().trim());

                    ps2.execute();

                    //errorsignupLabel.setText("Account successfully registered");
                }
            }
        }catch (Exception e) {
            System.out.println("error" + e);
        }
    }

    @FXML
    public void remove(ActionEvent event) throws IOException {
        //int selectedID = redlistTable.getSelectionModel().getSelectedIndex();

        //System.out.println("index: "+selectedID);

        Redlist redlistremove = redlistTable.getSelectionModel().getSelectedItem();
        int removeid = redlistremove.getUserid();
        System.out.println("remover: "+removeid);
        if (redlistremove != null){
            ResultSet rs = null;
            PreparedStatement pst = null;
            Connect_SQL connect = new Connect_SQL();

            String sql = "DELETE from redlist where id = ?";
            try {
                Connection con = Connect_SQL.ConnectDb();
                pst = con.prepareStatement(sql);
                pst.setInt(1, removeid);
                pst.execute();
                pst.close();
            }catch (Exception e) {
                System.out.println(e);
            }
        }
        redlistTable.getItems().removeAll(redlistTable.getSelectionModel().getSelectedItem());
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
        userid.setCellValueFactory(new PropertyValueFactory<Redlist, Integer>("userid"));
        username.setCellValueFactory(new PropertyValueFactory<Redlist, String>("username"));
        userlastname.setCellValueFactory(new PropertyValueFactory<Redlist, String>("userlastname"));

        redlistTable.setItems(getRedlist());
        //BookcontrolTable.getColumns().addAll(title, author, firstpub, isbn, keyword1,keyword2,keyword3,keyword4,keyword5);
    }
    public ObservableList<Redlist> getRedlist()
    {
        ObservableList<Redlist> redlist = FXCollections.observableArrayList();
        ResultSet rs;
        PreparedStatement pst;
        Connect_SQL connect = new Connect_SQL();
        String sql = "SELECT id, name, lastname from redlist";
        try {
            Connection con = Connect_SQL.ConnectDb();

            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();

            while (rs.next()) {
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
