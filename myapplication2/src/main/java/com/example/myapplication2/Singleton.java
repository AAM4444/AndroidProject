package com.example.myapplication2;

//import
import android.content.Context;

import androidx.room.Room;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

public class Singleton {

    public static final Singleton INSTANCE = new Singleton();

    private Singleton() {

    }

    public UsersDatabase CreateDatabase(Context context) {
        UsersDatabase db = Room.databaseBuilder(context, UsersDatabase.class, "database")
                .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4).build();
        return db;
    }

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
        }
    };

    static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
        }
    };

    static final Migration MIGRATION_3_4 = new Migration(3, 4) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
        }
    };

}
