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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class MenuController implements Initializable {
    private BookcontrolController bookcontrolController;

    private Parent view6;
    @FXML
    private Button logoutButton;

    @FXML
    private Button bookloanButton;

    @FXML
    private Button bookcontrolButton;

    @FXML
    private Button borrowedButton;

    @FXML
    private Button returnbookButton;

    @FXML
    private Button usercontrolButton;

    @FXML
    private Button redlistButton;

    @FXML
    private Label nameLabel;

    private String id = null;
    private String category = null;

    public void displayname(String id){
        this.id =  id;
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
                nameLabel.setText("ID: "+id+"\n"+category);
            }
        }catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }

    @FXML
    void logout(ActionEvent event) throws IOException {
        Parent view6=FXMLLoader.load(getClass().getResource("hello-view.fxml"));
        Scene scene6=new Scene(view6);
        Stage window =(Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene6);
        window.show();
    }

    @FXML
    void bookloan(ActionEvent event) throws IOException {
        System.out.println("Mon id c'est: "+id);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("bookloan-view.fxml"));
        Parent view6 = loader.load();

        BookloanController bookloanController = loader.getController();
        bookloanController.displayname2(id);

        //Parent view6=FXMLLoader.load(getClass().getResource("bookloan-view.fxml"));
        Scene scene6=new Scene(view6);
        Stage window =(Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene6);
        window.show();

    }

    public void bookcontrol(ActionEvent event) throws IOException {
        //Parent tableViewParent = FXMLLoader.load(getClass().getResource("bookcontrol-view.fxml"));
        FXMLLoader loader = new FXMLLoader(getClass().getResource("bookcontrol-view.fxml"));
        Parent view6 = loader.load();

        BookcontrolController bookcontrolController = loader.getController();
        bookcontrolController.displayname2(id);

        Scene tableViewScene = new Scene(view6);

        //This line gets the Stage information
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

        window.setScene(tableViewScene);
        window.show();
    }

    public void borrowed(ActionEvent event) throws IOException {
        //Parent tableViewParent = FXMLLoader.load(getClass().getResource("bookcontrol-view.fxml"));

        FXMLLoader loader = new FXMLLoader(getClass().getResource("borrowed-view.fxml"));
        Parent view6 = loader.load();

        BorrowedController borrowedController = loader.getController();
        borrowedController.displayname2(id);

        Scene tableViewScene = new Scene(view6);

        //This line gets the Stage information
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

        window.setScene(tableViewScene);
        window.show();
    }

    @FXML
    void usercontrol(ActionEvent event) throws IOException {
        if (category.equals("user")){
            //Parent tableViewParent = FXMLLoader.load(getClass().getResource("bookcontrol-view.fxml"));
            FXMLLoader loader = new FXMLLoader(getClass().getResource("usercontrol-view.fxml"));
            Parent view6 = loader.load();

            UsercontrolController usercontrolController = loader.getController();
            usercontrolController.displayname2(id);

            Scene tableViewScene = new Scene(view6);

            //This line gets the Stage information
            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

            window.setScene(tableViewScene);
            window.show();
        }else if (category.equals("manager")){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("usercontroladm-view.fxml"));
            Parent view6 = loader.load();

            UsercontrolControlleradm usercontrolControlleradm = loader.getController();
            usercontrolControlleradm.displayname2(id);

            Scene tableViewScene = new Scene(view6);

            //This line gets the Stage information
            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

            window.setScene(tableViewScene);
            window.show();
        }

    }

    @FXML
    void returnbook(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("returnbook-view.fxml"));
        Parent view6 = loader.load();

        ReturnbookController returnbookController = loader.getController();
        returnbookController.displayname2(id);

        //Parent view6=FXMLLoader.load(getClass().getResource("returnbook-view.fxml"));
        Scene scene6=new Scene(view6);
        Stage window =(Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene6);
        window.show();

    }

    @FXML
    void redlist(ActionEvent event) throws IOException {
        //Parent tableViewParent = FXMLLoader.load(getClass().getResource("bookcontrol-view.fxml"));
        if (category.equals("user")){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("redlist-view.fxml"));
            Parent view6 = loader.load();

            RedlistController redlistController = loader.getController();
            redlistController.displayname2(id);

            Scene tableViewScene = new Scene(view6);

            //This line gets the Stage information
            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

            window.setScene(tableViewScene);
            window.show();
        }else if (category.equals("manager")){
            System.out.println("Entrando na redlistadm");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("redlistadm-view.fxml"));
            Parent view6 = loader.load();

            RedlistControlleradm redlistControlleradm = loader.getController();


            Scene tableViewScene = new Scene(view6);
            redlistControlleradm.displayname2(id);

            //This line gets the Stage information
            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

            window.setScene(tableViewScene);
            window.show();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }
}
