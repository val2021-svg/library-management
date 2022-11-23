package com.example.library2;

import javafx.beans.property.SimpleStringProperty;

public class Users {
    private SimpleStringProperty username, userlastname, title, issuedate, isbn;
    private int userid,edition,loanid;

    public Users(int userid, String username, String userlastname, String title, String issuedate, String isbn, int edition, int loanid) {
        this.title = new SimpleStringProperty(title);
        this.username = new SimpleStringProperty(username);
        this.isbn = new SimpleStringProperty(isbn);
        this.userlastname = new SimpleStringProperty(userlastname);
        this.issuedate = new SimpleStringProperty(issuedate);
        this.userid = userid;
        this.edition = edition;
        this.loanid = loanid;
    }

    public String getTitle() {
        return title.get();
    }

    public void setTitle(String title) {
        this.title = new SimpleStringProperty(title);
    }

    public String getUsername() {
        return username.get();
    }

    public void setUsername(String username) {
        this.username = new SimpleStringProperty(username);
    }

    public String getIsbn() {
        return isbn.get();
    }

    public void setIsbn(String isbn) {
        this.isbn = new SimpleStringProperty(isbn);
    }

    public String getUserlastname() {
        return userlastname.get();
    }

    public void setUserlastname(String userlastname) {
        this.userlastname = new SimpleStringProperty(userlastname);
    }

    public String getIssuedate() {
        return issuedate.get();
    }

    public void setIssuedate(String issuedate) {
        this.issuedate = new SimpleStringProperty(issuedate);
    }

    public Integer getUserid() { return userid;}

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public Integer getEdition() { return edition;}

    public void setEdition(int edition) {
        this.edition = edition;
    }

    public Integer getLoanid() { return loanid;}

    public void setLoanid(int loanid) {
        this.loanid = loanid;
    }

}
