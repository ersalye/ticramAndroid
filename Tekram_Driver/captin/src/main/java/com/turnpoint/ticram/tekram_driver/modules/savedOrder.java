package com.turnpoint.ticram.tekram_driver.modules;

/**
 * Created by marina on 14/08/2018.
 */

public class savedOrder {

    public savedOrder(){}

    public static Order cur_order;


    public static void saveorder(Order order){
             cur_order=order;
    }

    public static Order getorder(){
        return cur_order;
    }
}
