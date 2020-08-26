package com.turnpoint.ticram.tekram_driver.modules;

/**
 * Created by marina on 14/08/2018.
 */

public class addcoordsfirebase {
    public String coords;
    public  String id ;
    public  String title ;

    public addcoordsfirebase() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public addcoordsfirebase(String coords , String id , String title) {
        this.coords = coords;
        this.id=id;
        this.title=title;

    }

    public String getCoords() {
        return coords;
    }

    public void setCoords(String coords) {
        this.coords = coords;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return coords;
    }
}
