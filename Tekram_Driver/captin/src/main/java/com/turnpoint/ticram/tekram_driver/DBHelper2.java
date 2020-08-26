package com.turnpoint.ticram.tekram_driver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

public class DBHelper2 extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "MyDBName.db";
    public static final String Table_coords = "coords";
    public static final String Table_times = "times";
    public static final String Table_sec = "sec";

    public static final String coords_col1 = "id";
    public static final String coords_col2 = "coord_value";

    public static final String times_col1 = "id";
    public static final String times_col2 = "time_value";

    public static final String sec_col1 = "id";
    public static final String sec_col2 = "sec_value";

    public static final String LatLong = "LatLongs";

    private HashMap hp;

    public DBHelper2(Context context) {
        super(context, DATABASE_NAME , null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table coords " +
                        "(id integer primary key, coord_value text)"
        );

        db.execSQL(
                "create table times " +
                        "(id integer primary key, time_value text)"
        );

        db.execSQL(
                "create table sec " +
                        "(id integer primary key, sec_value text)"
        );
        db.execSQL(
                "create table LatLongs " +
                        "(id integer primary key, title text ,vertices_x text , vertices_y text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS coords");
        db.execSQL("DROP TABLE IF EXISTS times");
        db.execSQL("DROP TABLE IF EXISTS sec");
        db.execSQL("DROP TABLE IF EXISTS LatLongs");

        onCreate(db);
    }

    public boolean insertToCoordsTable (String lat_lon) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("coord_value", lat_lon);
        db.insert("coords", null, contentValues);
        return true;
    }
    public boolean insertToLatLongTable (Integer id , String title,String lat , String lon) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("id",id);
        contentValues.put("title",title);
        contentValues.put("vertices_x",lat);
        contentValues.put("vertices_y",lon);
        db.insert("LatLongs", null, contentValues);
        return true;
    }
    public String[][] getLatLongTable () {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM "+LatLong,null);

        String[][] newArr = new String[res.getCount()][4];

        try {

            res.moveToFirst();


            while (res.isAfterLast() == false) {
                for(int i=0; i < newArr.length ;i++)
                {
                    newArr[i][0]=res.getString(0);
                    newArr[i][1]=res.getString(1);
                    newArr[i][2]=res.getString(2);
                    newArr[i][3]=res.getString(3);
                    res.moveToNext();
                }


            }
            res.close();
        }
        catch (Exception ex){}

        return newArr;
    }



    public boolean insertToTimesTable (String lat_lon) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("time_value", lat_lon);
        db.insert("times", null, contentValues);
        return true;
    }


    public boolean insertToSecTable (String lat_lon) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
            contentValues.put("sec_value", lat_lon);
        db.insert("sec", null, contentValues);
        return true;
    }




    public int numberOfRows_CoordsTable(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, "coords");
        return numRows;
    }


    public int numberOfRows_TimesTable(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, "times");
        return numRows;
    }

    public int numberOfRows_SecTable(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, "sec");
        return numRows;
    }


    public ArrayList<String> getAllCoords() {
        ArrayList<String> array_list = new ArrayList<String>();

        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor res = db.rawQuery("select * from coords", null);
            res.moveToFirst();

            while (res.isAfterLast() == false) {
                array_list.add(res.getString(res.getColumnIndex("coord_value")));
                res.moveToNext();
            }
        }
        catch (Exception ex){}

        return array_list;
    }



    public ArrayList<String> getAllTimes() {
        ArrayList<String> array_list = new ArrayList<String>();

        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor res = db.rawQuery("select * from times", null);
            res.moveToFirst();

            while (res.isAfterLast() == false) {
                array_list.add(res.getString(res.getColumnIndex("time_value")));
                res.moveToNext();
            }
        }
        catch (Exception ex){}
        return array_list;
    }


    public ArrayList<String> getAllSec() {
        ArrayList<String> array_list = new ArrayList<String>();
        try {
            //hp = new HashMap();
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor res = db.rawQuery("select * from sec", null);
            res.moveToFirst();

            while (res.isAfterLast() == false) {
                array_list.add(res.getString(res.getColumnIndex("sec_value")));
                res.moveToNext();
            }
        }
        catch (Exception ex){}

        return array_list;
    }

    public void deleteLatlongTable () {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(LatLong, null, null);

    }
    public void deleteCoordsTable () {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("coords", null, null);

    }

    public void deleteTimesTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("times", null, null);

    }

    public void deleteSecTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("sec", null, null);

    }
   /* public boolean updateContact (Integer id, String name, String phone, String email, String street,String place) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("phone", phone);
        contentValues.put("email", email);
        contentValues.put("street", street);
        contentValues.put("place", place);
        db.update("contacts", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public Integer deleteContact (Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("contacts",
                "id = ? ",
                new String[] { Integer.toString(id) });
    }*/




   /* public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from contacts where id="+id+"", null );
        return res;
    }*/
}
