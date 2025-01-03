package org.example.Controller;

import java.util.ArrayList;
import java.util.List;
import org.example.Models.User;

public class Users {
    public static final List<User> UserList = new ArrayList<>();

    public void adduser(User users){
        UserList.add(users);
    }

}
