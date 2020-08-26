package com.turnpoint.ticram;

import com.turnpoint.ticram.modules.tabs_catogary;

import java.util.ArrayList;

/**
 * Created by marina on 19/10/2018.
 */

public class Parse_mainCats  {


    private static ArrayList<tabs_catogary> catalog= new ArrayList<tabs_catogary>();

    public static ArrayList<tabs_catogary> getCatalog(){
        return catalog;
    }


    public static ArrayList<tabs_catogary> setCatalog(ArrayList<tabs_catogary> array){
        catalog=array;
        return catalog;
    }

    public static void ClearArray(){
        catalog.clear();
    }

    public static int arraySize(){

        return catalog.size();
    }


    public static void AddCatalog(ArrayList<tabs_catogary> array){
        ArrayList<tabs_catogary> newArry=array;
        for(int i=0;i<newArry.size();i++){
            catalog.add(newArry.get(i));
        }

    }



}
