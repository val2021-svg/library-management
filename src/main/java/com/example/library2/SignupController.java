package com.example.library2;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SignupController implements Initializable{

    ResultSet rs;
    PreparedStatement ps;

    @FXML
    private TextField nametxt;

    @FXML
    private TextField lastnametxt;

    @FXML
    private TextField emailtxt;

    @FXML
    private TextField idtxt;

    @FXML
    private PasswordField passwordtxt;

    @FXML
    private Button signupButton;

    @FXML
    private Label signupLabel;

    @FXML
    private Label errorsignupLabel;

    @FXML
    private Label correctsignupLabel;

    @FXML
    private Button returnButton;

    @FXML
    public void returning(ActionEvent event) throws IOException {
        Parent view3 = FXMLLoader.load(getClass().getResource("hello-view.fxml"));
        Scene scene3 = new Scene(view3);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene3);
        window.show();
    }

    @FXML
    void createacc(ActionEvent event) throws IOException {
        Connect_SQL connect = new Connect_SQL();

        try {

            Connection con = Connect_SQL.ConnectDb();

            String name = nametxt.getText().trim();
            String lastname = lastnametxt.getText().trim();
            String id = idtxt.getText().trim();
            String email = emailtxt.getText().trim();
            String password = passwordtxt.getText().trim();
            String category;

            if (email.contains("@student-cs")){
                category = "user";
            } else {
                if (email.contains("@centralesupelec")){
                    category = "manager";
                }
                else{
                    category = null;
                    errorsignupLabel.setText("The email address is not valid");
                    correctsignupLabel.setText("");
                }
            }

            if (email.isEmpty() || password.isEmpty() || name.isEmpty() || lastname.isEmpty() || id.isEmpty()) {
                errorsignupLabel.setText("Please complete all the fields");
                correctsignupLabel.setText("");
            } else {
                if (password.length() < 4) {
                    errorsignupLabel.setText("Password is too weak, please choose at least 4 characters");
                    correctsignupLabel.setText("");
                } else {

                    String sql = "select * from library2.admins where id = ?";
                    ps = con.prepareStatement(sql);
                    ps.setString(1, id);

                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        errorsignupLabel.setText("This ID already has an account, try to log in");
                        correctsignupLabel.setText("");
                    } else {

                        String sql1 = "select maxbook from library2.categories where category = ?";
                        ps = con.prepareStatement(sql1);
                        ps.setString(1, category);

                        ResultSet rs1 = ps.executeQuery();

                        String sql3 = "select maxday from library2.categories where category = ?";
                        ps = con.prepareStatement(sql3);
                        ps.setString(1, category);

                        ResultSet rs3 = ps.executeQuery();

                        String sql2 = "insert into library2.admins (name,lastname,email,id,password,category,maxbook,maxday) values(?,?,?,?,?,?,?,?)";
                        ps = con.prepareStatement(sql2);

                        ps.setString(1, nametxt.getText().trim());
                        ps.setString(2, lastnametxt.getText().trim());
                        ps.setString(3, emailtxt.getText().trim());
                        ps.setString(4, idtxt.getText().trim());
                        ps.setString(5, passwordtxt.getText().trim());
                        ps.setString(6, category);

                        if (rs1.next()) {
                            int maxbookint = rs1.getInt("maxbook");
                            ps.setInt(7, maxbookint);
                            System.out.println(maxbookint);
                        }
                        if (rs3.next()) {
                            int maxdayint = rs3.getInt("maxday");
                            ps.setInt(8, maxdayint);
                            System.out.println(maxdayint);
                        }
                        ps.execute();

                        errorsignupLabel.setText("");
                        correctsignupLabel.setText("Account successfully registered");

                    }
                }
            }
        } catch (Exception e) {
            System.out.println("error" + e);
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
}
