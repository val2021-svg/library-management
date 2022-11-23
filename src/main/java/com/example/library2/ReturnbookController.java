package com.example.library2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.Date;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ReturnbookController {
    @FXML
    private TextField loanidtxt;

    @FXML
    private Label errorLabel;

    @FXML
    private Label rightLabel;

    @FXML
    private Label returnbookLabel;

    @FXML
    private Button logoutButton;

    @FXML
    private Button returnButton;

    @FXML
    private Button returnbookButton;

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
        System.out.println("aqui"+id2);
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

    @FXML
    public void returnbookaction(ActionEvent event) throws IOException {
        System.out.println(id2);
        String loanid = loanidtxt.getText();

        ResultSet rs = null, rs2 = null, rs1, rs3, rs4, rs5, rs6 = null;
        PreparedStatement pst = null, pst2 = null, pst1, pst3, pst4, pst5, pst6 = null;
        Connect_SQL connect = new Connect_SQL();

        if (loanid.isEmpty()) {
            rightLabel.setText("");
            errorLabel.setText("Fill in the Loan ID");
        } else{
            String sql1 = "select bookid from currentloan where idloan = '" + loanid + "' and id = '" + id2 + "' ";
            String sql2 = "insert into available (bookid) values (?)";
            String sql3 = "SELECT admins.id, admins.name, admins.lastname,categories.maxday from categories inner join admins on categories.category = admins.category where admins.id = '" + id2 + "'";
            String sql4 = "SELECT loandate from currentloan where idloan = '" + loanid + "' ";
            String sql5 = "SELECT DATEDIFF(NOW(),?)";
            String sql6 = "insert into library2.redlist (id,name,lastname) values(?,?,?)";
            String sql = "delete from currentloan where idloan = '" + loanid + "' ";
            try {
                Connection con = Connect_SQL.ConnectDb();
                pst1 = con.prepareStatement(sql1);
                rs1 = pst1.executeQuery();

                System.out.println("idloan: "+loanid);
                if (rs1.next()) {
                    int book_id = rs1.getInt("bookid");

                    System.out.println("deletou da currentloan");

                    pst3 = con.prepareStatement(sql3);
                    rs3 = pst3.executeQuery();

                    pst4 = con.prepareStatement(sql4);
                    rs4 = pst4.executeQuery();

                    if (rs3.next()) {
                        System.out.println("rs4 et rs3");
                        int max_days = rs3.getInt("categories.maxday");
                        int userid = rs3.getInt("admins.id");
                        String username = rs3.getString("admins.name");
                        String userlastname = rs3.getString("admins.lastname");
                        System.out.println("max days: "+max_days);
                        if (rs4.next()){
                            System.out.println("entrei rs4");
                            Timestamp loandate = rs4.getTimestamp("loandate");
                            System.out.println("loandate: "+loandate);

                            pst5 = con.prepareStatement(sql5);

                            pst5.setTimestamp(1, loandate);
                            rs5 = pst5.executeQuery();

                            pst = con.prepareStatement(sql);
                            pst.execute();
                            pst.close();

                            pst2 = con.prepareStatement(sql2);
                            pst2.setInt(1, book_id);
                            pst2.execute();
                            pst2.close();

                            if (rs5.next()){
                                int qtd_days = rs5.getInt(1);
                                if (qtd_days <= max_days) {
                                    rightLabel.setText("The book was successfully returned");
                                    errorLabel.setText("");
                                    System.out.println("The book was successfully returned");
                                }else{
                                    rightLabel.setText("");
                                    int late_days = -max_days+qtd_days;
                                    errorLabel.setText("The book was returned after " +late_days+ " days \nYou will be added to the Red list");
                                    System.out.println("The book was returned after " +late_days+ " days");
                                    pst6 = con.prepareStatement(sql6);

                                    pst6.setInt(1, userid);
                                    pst6.setString(2, username);
                                    pst6.setString(3, userlastname);

                                    pst6.execute();
                                }
                            }
                        }

                    }
                }else{
                    rightLabel.setText("");
                    errorLabel.setText("This loan ID is incorrect");
                }
            } catch (Exception e) {
                System.out.println(e);
                e.printStackTrace();
            }
        }
    }
}
