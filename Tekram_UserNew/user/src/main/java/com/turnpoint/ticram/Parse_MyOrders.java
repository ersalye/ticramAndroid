package com.turnpoint.ticram;

import com.turnpoint.ticram.modules.singleOrder;
import com.turnpoint.ticram.modules.tabs_catogary;

import java.util.ArrayList;

/**
 * Created by marina on 19/10/2018.
 */

public class Parse_MyOrders {


    private static ArrayList<singleOrder> catalog= new ArrayList<singleOrder>();

    public static ArrayList<singleOrder> getCatalog(){
        return catalog;
    }


    public static ArrayList<singleOrder> setCatalog(ArrayList<singleOrder> array){
        catalog=array;
        return catalog;
    }

    public static void ClearArray(){
        catalog.clear();
    }

    public static int arraySize(){

        return catalog.size();
    }


    public static void AddCatalog(ArrayList<singleOrder> array){
        ArrayList<singleOrder> newArry=array;
        for(int i=0;i<newArry.size();i++){
            catalog.add(newArry.get(i));
        }

    }



}
