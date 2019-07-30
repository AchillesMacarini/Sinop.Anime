package com.teknestige.classes;


/**
 * Created by ravi on 26/09/17.
 */

public class Item {
    int id;
    String name;
    String description;
    String thumbnail;
    static String title;
    static int imageId;

    public Item() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public static String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public static int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId= imageId;
    }


}
