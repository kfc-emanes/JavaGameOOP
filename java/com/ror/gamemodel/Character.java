package com.ror.gamemodel;

import java.util.List;

public class Character {
    private String name;
    private int maxHealth;
    private int currHealth;
    private int atk;
    private int def;
    private Skill[] skills;
   //private int spd; spd -> used for character attacking order since this is turn-based. many turn-based games use this


public Character(String name, int maxHealth, int currHealth, int atk, int def) {
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

public void attack(Character target) { //target is the character being attacked. not defined here, but will be defined when called
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

public void useSkill(int slot, Character target) {
    
        System.out.println(name + " uses " + skill.getName() + " on " + target.getName() + "!");
    
} //not properly defined yet, needs Skill.java

}





