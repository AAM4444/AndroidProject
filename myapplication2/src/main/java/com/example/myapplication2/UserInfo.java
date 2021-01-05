package com.example.myapplication2;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "UsersInfo")
public class UserInfo extends Model {

    @Column(name = "remoteId")
    public int remoteId;

    @Column(name = "email")
    public String email;

    @Column(name = "first_name")
    public String firstName;

    @Column(name = "last_name")
    public String lastName;

    @Column(name = "avatar")
    public String avatar;

    @Column(name = "from_page")
    public int fromPage;

    public UserInfo() {

        super();
        }


}

