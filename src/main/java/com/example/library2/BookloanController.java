package com.example.library2;

import java.util.*;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

//import javax.swing.*;
//import javax.swing.JOptionPane;

public class BookloanController {
    @FXML
    private TextField titletxt;

    @FXML
    private TextField keyword1txt;

    @FXML
    private TextField keyword2txt;

    @FXML
    private TextField keyword3txt;

    @FXML
    private TextField keyword4txt;

    @FXML
    private TextField keyword5txt;

    @FXML
    private TextField authornametxt;

    @FXML
    private TextField authorlastnametxt;

    @FXML
    private TextField isbntxt;

    @FXML
    private Label leftredLabel;

    @FXML
    private Label leftgreenLabel;

    @FXML
    private Label rightredLabel;

    @FXML
    private Label rightgreenLabel;

    @FXML
    private Label authorLabel;

    @FXML
    private Label editionLabel;

    @FXML
    private Button logoutButton;

    @FXML
    private Button returnButton;

    @FXML
    private Button searchButton;

    @FXML
    private Button loanButton;

    @FXML
    private Label nameLabel2;

    private String id2 = null;

    private boolean searchpressed = false;

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
    @FXML
    public void loan() {
        if(searchpressed==true){
            System.out.println("agora: "+id2);
            ResultSet rs = null, rs1, rs2, rs3, rs4 = null, rs5 = null, rs6 = null, rs7, rs8;
            PreparedStatement pst = null, pst1, pst2, pst3, pst7, pst4 = null, pst5 = null, pst6 = null, pst8;
            Connect_SQL connect = new Connect_SQL();

            String isbn = isbntxt.getText();

            String sql1 = "SELECT categories.maxday, categories.maxbook from categories inner join admins on categories.category = admins.category where admins.id = '" + id2 + "'";
            String sql2 = "SELECT available.bookid from available inner join books on available.bookid = books.bookid inner join editions on books.ISBN = editions.ISBN where editions.ISBN = '" + isbn + "' LIMIT 1";
            String sql3 = "SELECT COUNT(*) as qtd_books from currentloan inner join admins on admins.id = currentloan.id where admins.id = '" + id2 + "'";
            String sql4 = "SET FOREIGN_KEY_CHECKS = 0";
            String sql5 = "DELETE from available where bookid = ?";
            String sql6 = "SET FOREIGN_KEY_CHECKS = 1";
            String sql7 = "SELECT currentloan.idloan from currentloan inner join admins on admins.id = currentloan.id where admins.id = '" + id2 + "' ORDER BY currentloan.idloan DESC LIMIT 1";
            String sql8 = "SELECT id from redlist where id = '" + id2 + "'";
            String sql = "insert into library2.currentloan (id, bookid, loandate, duedate) value (?,?,now(),date_add(now(), interval ? day))";

            try {
                Connection con = Connect_SQL.ConnectDb();
                pst1 = con.prepareStatement(sql1);
                rs1 = pst1.executeQuery();
                pst2 = con.prepareStatement(sql2);
                rs2 = pst2.executeQuery();
                pst3 = con.prepareStatement(sql3);
                rs3 = pst3.executeQuery();
                pst8 = con.prepareStatement(sql8);
                rs8 = pst8.executeQuery();
                if (rs1.next() && rs2.next()) {
                    int max_days = rs1.getInt("categories.maxday");
                    System.out.println("Max days: " + max_days);
                    int max_books = rs1.getInt("categories.maxbook");
                    System.out.println("Max books: " + max_books);
                    int book_id = rs2.getInt("available.bookid");
                    System.out.println("Book id: " + book_id);
                    if (rs3.next()) {
                        int qtd_books = rs3.getInt("qtd_books");
                        System.out.println("qtd books: " + qtd_books);
                        if (qtd_books < max_books) {
                            System.out.println("didn't reach the limit");
                            if (!rs8.next()) {
                                try {
                                    pst = con.prepareStatement(sql);


                                    pst3 = con.prepareStatement(sql3);
                                    rs3 = pst3.executeQuery();
                                    pst.setString(1, id2);
                                    pst.setInt(2, book_id);
                                    pst.setInt(3, max_days);
                                    System.out.println(pst);

                                    pst.execute();
                                    pst.close();

                                    pst4 = con.prepareStatement(sql4);
                                    pst4.execute();
                                    pst4.close();

                                    pst5 = con.prepareStatement(sql5);
                                    pst5.setInt(1, book_id);
                                    pst5.execute();
                                    pst5.close();

                                    pst6 = con.prepareStatement(sql6);
                                    pst6.execute();
                                    pst6.close();

                                    pst7 = con.prepareStatement(sql7);
                                    rs7 = pst7.executeQuery();
                                    if (rs7.next()) {
                                        int id_loan = rs7.getInt("currentloan.idloan");
                                        rightgreenLabel.setText("Book loaned Successfully! \nLoan ID: " + id_loan);
                                        rightredLabel.setText("");
                                        leftgreenLabel.setText("");
                                        leftredLabel.setText("");
                                    }
                                } catch (Exception e) {
                                    System.out.println(e);
                                }
                            }else{
                                rightredLabel.setText("User on red list can't borrow books !");
                                rightgreenLabel.setText("");
                                leftgreenLabel.setText("");
                                leftredLabel.setText("");
                            }
                        } else {
                            rightredLabel.setText("The book limit was reached !");
                            rightgreenLabel.setText("");
                            leftgreenLabel.setText("");
                            leftredLabel.setText("");
                        }
                    }else{
                        rightredLabel.setText("");
                        rightgreenLabel.setText("");
                        leftgreenLabel.setText("");
                        leftredLabel.setText("");
                    }
                }else {
                rightredLabel.setText("This book is not available");
                rightgreenLabel.setText("");
                leftgreenLabel.setText("");
                leftredLabel.setText("");
                }
            } catch (Exception e) {
                System.out.println(e);
            }

        }else{
            rightredLabel.setText("Search for the book first");
            rightgreenLabel.setText("");
            leftgreenLabel.setText("");
            leftredLabel.setText("");
        }
    }

