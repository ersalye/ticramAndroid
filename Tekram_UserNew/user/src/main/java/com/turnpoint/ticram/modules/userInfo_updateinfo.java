package com.turnpoint.ticram.modules;



public class userInfo_updateinfo
{
    private String id;

    private String balance;

    private String mob_active;

    private String rate;

    private String order;

    private String mob;

    private String email;

    private String name;

    private String gender;

    private String noti;

    private String photo;

    private String order_status;

    private String access_token;



    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getBalance ()
    {
        return balance;
    }

    public void setBalance (String balance)
    {
        this.balance = balance;
    }

    public String getMob_active ()
    {
        return mob_active;
    }

    public void setMob_active (String mob_active)
    {
        this.mob_active = mob_active;
    }

    public String getRate ()
    {
        return rate;
    }

    public void setRate (String rate)
    {
        this.rate = rate;
    }

    public String getOrder ()
    {
        return order;
    }

    public void setOrder (String order)
    {
        this.order = order;
    }

    public String getMob ()
    {
        return mob;
    }

    public void setMob (String mob)
    {
        this.mob = mob;
    }

    public String getEmail ()
    {
        return email;
    }

    public void setEmail (String email)
    {
        this.email = email;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public String getGender ()
    {
        return gender;
    }

    public void setGender (String gender)
    {
        this.gender = gender;
    }

    public String getNoti ()
    {
        return noti;
    }

    public void setNoti (String noti)
    {
        this.noti = noti;
    }

    public String getPhoto ()
    {
        return photo;
    }

    public void setPhoto (String photo)
    {
        this.photo = photo;
    }

    public String getOrder_status ()
    {
        return order_status;
    }

    public void setOrder_status (String order_status)
    {
        this.order_status = order_status;
    }

    public String getAccess_token ()
    {
        return access_token;
    }

    public void setAccess_token (String access_token)
    {
        this.access_token = access_token;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [id = "+id+", balance = "+balance+", mob_active = "+mob_active+", rate = "+rate+", order = "+order+", mob = "+mob+", email = "+email+", name = "+name+", gender = "+gender+", noti = "+noti+", photo = "+photo+", order_status = "+order_status+", access_token = "+access_token+"]";
    }
}