package com.example.gupta.mdbsocial;

import java.io.Serializable;

/**
 * Created by Gupta on 9/25/2017.
 */

public class Message implements Serializable {
    String email, url, interested, name, description, date, people;
    Long timestamp;



    // Sets all necessary info from message
    public Message(String email, String url,String interested, String name, String description, String date, Long timestamp) {
        this.email = email;
        this.url = url;
        this.interested = interested;
        this.name = name;
        this.description = description;
        this.date = date;
        this.timestamp = timestamp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getInterested() {
        return interested;
    }

    public void setInterested(String interested) {
        this.interested = interested;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
