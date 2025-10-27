package com.ror.gamemodel;

public class Skill {
    private String name;
    private int power;
    private String type;
    private int cooldown;
    private int currentCooldown;

    // Constructor for skills with cooldown
    public Skill(String name, int power, String type, int cooldown) {
        this.name = name;
        this.power = power;
        this.type = type;
        this.cooldown = cooldown;
        this.currentCooldown = 0;
    }

    // Optional shortcut for skills with no cooldown
    public Skill(String name, int power, String type) {
        this(name, power, type, 0);
    }

    public String getName() { return name; }
    public int getPower() { return power; }
    public String getType() { return type; }
    public int getCooldown() { return cooldown; }
    public int getCurrentCooldown() { return currentCooldown; }

    public boolean isOnCooldown() {
        return currentCooldown > 0;
    }

    public void triggerCooldown() {
        if (cooldown > 0) {
            currentCooldown = cooldown;
        }
    }

    public void reduceCooldown() {
        if (currentCooldown > 0) {
            currentCooldown--;
        }
    }

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
}
