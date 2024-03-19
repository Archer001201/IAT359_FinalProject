package com.example.milestonemate;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

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
        contentValues.put(Constants.EMAIL, email);
        contentValues.put(Constants.PASSWORD, password);
        db.insert(Constants.USER_TABLE, null, contentValues);
    }

    public boolean checkUserExists(String email) {
        SQLiteDatabase db = helper.getReadableDatabase();
        String[] columns = { Constants.EMAIL };
        String selection = Constants.EMAIL + " = ?";
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
                Constants.EMAIL + "=?",
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

    public void addNewTodo(String uid, String title, String dueDate){
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.TODO_TITLE, title);
        contentValues.put(Constants.DUE_DATE, dueDate);
        contentValues.put(Constants.TODO_UID, Integer.parseInt(uid));
        contentValues.put(Constants.TODO_STATE, Constants.INCOMPLETE);
        db.insert(Constants.TODO_TABLE, null, contentValues);
    }

    public List<TodoSlot> getTodoListById(String uid, String date, String dateRadioResult, String stateRadioResult){
        List<TodoSlot> todoList = new ArrayList<>();
        SQLiteDatabase db = helper.getReadableDatabase();
        List<String> selectionArgsList = new ArrayList<>();
        selectionArgsList.add(uid);

        String selection_1 = "";
        if (dateRadioResult.equals("Today")){
            selection_1 = " AND " + Constants.DUE_DATE + "=?";
            selectionArgsList.add(date);
        }
        else if (dateRadioResult.equals("Other")){
            selection_1 = " AND " + Constants.DUE_DATE + "!=?";
            selectionArgsList.add(date);
        }

        String selection_2 = "";
        if (!stateRadioResult.equals("All")){
            selection_2 = " AND " + Constants.TODO_STATE + "=?";
            selectionArgsList.add(stateRadioResult);
        }


        String[] columns = {Constants.TODO_TITLE, Constants.TODO_STATE};
        String selection = Constants.TODO_UID + "=?" + selection_1 + selection_2;
        String[] selectionArgs = selectionArgsList.toArray(new String[0]);

        Cursor cursor = db.query(
                Constants.TODO_TABLE,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        int titleIndex = cursor.getColumnIndex(Constants.TODO_TITLE);
        int stateIndex = cursor.getColumnIndex(Constants.TODO_STATE);
        int idIndex = cursor.getColumnIndex(Constants.TODO_ID);
        int uidIndex = cursor.getColumnIndex(Constants.TODO_UID);
        int dateIndex = cursor.getColumnIndex(Constants.DUE_DATE);
        int descriptionIndex = cursor.getColumnIndex(Constants.TODO_DESCRIPTION);
        int imagePathIndex = cursor.getColumnIndex(Constants.IMAGE_PATH);

        if (titleIndex != -1 && stateIndex != -1) {
            if (cursor.moveToFirst()) {
                do {
                    String id = cursor.getString(idIndex);
                    String todo_uid = cursor.getString(uidIndex);
                    String title = cursor.getString(titleIndex);
                    String state = cursor.getString(stateIndex);
                    String todo_date = cursor.getString(dateIndex);
                    String description = cursor.getString(descriptionIndex);
                    String imagePath = cursor.getString(imagePathIndex);

                    TodoSlot item = new TodoSlot(id, todo_uid, title, state, todo_date, description, imagePath);
                    todoList.add(item);
                } while (cursor.moveToNext());
            }
        } else {
            Log.e("getTodayTodoByUid", "One or more column names are invalid.");
        }
        cursor.close();

        return todoList;
    }

    public void updateTodoById(String id, String state, String description, String imagePath){
        SQLiteDatabase db = helper.getReadableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.TODO_STATE, state);
        contentValues.put(Constants.TODO_DESCRIPTION, description);
        contentValues.put(Constants.IMAGE_PATH, imagePath);

        String selection = Constants.TODO_ID + "=?";
        String[] selectionArgs = { id };

        db.update(Constants.TODO_TABLE, contentValues, selection, selectionArgs);
    }

    public void deleteTodoById(String id){
        SQLiteDatabase db = helper.getWritableDatabase();
        String selection = Constants.TODO_ID + " = ?";
        String[] selectionArgs = { id };
        db.delete(Constants.TODO_TABLE, selection, selectionArgs);
    }
}
