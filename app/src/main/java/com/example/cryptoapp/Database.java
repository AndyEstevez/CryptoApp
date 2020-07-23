package com.example.cryptoapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class Database extends SQLiteOpenHelper {

    public static final String ID = "ID";
    public static final String USER_TABLE = "USER_TABLE";
    public static final String USER_NAME = "USER_NAME";
    public static final String USER_PASSWORD = "USER_PASSWORD";
    public static final String USER_TRANSACTIONS = "USER_TRANSACTIONS";

    public Database(@Nullable Context context) {

        super(context, "database.db", null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE " + USER_TABLE + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + USER_NAME + " TEXT," + USER_PASSWORD + " TEXT," + USER_TRANSACTIONS + " TEXT" + ")";
        db.execSQL(createTableStatement);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(" DROP TABLE IF EXISTS " + USER_TABLE);
        onCreate(db);
    }

    public boolean addUser(UserModel userModel){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        if(userModel.getUsername().equals("") || userModel.getPassword().equals("")){
            return false;
        }

        cv.put(USER_NAME, userModel.getUsername());
        cv.put(USER_PASSWORD, userModel.getPassword());

        long insert = db.insert(USER_TABLE, null, cv);
        if(insert == -1){
            return false;
        }
        else {
            return true;
        }
    }

    public boolean checkUserCredentials(String username, String password){
        String [] cols = {ID};
        SQLiteDatabase db = getReadableDatabase();
        String selection = USER_NAME + "=?" + " and " + USER_PASSWORD + "=?";
        String [] selectionArgs = {username, password};
        Cursor cursor = db.query(USER_TABLE, cols, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();

        cursor.close();
        db.close();

        if(count > 0) return true;
        else return false;
    }

    public boolean ifUserExists(String username){
        String [] cols = {ID};
        SQLiteDatabase db = getReadableDatabase();
        String selection = USER_NAME + "=?";
        String [] selectionArgs = {username};
        Cursor cursor = db.query(USER_TABLE, cols, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();

        cursor.close();
        db.close();

        if(count >= 1) return true;
        else return false;
    }
//
//    public String getTransactions(String username, String arrayList){
//        String [] cols = {ID};
//        SQLiteDatabase db = getReadableDatabase();
//        String selection = USER_NAME + "=?" + " and " + USER_TRANSACTIONS + "=?";
//        String [] selectionArgs = {username, arrayList};
//        Cursor cursor = db.query(USER_TABLE, cols, selection, selectionArgs, null, null, null);
//        int count = cursor.getCount();
//
////        cursor.close();
////        db.close();
//
//        if(count > 0){
//            cursor.moveToFirst();
//            arrayList = cursor.getString(3);
//            System.out.println(arrayList);
//            return arrayList;
//        }
//        else return null;
//
//    }
//
//    public void setTransactions(String username, String arrayList){
//        SQLiteDatabase db = getWritableDatabase();
//        ContentValues cv = new ContentValues();
//        cv.put(USER_TRANSACTIONS, arrayList);
//        String [] cols = {ID};
//        String selection = USER_NAME + "=?";
//        String [] selectionArgs = {username};
//
//        Cursor cursor = db.query(USER_TABLE, cols, selection, selectionArgs, null, null, null);
//        int count = cursor.getCount();
//
////        cursor.close();
////        db.close();
//
//        if(count > 0) {
//            db.update(USER_TABLE, cv, USER_NAME + "= ' " + username + "'", USER_TRANSACTIONS + "=?");
//            return true;
//        }
//        else return null;
//    }
}
