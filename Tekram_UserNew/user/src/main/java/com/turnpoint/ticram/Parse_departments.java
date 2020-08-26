package com.turnpoint.ticram;

import com.turnpoint.ticram.modules.Department;
import com.turnpoint.ticram.modules.SubDepartment;

import java.util.ArrayList;

public class Parse_departments  {


    private static ArrayList<Department> catalog= new ArrayList<Department>();

    public static ArrayList<Department> getCatalog(){
        return catalog;
    }


    public static ArrayList<Department> setCatalog(ArrayList<Department> array){
        catalog=array;
        return catalog;
    }

    public static void ClearArray(){
        catalog.clear();
    }

    public static int arraySize(){
        return catalog.size();
    }

    /*public static ArrayList<SubDepartment> arrysub(int dep_id){
        for(catalog.size())
        return catalog.size();
    }*/





}
