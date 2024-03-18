package com.example.milestonemate;

public class TodoSlot {
    private final String title;
    private final String state;
    private final String id;
    private final String uid;
    private final String date;
    private final String description;
    private final String imagePath;

    public TodoSlot(String id, String uid, String title, String state, String date, String description, String imagePath)
    {
        this.id = id;
        this.uid = uid;
        this.title = title;
        this.state = state;
        this.date = date;
        this.description = description;
        this.imagePath = imagePath;
    }

    public String getTitle(){
        return title;
    }

    public String getState(){
        return state;
    }

    public String getDate() { return date; }

    public String getId() { return id; }

    public String getDescription() { return description; }

    public String getImagePath() { return imagePath; }
}
