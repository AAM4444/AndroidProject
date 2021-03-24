package com.example.myapplication2;

//import
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UserDao {

    @Query("SELECT * FROM users WHERE from_page = :pageSelected")
    List<User> getUsersByPage(int pageSelected);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(User user);

}
