package com.example.library2;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class HelloController {
    private HelloApplication main;
    private Parent view3;

    //connect main class to controller
    public void setMain(HelloApplication main) {
        this.main = main;
    }
    @FXML
    private TextField idtxt;

    @FXML
    private PasswordField passwordtxt;

    @FXML
    private Button loginButton;

    @FXML
    private Button signupButton;

    @FXML
    private Button closeButton;

    @FXML
    private Label errorLabel;

/*    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }*/

    Connection con;
    PreparedStatement pst;
    ResultSet rs;

    @FXML
    public void exit(ActionEvent event) throws IOException {
        closeButton.setOnAction(e -> Platform.exit());
    }

    @FXML
    public void signup(ActionEvent event)throws IOException{
        Parent view3=FXMLLoader.load(getClass().getResource("signup-view.fxml"));
        Scene scene3=new Scene(view3);
        Stage window =(Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene3);
        window.show();
    }

    @FXML
    public void login(ActionEvent event) throws IOException {
        Connect_SQL connect = new Connect_SQL();
        try {
            Connection con = connect.ConnectDb();
            String id = idtxt.getText().trim();
            String password = passwordtxt.getText().trim();
            System.out.println(id);
            if (id.isEmpty() || password.isEmpty()) {
                errorLabel.setText("Please insert ID and password");
            } else {

                PreparedStatement ps = con.prepareStatement("select * from library2.admins where id=?"
                        + " and password=?");

                ps.setString(1, idtxt.getText().trim());
                ps.setString(2, passwordtxt.getText().trim());

                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("menu-view.fxml"));
                    view3 = loader.load();

                    MenuController menuController = loader.getController();
                    menuController.displayname(id);
                    //Parent view3=FXMLLoader.load(getClass().getResource("menu-view.fxml"));
                    Scene scene3=new Scene(view3);
                    Stage window =(Stage)((Node)event.getSource()).getScene().getWindow();
                    window.setScene(scene3);
                    window.show();
                } else {
                    errorLabel.setText("Invalid credentials. Please try again");
                }
            }
        } catch (Exception ex) {
            System.out.println("error" + ex.toString());
        }

    }

}