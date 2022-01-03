package com.example.cookwhat.database;

import android.provider.BaseColumns;

public class UserTableContract {
    private UserTableContract() {}

    /* Inner class that defines the table contents */
    public static class UserTable implements BaseColumns {
        public static final String TABLE_NAME = "users";
        public static final String COLUMN_NAME_DISPLAY_NAME = "display_name";
        public static final String COLUMN_NAME_FOLLOWER = "follower";
        public static final String COLUMN_NAME_FOLLOWING = "following";
    }
}
