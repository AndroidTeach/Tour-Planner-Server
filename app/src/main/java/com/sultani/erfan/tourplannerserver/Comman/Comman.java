package com.sultani.erfan.tourplannerserver.Comman;

import com.sultani.erfan.tourplannerserver.Model.User;

/**
 * Created by Erfan on 12/1/2017.
 */

public class Comman {
    public static User currentUser;

    public static final String UPDATE = "Update";

    public static final String DELETE = "Delete";

    public static final String USER_KEY = "User";
    public static final String PWD_KEY = "Password";

    public static final int PICK_IMAGE_REQUEST = 71;

    public static final String convertCodeStatus(String code)
    {
             if (code.equals("0"))
                 return "Progess";

            else if (code.equals("1"))
            return "Paid";
               else
                   return "Not Paid";


    }

}
