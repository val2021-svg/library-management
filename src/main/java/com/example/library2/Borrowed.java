package com.example.library2;

import javafx.beans.property.SimpleStringProperty;

public class Borrowed {
    private SimpleStringProperty title, author, isbn, keyword1, keyword2, keyword3, keyword4, keyword5;
    private int firstpub;

    public Borrowed(String title, String author, String isbn, String keyword1, String keyword2, String keyword3, String keyword4, String keyword5, int firstpub) {
        this.title = new SimpleStringProperty(title);
        this.author = new SimpleStringProperty(author);
        this.isbn = new SimpleStringProperty(isbn);
        this.keyword1 = new SimpleStringProperty(keyword1);
        this.keyword2 = new SimpleStringProperty(keyword2);
        this.keyword3 = new SimpleStringProperty(keyword3);
        this.keyword4 = new SimpleStringProperty(keyword4);
        this.keyword5 = new SimpleStringProperty(keyword5);
        this.firstpub = firstpub;
    }

    public String getTitle() {
        return title.get();
    }

    public void setTitle(String title) {
        this.title = new SimpleStringProperty(title);
    }

    public String getAuthor() {
        return author.get();
    }

    public void setAuthor(String author) {
        this.author = new SimpleStringProperty(author);
    }

    public String getIsbn() {
        return isbn.get();
    }

    public void setIsbn(String isbn) {
        this.isbn = new SimpleStringProperty(isbn);
    }

    public String getKeyword1() {
        return keyword1.get();
    }

    public void setKeyword1(String keyword1) {
        this.keyword1 = new SimpleStringProperty(keyword1);
    }

    public String getKeyword2() {
        return keyword2.get();
    }

    public void setKeyword2(String keyword2) {
        this.keyword2 = new SimpleStringProperty(keyword2);
    }

    public String getKeyword3() {
        return keyword3.get();
    }

    public void setKeyword3(String keyword3) {
        this.keyword3 = new SimpleStringProperty(keyword3);
    }

    public String getKeyword4() {
        return keyword4.get();
    }

    public void setKeyword4(String keyword4) {
        this.keyword4 = new SimpleStringProperty(keyword4);
    }

    public String getKeyword5() {
        return keyword5.get();
    }

    public void setKeyword5(String keyword5) {
        this.keyword5 = new SimpleStringProperty(keyword5);
    }

    public Integer getFirstpub() { return firstpub;}

    public void setFirstpub(int firstpub) {
        this.firstpub = firstpub;
    }

}
