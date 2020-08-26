package com.turnpoint.ticram.modules;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Department {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("sub_departments")
    @Expose
    private ArrayList<SubDepartment> subDepartments = null;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<SubDepartment> getSubDepartments() {
        return subDepartments;
    }

    public void setSubDepartments(ArrayList<SubDepartment> subDepartments) {
        this.subDepartments = subDepartments;
    }

}
