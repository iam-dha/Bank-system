package model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Notification {
    @JsonProperty("title")
    private String title;
    @JsonProperty("message")
    private String message;
    @JsonProperty("dateTime")
    private String dateTime;
    @JsonProperty("id")
    private int id;
    @JsonProperty("imageUrl")
    private String imageUrl;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
