package com.chronorpg.gamemodel;

import java.util.List;

public class Character {
    private String name;
    private int maxHealth;
    private int currentHealth;
    private int atk;
    private int def;
    private int spd; //spd -> used for character attacking order since this is turn-based
}

public String getName() {
    return name;
}