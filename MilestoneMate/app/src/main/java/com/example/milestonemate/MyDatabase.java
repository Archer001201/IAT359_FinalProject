package com.example.milestonemate;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class MyDatabase {
    private final Context context;
    private final DatabaseHelper helper;

    public MyDatabase (Context c){
        context = c;
        helper = new DatabaseHelper(context);
    }

    public void addNewUser(String username, String email, String password){
        if (checkUserExists(email)) return;

        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.USERNAME, username);
        contentValues.put(Constants.Email, email);
        contentValues.put(Constants.PASSWORD, password);
        db.insert(Constants.USER_TABLE, null, contentValues);
    }

    public boolean checkUserExists(String email) {
        SQLiteDatabase db = helper.getReadableDatabase();
        String[] columns = { Constants.Email };
        String selection = Constants.Email + " = ?";
        String[] selectionArgs = { email };

        Cursor cursor = db.query(
                Constants.USER_TABLE,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        int cursorCount = cursor.getCount();
        cursor.close();
        return cursorCount > 0;
    }

    public String getValueByEmail(String email, String returnType){
        SQLiteDatabase db = helper.getReadableDatabase();
        String result = null;

        Cursor cursor = db.query(
                Constants.USER_TABLE,
                new String[]{returnType},
                Constants.Email + "=?",
                new String[]{email},
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(returnType);
            if (columnIndex != -1) {
                result = cursor.getString(columnIndex);
            } else {
                Log.e("getValueByEmail", "Column " + returnType + " does not exist.");
            }
            cursor.close();
        } else {
            Log.e("getValueByEmail", "No entry matches the given email.");
        }
        db.close();
        return result;
    }
}
