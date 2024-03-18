package com.example.milestonemate;

public class Constants {
    public static final String DATABASE_NAME = "milestone_mate_database";
    public static final int DATABASE_VERSION = 5;

    //Constants of user table
    public static final String USER_TABLE = "users";
    public static final String UID = "uid";
    public static final String USERNAME = "username";
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";

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
    public static final String COMPLETE = "Complete";
    public static final String INCOMPLETE = "Incomplete";
}
