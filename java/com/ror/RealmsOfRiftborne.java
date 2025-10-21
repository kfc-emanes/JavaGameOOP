package com.ror;

import com.ror.gamemodel.*;

public class RealmsofRiftborne {
    public static void main(String[] args) {
        System.out.println("Debug Build Prototype v1.0");

        PlayerEntity andrew = new PlayerEntity("Andrew", 100, 100, 20, 5);

        Entity dummy = new Entity("Training Dummy", 50, 50, 0, 0);

        System.out.println("Combat Prototype Test:");
        andrew.attack(dummy);
        dummy.attack(andrew);


    }
}