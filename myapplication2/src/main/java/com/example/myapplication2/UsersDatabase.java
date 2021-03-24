package com.example.myapplication2;

//import
import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {User.class}, version = 4)
public abstract class UsersDatabase extends RoomDatabase {

    public abstract UserDao userDao();

}
