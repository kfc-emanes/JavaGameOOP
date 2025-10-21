package com.ror.gamemodel;

import java.util.*;

public class Entity {
    protected String name;
    protected int maxHealth;
    protected int currHealth;
    protected int atk;
    protected int def;
    protected Skill[] skills;
   //private int spd; spd -> used for character attacking order since this is turn-based. many turn-based games use this


public Entity(String name, int maxHealth, int currHealth, int atk, int def) {
    this.name = name;
    this.maxHealth = maxHealth;
    this.currHealth = currHealth;
    this.atk = atk;
    this.def = def;
    this.skills = skills;
}

public String getName() {
    return name;
}

public int getMaxHealth() {
    return maxHealth;
}

public int getCurrentHealth() {
    return currentHealth;
}

public int getAtk() {
    return atk;
}

public int getDef() {
    return def;
}

// public String getSpd() {
//     return spd;
// }

public Skill[] getSkills() {
    return skills;
}

//Methods for char actions and etc

public void takeDamage(int dmg) {
    int currHealth = Math.max(0, dmg - def); //takes dmg - def
    if (currHealth < 0) { //if currHealth < 0, then its 0
        currHealth = 0;
    }
    System.out.println(name + " took " + dmg + " damage! " + name + " has " + currHealth + " health left.");
}

public void attack(Entity target) { //target is the character being attacked. not defined here, but will be defined when called
    System.out.println(name + " attacks " + target.getName() + " for " + atk + " damage!");
    target.takeDamage(atk);
}

public boolean isAlive() {
    return currHealth > 0;
}

public void setSkill(int slot, Skill skill) {
    if (slot >= 0 || slot < skills.length) {
        skills[slot] = skill;
        return;
    } else {
        throw new IllegalArgumentException("Invalid skill slot: " + slot);
    }

    skills[slot] = skill;
}

public void useSkill(int slot, Entity target) {
    
        System.out.println(name + " uses " + skill.getName() + " on " + target.getName() + "!");
    
} //not properly defined yet, needs Skill.java

}





