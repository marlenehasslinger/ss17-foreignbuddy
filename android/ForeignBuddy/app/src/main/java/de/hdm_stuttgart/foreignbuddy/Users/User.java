package de.hdm_stuttgart.foreignbuddy.Users;

/**
 * Created by Marc-JulianFleck on 29.03.17.
 */

public class User {

    private String name;
    private String surname;
    private String nickname;
    private String email;
    private String language;
    private String location;
    //nativeLanguage
    //lastLocation
    //interests
    //languages

    //Constucture
    public User (String name, String location, String language) {
        this.name = name;
        this.location = location;
        this.language = language;
    }


    //Getter and Setter
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