    @FXML
    public void search(ActionEvent event) throws IOException {
        rightredLabel.setText("");
        rightgreenLabel.setText("");
        leftgreenLabel.setText("");
        leftredLabel.setText("");
        ResultSet rs = null;
        PreparedStatement pst = null;
        Connect_SQL connect = new Connect_SQL();

        String title = titletxt.getText();
        String keyword1 = keyword1txt.getText();
        String keyword2 = keyword2txt.getText();
        String keyword3 = keyword3txt.getText();
        String keyword4 = keyword4txt.getText();
        String keyword5 = keyword5txt.getText();

        String authorname = authornametxt.getText();
        String authorlastname = authorlastnametxt.getText();

        String isbn = isbntxt.getText();

        if (title.isEmpty() || keyword1.isEmpty() || authorname.isEmpty() || authorlastname.isEmpty() || isbn.isEmpty()) {
            rightredLabel.setText("Please complete all the fills");
            rightgreenLabel.setText("");
            leftgreenLabel.setText("");
            leftredLabel.setText("");
        } else {
            String sql = "select available.bookid, books.title, books.firstpub, books.keyword1, books.keyword2, books.keyword3, books.keyword4, books.keyword5 from library2.available inner join books on available.bookid = books.bookid where title = ? and ((keyword1 like ? or keyword2 like ? or keyword3 like ? or keyword4 like ? or keyword5 like ?) or (keyword1 like ? or keyword2 like ? or keyword3 like ? or keyword4 like ? or keyword5 like ?) or (keyword1 like ? or keyword2 like ? or keyword3 like ? or keyword4 like ? or keyword5 like ?) or (keyword1 like ? or keyword2 like ? or keyword3 like ? or keyword4 like ? or keyword5 like ?) or (keyword1 like ? or keyword2 like ? or keyword3 like ? or keyword4 like ? or keyword5 like ?))";
            //select title, firstpub, keyword1, keyword2 from library2.books where keyword1 like 'fiction' or keyword1 like 'adventure' or keyword2 like 'action';
            try {
                Connection con = Connect_SQL.ConnectDb();
                pst = con.prepareStatement(sql);
                pst.setString(1, title);
                pst.setString(2, keyword1);
                pst.setString(3, keyword1);
                pst.setString(4, keyword1);
                pst.setString(5, keyword1);
                pst.setString(6, keyword1);
                pst.setString(7, keyword2);
                pst.setString(8, keyword2);
                pst.setString(9, keyword2);
                pst.setString(10, keyword2);
                pst.setString(11, keyword2);
                pst.setString(12, keyword3);
                pst.setString(13, keyword3);
                pst.setString(14, keyword3);
                pst.setString(15, keyword3);
                pst.setString(16, keyword3);
                pst.setString(17, keyword4);
                pst.setString(18, keyword4);
                pst.setString(19, keyword4);
                pst.setString(20, keyword4);
                pst.setString(21, keyword4);
                pst.setString(22, keyword5);
                pst.setString(23, keyword5);
                pst.setString(24, keyword5);
                pst.setString(25, keyword5);
                pst.setString(26, keyword5);


                rs = pst.executeQuery();

                String sql1 = "select library2.available.bookid, library2.books.title, library2.editions.edition, library2.editions.editionyear from library2.available inner join books on available.bookid = books.bookid inner join library2.authors on library2.books.authorid = library2.authors.authorid inner join library2.editions on library2.books.ISBN = library2.editions.ISBN where library2.authors.authorname = ? and library2.authors.authorlastname = ? and library2.books.title = ? and library2.editions.ISBN = ?";
                pst = con.prepareStatement(sql1);
                pst.setString(1, authorname);
                pst.setString(2, authorlastname);
                pst.setString(3, title);
                pst.setString(4, isbn);
                ResultSet rs1 = pst.executeQuery();

                String sql2 = "select COUNT(*) as rowcount from library2.available inner join books on available.bookid = books.bookid inner join library2.authors on library2.books.authorid = library2.authors.authorid inner join library2.editions on library2.books.ISBN = library2.editions.ISBN where library2.authors.authorname = ? and library2.authors.authorlastname = ? and library2.books.title = ? and library2.editions.ISBN = ?";
                pst = con.prepareStatement(sql2);
                pst.setString(1, authorname);
                pst.setString(2, authorlastname);
                pst.setString(3, title);
                pst.setString(4, isbn);
                ResultSet rs2 = pst.executeQuery();


                if (rs.next()) {
                    int firstpub = rs.getInt("firstpub");
                    rightgreenLabel.setText("Title available: " +title+ "\nPublished: " + firstpub);
                    System.out.println("livro existe, checar edição e autor");
                    searchpressed = false;
                    rs.close();

                    if (rs1.next()) {
                        System.out.println("livro existe e edição e autor tb");
                        searchpressed = true;
                        if (rs2.next()) {
                            int count_editions = rs2.getInt("rowcount");
                            leftgreenLabel.setText("Copies available: " + count_editions);
                            rs2.close();
                        }
                        rs1.close();
                        pst.close();
                    } else{
                        leftredLabel.setText("Edition/Author is not available, \nchoose another");
                        rightgreenLabel.setText("");
                        leftgreenLabel.setText("");
                        rightredLabel.setText("");
                        System.out.println("livro existe mas edição e/ou autor n");
                        searchpressed = false;
                    }
                } else {
                    rightredLabel.setText("Title is not available, choose another");
                    rightgreenLabel.setText("");
                    leftgreenLabel.setText("");
                    leftredLabel.setText("");
                    System.out.println("nem livro nem edição/autor existem");
                    searchpressed = false;
                    //JOptionPane.showMessageDialog(null,"The book does not exist, check the book ID well");
                }
            } catch (Exception e) {
                System.out.println("error" + e);
            } finally {
                try {
                    rs.close();
                    pst.close();
                } catch (Exception e) {

                }
            }
        }
    }
}
