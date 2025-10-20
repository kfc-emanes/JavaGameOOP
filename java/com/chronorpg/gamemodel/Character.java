package com.chronorpg.gamemodel;

import java.util.List;

public class Character {
    private String name;
    private int maxHealth;
    private int currentHealth;
    private int atk;
    private int def;
    private int spd; //spd -> used for character attacking order since this is turn-based. many turn-based games use this
}

public String getName() {
    return name;
}

public String getMaxHealth() {
    return maxHealth;
}

public String getCurrentHealth() {
    return currentHealth;
}

public String getAtk() {
    return atk;
}

public String getDef() {
    return def;
}

public String getSpd() {
    return spd;
}