package com.example.milestonemate;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.constraintlayout.widget.Constraints;

public class DatabaseHelper extends SQLiteOpenHelper {
    private final Context context;

    private static final String CREATE_USER_TABLE =
            "CREATE TABLE " +
                    Constants.USER_TABLE + " (" +
                    Constants.UID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    Constants.USERNAME + " TEXT, " +
                    Constants.EMAIL + " TEXT, " +
                    Constants.PASSWORD + " TEXT, " +
                    Constants.REWARD_POINT + " INTEGER DEFAULT 0);";

    private static final String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + Constants.USER_TABLE;

    private static final String CREATE_TODO_TABLE =
            "CREATE TABLE " +
                    Constants.TODO_TABLE + " (" +
                    Constants.TODO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    Constants.TODO_TITLE + " TEXT, " +
                    Constants.DUE_DATE + " TEXT, " +
                    Constants.TODO_STATE + " TEXT, " +
                    Constants.TODO_DESCRIPTION + " TEXT, " +
                    Constants.IMAGE_PATH + " TEXT, " +
                    Constants.TODO_UID + " INTEGER);";

    private static final String DROP_TODO_TABLE = "DROP TABLE IF EXISTS " + Constants.TODO_TABLE;

    public DatabaseHelper(Context context){
        super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(CREATE_USER_TABLE);
            db.execSQL(CREATE_TODO_TABLE);
            Toast.makeText(context, "onCreate() called", Toast.LENGTH_LONG).show();
        } catch (SQLException e) {
            Toast.makeText(context, "exception onCreate() db", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL(DROP_TODO_TABLE);
            db.execSQL(DROP_USER_TABLE);
            onCreate(db);
            Toast.makeText(context, "onUpgrade called", Toast.LENGTH_LONG).show();
        } catch (SQLException e) {
            Toast.makeText(context, "exception onUpgrade() db", Toast.LENGTH_LONG).show();
        }
    }
}
