package com.example.milestonemate;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.constraintlayout.widget.Constraints;

public class DatabaseHelper extends SQLiteOpenHelper {
    private final Context context;
    // SQL statement to create a user table
    private static final String CREATE_USER_TABLE =
            "CREATE TABLE " +
                    Constants.USER_TABLE + " (" +
                    Constants.UID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    Constants.USERNAME + " TEXT, " +
                    Constants.EMAIL + " TEXT, " +
                    Constants.PASSWORD + " TEXT, " +
                    Constants.REWARD_POINT + " INTEGER DEFAULT 0);";

    // SQL statement to drop the user table if it exists
    private static final String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + Constants.USER_TABLE;

    // SQL statement to create a todo table
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


    // SQL statement to create a banner table
    private static final String CREATE_BANNER_TABLE =
            "CREATE TABLE " +
                    Constants.BANNER_TABLE + " (" +
                    Constants.ITEM_NAME + " TEXT PRIMARY KEY, " +
                    Constants.ITEM_TYPE + " TEXT, " +
                    Constants.ITEM_IMAGE_ONE + " TEXT, " +
                    Constants.ITEM_IMAGE_TWO + " TEXT, " +
                    Constants.ITEM_VALUE + " INTEGER);";

    private static final String DROP_BANNER_TABLE = "DROP TABLE IF EXISTS " + Constants.BANNER_TABLE;

    // SQL statement to create a banner table
    private static final String CREATE_USER_CHARACTER_TABLE =
            "CREATE TABLE " +
                    Constants.USER_CHARACTER_TABLE + " (" +
                    Constants.UID_CHARACTER + " INTEGER, " +
                    Constants.NAME_CHARACTER + " TEXT, " +
                    Constants.RELATIONSHIP + " REAL);";
    private static final String DROP_USER_CHARACTER_TABLE = "DROP TABLE IF EXISTS " + Constants.USER_CHARACTER_TABLE;

    private static final String CREATE_USER_GIFT_TABLE =
            "CREATE TABLE " +
                    Constants.USER_GIFT_TABLE + " (" +
                    Constants.UID_GIFT + " INTEGER, " +
                    Constants.NAME_GIFT + " TEXT, " +
                    Constants.AMOUNT_GIFT + " INTEGER);";
    private static final String DROP_USER_GIFT_TABLE = "DROP TABLE IF EXISTS " + Constants.USER_GIFT_TABLE;

    // Constructor for DatabaseHelper
    public DatabaseHelper(Context context){
        super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
        this.context = context;
    }

    // Called when the database is created for the first time
    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(CREATE_USER_TABLE);
            db.execSQL(CREATE_TODO_TABLE);
            db.execSQL(CREATE_BANNER_TABLE);
            db.execSQL(CREATE_USER_CHARACTER_TABLE);
            db.execSQL(CREATE_USER_GIFT_TABLE);

            insertInitialData(db);
            Toast.makeText(context, "onCreate() called", Toast.LENGTH_LONG).show();
        } catch (SQLException e) {
            Toast.makeText(context, "exception onCreate() db", Toast.LENGTH_LONG).show();
        }
    }
    // Called when the database needs to be upgraded
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL(DROP_TODO_TABLE);
            db.execSQL(DROP_USER_TABLE);
            db.execSQL(DROP_BANNER_TABLE);
            db.execSQL(DROP_USER_CHARACTER_TABLE);
            db.execSQL(DROP_USER_GIFT_TABLE);
            onCreate(db);
            Toast.makeText(context, "onUpgrade called", Toast.LENGTH_LONG).show();
        } catch (SQLException e) {
            Toast.makeText(context, "exception onUpgrade() db", Toast.LENGTH_LONG).show();
        }
    }

    //initialize banner table
    private void insertInitialData(SQLiteDatabase db){
        // 示例：向 BANNER_TABLE 插入一些初始化数据
        String insertData = "INSERT INTO " + Constants.BANNER_TABLE +
                " (" + Constants.ITEM_NAME + ", " +
                Constants.ITEM_TYPE + ", " +
                Constants.ITEM_IMAGE_ONE + ", " +
                Constants.ITEM_IMAGE_TWO + ", " +
                Constants.ITEM_VALUE + ") VALUES (?, ?, ?, ?, ?)";

        // 以防你想添加多条记录，可以使用循环或者准备多个数据数组
        Object[][] data = {
                {"Keqing", Constants.CHARACTER, "img/Keqing_01.png", "img/Keqing_02.png", 0},
                {"Hu Tao", Constants.CHARACTER, "img/HuTao_01.png", "img/HuTao_02.png", 0},
                {"Ayaka", Constants.CHARACTER, "img/Ayaka_01.png", "img/Ayaka_02.png", 0},
                {"Kokomi", Constants.CHARACTER, "img/Kokomi_01.png", "img/Kokomi_02.png", 0},

                {"Donut", Constants.GIFT, "img/donut2.png", "img/donut2.png", 20},
                {"Martini", Constants.GIFT, "img/martini.png", "img/martini.png", 20},
                {"Cupcake", Constants.GIFT, "img/cupcake2.png", "img/cupcake2.png", 20},
                {"Potion Love", Constants.GIFT, "img/potion-love.png", "img/potion-love.png", 50}
                // 可以继续添加更多行数据
        };

        for (Object[] row : data) {
            db.execSQL(insertData, row);
        }
    }
}
