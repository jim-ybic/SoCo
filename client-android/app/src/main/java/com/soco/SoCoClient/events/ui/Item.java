package com.soco.SoCoClient.events.ui;

public interface Item {

    public static String LIST_ITEM_TYPE_ENTRY = "entry";
    public static String LIST_ITEM_TYPE_USER = "user";
    public static String LIST_ITEM_TYPE_SECTION = "section";


    public boolean isSection();

    public String getType();

}
