package com.example.myapplication2;

//import
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "users", indices = {@Index(value = {"first_name", "last_name"}, unique = true)})
public class User {

    @PrimaryKey
    @ColumnInfo(name = "remoteId")
    public int remoteId;

    public String email;

    @ColumnInfo(name = "first_name")
    public String firstName;

    @ColumnInfo(name = "last_name")
    public String lastName;

    public String avatar;

    @ColumnInfo(name = "from_page")
    public int fromPage;

    public User() {

    }

}
