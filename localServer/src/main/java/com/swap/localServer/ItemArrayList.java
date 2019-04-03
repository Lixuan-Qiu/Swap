package com.swap.localServer;
import java.util.*;

public class ItemArrayList{
    public ArrayList<Item> items ;
    ItemArrayList(){
        items = new ArrayList<Item>();
        items.add(new Item(0,"A"));
        items.add(new Item(1,"B"));
        items.add(new Item(2,"C"));
        items.add(new Item(3,"D"));
        items.add(new Item(4,"E"));
        items.add(new Item(5,"Dog"));
        items.add(new Item(6,"Cat"));
        items.add(new Item(7,"Fish"));

    }
    public ArrayList<Item> getAll(){
        return items;
    }
    public ArrayList<Item> getAnimal(){
        ArrayList<Item> animal_item = new ArrayList<Item>();
        animal_item.add(items.get(5));
        animal_item.add(items.get(6));
        animal_item.add(items.get(7));
        return animal_item;
    }
}