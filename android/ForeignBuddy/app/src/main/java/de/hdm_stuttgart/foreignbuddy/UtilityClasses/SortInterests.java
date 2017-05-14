package de.hdm_stuttgart.foreignbuddy.UtilityClasses;

import java.util.Comparator;

import de.hdm_stuttgart.foreignbuddy.Users.User;

/**
 * Created by Marc-JulianFleck on 14.05.17.
 */

public class SortInterests implements Comparator<User> {
    @Override
    public int compare(User u1, User u2) {
        return Integer.compare(u2.getNumberOfCommonInterest(), u1.getNumberOfCommonInterest());
    }
}