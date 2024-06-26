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

    //获取数据库里面的用户的数据，传递一个用户uid和一个数据类型（调用Constants类里的常量）
    public int getValueByUid(String uid, String returnType){
        SQLiteDatabase db = helper.getReadableDatabase();
        int result = 0;

        Cursor cursor = db.query(
                Constants.USER_TABLE,
                new String[]{returnType},
                Constants.UID + "=?",
                new String[]{uid},
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(returnType);
            if (columnIndex != -1) {
                result = cursor.getInt(columnIndex);
            } else {
                Log.e("getValueByUid", "Column " + returnType + " does not exist.");
            }
            cursor.close();
        } else {
            Log.e("getValueByUid", "No entry matches the given email.");
        }
        db.close();
        return result;
    }

    //更新奖励点数，传递用户uid和更新的点数的值，用户uid通过shared preferences获取
    public void updateRewardPointsByUid(String uid, int value){
        SQLiteDatabase db = helper.getReadableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.REWARD_POINT, value);

        String selection = Constants.UID + "=?";
        String[] selectionArgs = { uid };

        db.update(Constants.USER_TABLE, contentValues, selection, selectionArgs);
    }

    public void addNewTodo(String uid, String title, String dueDate, String description){
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.TODO_TITLE, title);
        contentValues.put(Constants.DUE_DATE, dueDate);
        contentValues.put(Constants.TODO_DESCRIPTION, description);
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


