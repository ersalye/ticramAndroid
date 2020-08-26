package com.turnpoint.ticram.modules;



public class update_info2 {
    private userInfo_updateinfo User;

    private String handle;

    private String success;

    private String msg;

    public userInfo_updateinfo getUser ()
    {
        return User;
    }

    public void setUser (userInfo_updateinfo User)
    {
        this.User = User;
    }

    public String getHandle ()
    {
        return handle;
    }

    public void setHandle (String handle)
    {
        this.handle = handle;
    }

    public String getSuccess ()
    {
        return success;
    }

    public void setSuccess (String success)
    {
        this.success = success;
    }

    public String getMsg ()
    {
        return msg;
    }

    public void setMsg (String msg)
    {
        this.msg = msg;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [User = "+User+", handle = "+handle+", success = "+success+", msg = "+msg+"]";
    }
}