package com.ror.gamemodel;

public class Skill {
    private String name;
    private int power; 
    private String type; 
    private int level;
    private int maxLevel;

    public String getName() { return name; }
    public int getPower() { return power; }
    public String getType() { return type; }
    public int getCooldown() { return cooldown; }

    // For logging/testing
    public void use(Entity user, Entity target) {
        System.out.println(user.getName() + " uses " + name + " on " + target.getName() + "!");

        if (type.equalsIgnoreCase("Attack")) {
            int totalDamage = power + user.getAtk();
            target.takeDamage(totalDamage);
            System.out.println(target.getName() + " takes " + totalDamage + " damage!");
        } 
        else if (type.equalsIgnoreCase("Heal")) {
            int healed = power;
            int newHealth = Math.min(user.getMaxHealth(), user.getCurrentHealth() + healed);
            user.setCurrentHealth(newHealth);
            System.out.println(user.getName() + " heals for " + healed + " HP (" + newHealth + " total).");
        } 
        else {
            System.out.println("Unknown skill type: " + type);
        }
    }
    private int cooldown;
    private int currentCooldown;

    public Skill(String name, int power, String type, int cooldown) {
        this.name = name;
        this.power = power;
        this.type = type;
        this.cooldown = cooldown;
        this.currentCooldown = 0;
    }

    public boolean isOnCooldown() {
        return currentCooldown > 0;
    }

    public void reduceCooldown() {
        if (currentCooldown > 0) currentCooldown--;
    }

    public void triggerCooldown() {
        currentCooldown = cooldown;
    }

    public int getCurrentCooldown() {
        return currentCooldown;
    }

    public void levelUp() { // updated - level up method
        if (level < maxLevel) {
            level++;
            power += 5;
            System.out.println(name + " leveled up to " + level + "! Power increased to " + power);
        } else {
            System.out.println(name + " is already at max level.");
        }
    }

    public void resetCooldown() {
        currentCooldown = 0;
    }

    public void setCurrentCooldown(int val) {
        this.currentCooldown = Math.max(0, val);
    }
}