//        String[] columns = {Constants.TODO_TITLE, Constants.TODO_STATE};
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

    public List<String> getItemNameByType(String type){
        SQLiteDatabase db = helper.getReadableDatabase();
        List<String> items = new ArrayList<>();

        Cursor cursor = db.query(
                Constants.BANNER_TABLE,
                new String[]{Constants.ITEM_NAME},
                Constants.ITEM_TYPE + "=?",
                new String[]{type},
                null,
                null,
                null
        );

        if (cursor != null) {
            try {
                int columnIndex = cursor.getColumnIndexOrThrow(Constants.ITEM_NAME);
                while (cursor.moveToNext()) {
                    items.add(cursor.getString(columnIndex));
                }
            } catch (IllegalArgumentException e) {
                Log.e("getItemNameByType", "Error finding column " + Constants.ITEM_NAME, e);
            } finally {
                cursor.close();
            }
        } else {
            Log.e("getItemNameByType", "Cursor is null or could not move to first entry.");
        }

        return items;
    }

    public String getItemImageByName(String name, String imageType){
        SQLiteDatabase db = helper.getReadableDatabase();
//        List<String> items = new ArrayList<>();
        String result = null;

        Cursor cursor = db.query(
                Constants.BANNER_TABLE,
                new String[]{imageType},
                Constants.ITEM_NAME + "=?",
                new String[]{name},
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(imageType);
            if (columnIndex != -1) {
                result = cursor.getString(columnIndex);
            } else {
                Log.e("getItemImageByName", "Column " + Constants.ITEM_NAME + " does not exist.");
            }
            cursor.close();
        } else {
            Log.e("getItemImageByName", "No entry matches the given " + name + ".");
        }

        return result;
    }

    public void updateGiftTable(String uid, String giftName, boolean isAdding){
        if (checkGiftExists(uid, giftName)){
            //update amount
            int giftAmount = getGiftAmount(uid, giftName);
            if (isAdding) giftAmount ++;
            else giftAmount --;

            SQLiteDatabase db = helper.getReadableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(Constants.AMOUNT_GIFT, giftAmount);

            String selection = Constants.UID_GIFT + "=? AND " + Constants.NAME_GIFT + "=?";
            String[] selectionArgs = {uid, giftName};

            db.update(Constants.USER_GIFT_TABLE, contentValues, selection, selectionArgs);
        }
        else{
            //insert gift
            SQLiteDatabase db = helper.getWritableDatabase();

            ContentValues contentValues = new ContentValues();
            contentValues.put(Constants.UID_GIFT, uid);
            contentValues.put(Constants.NAME_GIFT, giftName);
            contentValues.put(Constants.AMOUNT_GIFT, 1);
            db.insert(Constants.USER_GIFT_TABLE, null, contentValues);
        }
    }

    public int getGiftAmount(String uid, String giftName){
        SQLiteDatabase db = helper.getReadableDatabase();
        int result = 0;
        Cursor cursor = db.query(
                Constants.USER_GIFT_TABLE,
                new String[]{Constants.AMOUNT_GIFT},
                Constants.UID_GIFT + "=? AND " + Constants.NAME_GIFT + "=?",
                new String[]{uid, giftName},
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(Constants.AMOUNT_GIFT);
            if (columnIndex != -1) {
                result = cursor.getInt(columnIndex);
            } else {
                Log.e("getGiftAmount", "Column " + Constants.ITEM_NAME + " does not exist.");
            }
            cursor.close();
        } else {
            Log.e("getGiftAmount", "No entry matches the given " + uid + ".");
        }

        return result;
    }

    public boolean checkGiftExists(String uid, String giftName){
        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor cursor = db.query(
                Constants.USER_GIFT_TABLE,
                null,
                Constants.UID_GIFT + "=? AND " + Constants.NAME_GIFT + "=?",
                new String[]{uid, giftName},
                null,
                null,
                null
        );

        int cursorCount = cursor.getCount();
        cursor.close();
        return cursorCount > 0;
    }

    public void insertCharacter(String uid, String characterName){
        if (checkCharacterExists(uid, characterName)){
            updateGiftTable(uid, "Potion Love", true);
        }
        else{
            SQLiteDatabase db = helper.getWritableDatabase();

            ContentValues contentValues = new ContentValues();
            contentValues.put(Constants.UID_CHARACTER, uid);
            contentValues.put(Constants.NAME_CHARACTER, characterName);
            contentValues.put(Constants.RELATIONSHIP, 0);

            db.insert(Constants.USER_CHARACTER_TABLE, null, contentValues);
        }
    }

    public boolean checkCharacterExists(String uid, String characterName){
        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor cursor = db.query(
                Constants.USER_CHARACTER_TABLE,
                null,
                Constants.UID_CHARACTER + "=? AND " + Constants.NAME_CHARACTER + "=?",
                new String[]{uid, characterName},
                null,
                null,
                null
        );

        int cursorCount = cursor.getCount();
        cursor.close();
        return cursorCount > 0;
    }

    public List<String> getItemsByUidAndType(String uid, String type){
        SQLiteDatabase db = helper.getReadableDatabase();
        String table = null;
        String name = null;
        String user = null;
        List<String> result = new ArrayList<>();

        if (type.equals(Constants.CHARACTER)){
            table = Constants.USER_CHARACTER_TABLE;
            name = Constants.NAME_CHARACTER;
            user = Constants.UID_CHARACTER;
        }
        else{
            table = Constants.USER_GIFT_TABLE;
            name = Constants.NAME_GIFT;
            user = Constants.UID_GIFT;
        }

        Cursor cursor = db.query(
                table,
                new String[]{name},
                user + "=?",
                new String[]{uid},
                null,
                null,
                null
        );

        if (cursor != null) {
            try {
                int columnIndex = cursor.getColumnIndexOrThrow(name);
                while (cursor.moveToNext()) {
                    result.add(cursor.getString(columnIndex));
                }
            } catch (IllegalArgumentException e) {
                Log.e("getItemsByUidAndType", "Error finding column " + uid, e);
            } finally {
                cursor.close();
            }
        } else {
            Log.e("getItemsByUidAndType", "Cursor is null or could not move to first entry.");
        }

        return result;
    }

    public List<GiftSlot> getGiftListByUid(String uid){
        List<GiftSlot> giftSlots = new ArrayList<>();
        SQLiteDatabase db = helper.getReadableDatabase();

        // 注意SQL语句中的空格，并使用?作为参数占位符
        String sql = "SELECT * FROM " +
                Constants.BANNER_TABLE + " BANNER INNER JOIN " +
                Constants.USER_GIFT_TABLE + " USER " +
                "ON BANNER." + Constants.ITEM_NAME + " = USER." + Constants.NAME_GIFT +
                " WHERE USER." + Constants.UID_GIFT + " = ?";

        // 使用rawQuery的第二个参数传递uid，以避免SQL注入
        Cursor cursor = db.rawQuery(sql, new String[] {uid});

        int nameIndex = cursor.getColumnIndex(Constants.NAME_GIFT);
        int amountIndex = cursor.getColumnIndex(Constants.AMOUNT_GIFT);
        int valueIndex = cursor.getColumnIndex(Constants.ITEM_VALUE);
        int imageIndex = cursor.getColumnIndex(Constants.ITEM_IMAGE_ONE);

        // 确保cursor不为空且有数据
        if (cursor.moveToFirst()) {
            do {
                // 在这里你需要根据你的GiftSlot对象的构造函数或者setter方法来创建并填充对象
                String name = cursor.getString(nameIndex);
                int amount = cursor.getInt(amountIndex);
                int value = cursor.getInt(valueIndex);
                String image = cursor.getString(imageIndex);

                // 假设GiftSlot有一个相应的构造函数或者使用setter方法设置值
                GiftSlot slot = new GiftSlot(name, image, value, amount);
                giftSlots.add(slot);

            } while (cursor.moveToNext());

            cursor.close();
        }

        return giftSlots;
    }

    public List<CharacterSlot> getCharactersByUid(String uid){
        List<CharacterSlot> slots = new ArrayList<>();
        SQLiteDatabase db = helper.getReadableDatabase();

        // 注意SQL语句中的空格，并使用?作为参数占位符
        String sql = "SELECT * FROM " +
                Constants.BANNER_TABLE + " BANNER INNER JOIN " +
                Constants.USER_CHARACTER_TABLE + " USER " +
                "ON BANNER." + Constants.ITEM_NAME + " = USER." + Constants.NAME_CHARACTER +
                " WHERE USER." + Constants.UID_CHARACTER + " = ?";

        // 使用rawQuery的第二个参数传递uid，以避免SQL注入
        Cursor cursor = db.rawQuery(sql, new String[] {uid});

        int nameIndex = cursor.getColumnIndex(Constants.NAME_CHARACTER);
        int imageIndex = cursor.getColumnIndex(Constants.ITEM_IMAGE_ONE);

        // 确保cursor不为空且有数据
        if (cursor.moveToFirst()) {
            do {
                // 在这里你需要根据你的GiftSlot对象的构造函数或者setter方法来创建并填充对象
                String name = cursor.getString(nameIndex);

                String image = cursor.getString(imageIndex);

                // 假设GiftSlot有一个相应的构造函数或者使用setter方法设置值
                CharacterSlot slot = new CharacterSlot(name, image);
                slots.add(slot);

            } while (cursor.moveToNext());

            cursor.close();
        }

        return slots;
    }

    public float getRelationshipByUidAndName(String uid, String name){
        float result = 0f;
        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor cursor = db.query(
                Constants.USER_CHARACTER_TABLE,
                new String[] {Constants.RELATIONSHIP},
                Constants.UID_CHARACTER + "=? AND " +
                        Constants.NAME_CHARACTER + "=?",
                new String[] {uid, name},
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(Constants.RELATIONSHIP);
            if (columnIndex != -1) {
                result = cursor.getFloat(columnIndex);
            } else {
                Log.e("getRelationshipByUidAndName", "Column " + Constants.ITEM_NAME + " does not exist.");
            }
            cursor.close();
        } else {
            Log.e("getRelationshipByUidAndName", "No entry matches the given " + uid + ".");
        }

        return result;
    }

    public void updateRelationship(String uid, String name, int deltaValue, boolean isAdding){
        SQLiteDatabase db = helper.getReadableDatabase();
        String selection = Constants.UID_CHARACTER + "=? AND " + Constants.NAME_CHARACTER + "=?";
        String[] selectionArgs = {uid, name};
        float result = 0f;
        Cursor cursor = db.query(
                Constants.USER_CHARACTER_TABLE,
                new String[]{Constants.RELATIONSHIP},
                selection,
                selectionArgs,
                null,null,null
        );
        if (cursor != null && cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(Constants.RELATIONSHIP);
            if (columnIndex != -1) {
                result = cursor.getFloat(columnIndex);
            } else {
                Log.e("updateRelationship", "Column " + Constants.ITEM_NAME + " does not exist.");
            }
            cursor.close();
        } else {
            Log.e("updateRelationship", "No entry matches the given " + uid + ".");
        }
        float value = 0f;
        if (isAdding){
            value = result + deltaValue;
        }
        else{
            value = result - deltaValue;
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.RELATIONSHIP, value);
        db.update(Constants.USER_CHARACTER_TABLE, contentValues, selection, selectionArgs);
    }

    public List<String> queryDialogueByCharacter(String name){
        List<String> dialogues = new ArrayList<String>();
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(
                Constants.CHARACTER_DIALOGUE_TABLE,
                null,
                Constants.NAME_DIALOGUE + "=?",
                new String[]{name},
                null,null,null
        );

        int levelOneIndex = cursor.getColumnIndex(Constants.LEVEL_ONE);
        int levelTwoIndex = cursor.getColumnIndex(Constants.LEVEL_TWO);

        try {
            while (cursor.moveToNext()) {
                // 假设你要获取的两个列分别是COLUMN_A和COLUMN_B
                String levelOne = cursor.getString(levelOneIndex);
                String levelTwo = cursor.getString(levelTwoIndex);

                // 将这两个值以你希望的方式合并，这里简单地将它们连接起来
//                String combinedValue = columnAValue + " " + columnBValue; // 或者任何你希望的格式

                // 将合并后的字符串添加到列表中
                dialogues.add(levelOne);
                dialogues.add(levelTwo);
            }
        } finally {
            cursor.close(); // 确保使用完毕后关闭Cursor
        }

        return dialogues;
    }
}
