package com.example.milestonemate;

public class CharacterSlot {
    private final String name;
    private final String imagePath;

    CharacterSlot(String name, String imagePath){
        this.name = name;
        this.imagePath = imagePath;
    }

    public String getName(){return name;}

    public String getImagePath(){return imagePath;}
}
