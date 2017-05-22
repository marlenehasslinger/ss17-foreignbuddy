package de.hdm_stuttgart.foreignbuddy.UtilityClasses;

import java.util.Comparator;

import de.hdm_stuttgart.foreignbuddy.Users.Match;
import de.hdm_stuttgart.foreignbuddy.Users.User;

/**
 * Created by Marc-JulianFleck on 14.05.17.
 */

public class SortInterests implements Comparator<Match> {
    @Override
    public int compare(Match m1, Match m2) {
        return Integer.compare(m2.getNumberOfCommonInterest(), m1.getNumberOfCommonInterest());
    }
}