package com.example.thedarkknight.enoticeboard;

/**
 * Created by The Dark Knight on 20-05-2018.
 */

public class Model {
    String name;
    String city;
    int images;

    public Model(String name, String city, int images) {
        this.name = name;
        this.city = city;
        this.images = images;
    }

    public String getName() {
        return this.name;
    }

    public String getCity() {
        return this.city;
    }

    public int getImages() {
        return this.images;
    }
}
