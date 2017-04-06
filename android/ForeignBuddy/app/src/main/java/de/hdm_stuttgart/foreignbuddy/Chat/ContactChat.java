package de.hdm_stuttgart.foreignbuddy.Chat;

import android.widget.ImageView;

/**
 * Created by Jan-Niklas on 04.04.17.
 */

public class ContactChat {

    public String contactname;

    public ImageView contactPicture;


    public ContactChat(String contactname, ImageView contactPicture){
        this.contactname = contactname;
        this.contactPicture = contactPicture;
    }



    public String getContactname() {
        return contactname;
    }

    public void setContactname(String contactname) {
        this.contactname = contactname;
    }

    public ImageView getContactPicture() {
        return contactPicture;
    }

    public void setContactPicture(ImageView contactPicture) {
        this.contactPicture = contactPicture;
    }






}
