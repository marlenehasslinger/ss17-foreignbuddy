package de.hdm_stuttgart.foreignbuddy.Users;

import com.google.firebase.auth.FirebaseAuth;
import java.util.ArrayList;

/**
 * Created by Marc-JulianFleck on 29.03.17.
 */

public class User {

    public String userID;
    public String username;
    public String email;
    public String nativeLanguage;
    public String location;
    //private Location lastLocation = null;
    //private Boolean[] interests;

    public User() {

    }

    //Constucture
    public User (String userID, String username, String email, String nativeLanguage, String location) {
        this.userID = userID;
        this.username = username;
        this.email = email;
        this.nativeLanguage = nativeLanguage;
        this.location = location;
    }




}
