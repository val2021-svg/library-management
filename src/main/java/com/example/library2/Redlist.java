package com.example.library2;

import javafx.beans.property.SimpleStringProperty;

public class Redlist {
    private SimpleStringProperty username, userlastname;
    private int userid;

    public Redlist(int userid, String username, String userlastname) {
        this.username = new SimpleStringProperty(username);
        this.userlastname = new SimpleStringProperty(userlastname);
        this.userid = userid;
    }

    public String getUsername() {
        return username.get();
    }

    public void setUsername(String username) {
        this.username = new SimpleStringProperty(username);
    }

    public String getUserlastname() {
        return userlastname.get();
    }

    public void setUserlastname(String userlastname) {
        this.userlastname = new SimpleStringProperty(userlastname);
    }

    public int getUserid() { return userid;}

    public void setUserid(int userid) {
        this.userid = userid;
    }

}
