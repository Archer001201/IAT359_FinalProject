package com.example.milestonemate;

public class GiftSlot {
    private final String name;
    private final String imagePath;
    private final int value;
    private final int amount;

    GiftSlot(String name, String imagePath, int value, int amount){
        this.name = name;
        this.imagePath = imagePath;
        this.value = value;
        this.amount = amount;
    }

    public String getName(){return name;}

    public String getImagePath(){return imagePath;}

    public int getValue(){return value;}

    public int getAmount(){return amount;}
}
