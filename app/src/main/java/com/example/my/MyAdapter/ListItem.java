package com.example.my.MyAdapter;

import java.io.Serializable;

public class ListItem implements Serializable {
    private String title;
    private String dick;
    private String uri = "empty";
    private int id = 0;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDick() {
        return dick;
    }

    public void setDick(String dick) {
        this.dick = dick;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
