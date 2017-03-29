package de.hdm_stuttgart.foreignbuddy;

/**
 * Created by Marc-JulianFleck on 29.03.17.
 */

public class User {

    private String name;
    private String location;
    private String language;

    public User (String name, String location, String language) {
        super();

        this.name = name;
        this.location = location;
        this.language = language;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
