package com.example.dana.carmanagement.database;

import android.database.sqlite.SQLiteDatabase;

public class CarTable {
    // Database table
    public static final String TABLE_CAR = "car";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_MAKE = "make";
    public static final String COLUMN_MODEL = "model";
    public static final String COLUMN_YEAR = "year";
    public static final String COLUMN_PRICE = "price";

    // Database creation SQL statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_CAR
            + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_MAKE + " text not null, "
            + COLUMN_MODEL + " text not null, "
            + COLUMN_YEAR + " INT not null, "
            + COLUMN_PRICE + " INT not null"
            + ");";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_CAR);
        onCreate(database);
    }
}
