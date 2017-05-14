package de.hdm_stuttgart.foreignbuddy.UtilityClasses;

import java.util.Comparator;

import de.hdm_stuttgart.foreignbuddy.Users.User;

/**
 * Created by Marc-JulianFleck on 14.05.17.
 */

public class SortEntfernung implements Comparator<User>{
    @Override
    public int compare(User u1, User u2) {
        return Double.compare(u1.getDistanceToMyUser(), u2.getDistanceToMyUser());
    }
}