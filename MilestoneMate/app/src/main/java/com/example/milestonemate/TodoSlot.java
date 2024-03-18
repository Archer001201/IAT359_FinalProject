package com.example.milestonemate;

public class TodoSlot {
    private final String title;
    private final String state;




    public TodoSlot(String title, String state)
    {
        this.title = title;
        this.state = state;
    }

    public String getTitle(){
        return title;
    }

    public String getState(){
        return state;
    }
}
