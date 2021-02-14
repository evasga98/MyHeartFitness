package com.myhearfitness.app.db;

import android.provider.BaseColumns;

final class Contract {


    private Contract() {
    }

    public static class UserDataEntry implements BaseColumns {

        public static final String TABLE_NAME = "UserData";
        public static final String COLUMN_NAME= "name";
        public static final String COLUMN_LASTNAME = "lastname";


        public static final String CREATE_TABLE =
                "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                        " (" + _ID  + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + COLUMN_NAME + " TEXT NOT NULL, "
                        + COLUMN_LASTNAME + " TEXT);";
    }

    public static class ProfilePicEntry implements BaseColumns {

        public static final String TABLE_NAME = "ProfilePic";
        public static final String COLUMN_DATA = "data";

        public static final String CREATE_TABLE =
                "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                        " (" + _ID  + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + COLUMN_DATA + " BLOB);";
    }

}