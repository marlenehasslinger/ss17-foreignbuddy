package de.hdm_stuttgart.foreignbuddy.Users;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Marc-JulianFleck on 21.05.17.
 */

public class Match extends User implements Comparable<Match>{

    private List<String> commonInterest = new ArrayList<>();
    private int numberOfCommonInterest;
    private Double distanceToMyUser;

    public Match(){};
    public Match(String userID) {
        this.userID = userID;
    }


    public void setCommonInterests(User myUser){
        numberOfCommonInterest = 0;

        for (Iterator it = interests.keySet().iterator(); it.hasNext(); ){
            String interest = (String) it.next();
            Boolean value = interests.get(interest);
            if (value == true && myUser.interests.get(interest) == true) {
                commonInterest.add(interest);
                numberOfCommonInterest++;
            }
        }
    }
    public List<String> getCommonInterest() {
        return commonInterest;
    }
    public int getNumberOfCommonInterest() {
        return numberOfCommonInterest;
    }
    public Double getDistanceToMyUser() {
        return distanceToMyUser;
    }
    public void setDistanceToMyUser(Double distanceToMyUser) {
        this.distanceToMyUser = distanceToMyUser;
    }

    @Override
    public int compareTo(Match match) {
        return Integer.compare(match.getNumberOfCommonInterest(), numberOfCommonInterest);
    }


}
