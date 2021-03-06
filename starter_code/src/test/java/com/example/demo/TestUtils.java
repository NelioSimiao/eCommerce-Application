package com.example.demo;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;

import java.lang.reflect.Field;

public class TestUtils {
    public static void injectObjects(Object target, String fieldName, Object toInject){

        boolean wasPrivate = false;

        try {
            Field f = target.getClass().getDeclaredField(fieldName);
            if (!f.isAccessible()){
                f.setAccessible(true);
                wasPrivate = true;
            }
            f.set(target, toInject);
            if (wasPrivate){
                f.setAccessible(false);
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    public static User createUser(){

        User user= new User();
        user.setUsername("user1");
        user.setPassword("PasswordUser");
        Cart cart = new Cart();
        cart.setId(1L);
        cart.setUser(user);
        user.setCart( cart);
        return user;
    }

    public static Cart createCart(){
        Cart cart = new Cart();
        cart.setId(1L);
        return cart;
    }

    public static Item createItem(){
        Item item= new Item();
        item.setId(1L);
        return item;
    }
}
