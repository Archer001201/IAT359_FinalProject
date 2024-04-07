package com.example.milestonemate;

public class Constants {
    public static final String DATABASE_NAME = "milestone_mate_database";
    public static final int DATABASE_VERSION = 15;

    //Constants of user table
    public static final String USER_TABLE = "users";
    public static final String UID = "uid";
    public static final String USERNAME = "username";
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    public static final String REWARD_POINT = "reward_point";

    //Constants of todoList table
    public static final String TODO_TABLE = "todo";
    public static final String TODO_ID = "todo_id";
    public static final String TODO_UID = "todo_uid";
    public static final String TODO_TITLE = "todo_title";
    public static final String DUE_DATE = "due_date";
    public static final String TODO_STATE = "todo_state";
    public static final String TODO_DESCRIPTION = "todo_description";
    public static final String IMAGE_PATH = "image_path";

    //Constants of todoEvent state
    public static final String COMPLETE = "Completed";
    public static final String INCOMPLETE = "Incomplete";

    //Constants of banner table
    public static final String BANNER_TABLE = "banner";
    public static final String ITEM_TYPE = "item_type";
    public static final String ITEM_NAME = "item_name";
    public static final String ITEM_IMAGE_ONE = "item_image_one";
    public static final String ITEM_IMAGE_TWO = "item_image_two";
    public static final String ITEM_VALUE = "item_value";

    //Constants of item type
    public static final String CHARACTER = "Character";
    public static final String GIFT = "Gift";

    //Constants of user_character table
    public static final String USER_CHARACTER_TABLE = "user_character";
    public static final String UID_CHARACTER = "uid_character";
    public static final String NAME_CHARACTER = "name_character";
    public static final String RELATIONSHIP = "relationship";

    //Constants of user_gift table
    public static final String USER_GIFT_TABLE = "user_gift";
    public static final String UID_GIFT = "uid_gift";
    public static final String NAME_GIFT = "name_gift";
    public static final String AMOUNT_GIFT = "amount_gift";

    //Constants of gift view tags
    public static final int NAME = 1;
    public static final int VALUE = 2;
}
