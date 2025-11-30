package com.ror.gamemodel;

import com.ror.gameengine.BattlePanel;

public abstract class Skill {
    protected String name;
    protected int power;
    protected int cooldown;
    protected int currentCooldown;

    public Skill(String name, int power, int cooldown) {
        this.name = name;
        this.power = power;
        this.cooldown = cooldown;
        this.currentCooldown = 0;
    }

    public String getName() { return name; }
    public int getPower() { return power; }
    public int getCooldown() { return cooldown; }
    public int getCurrentCooldown() { return currentCooldown; }

    public boolean isOnCooldown() { return currentCooldown > 0; }
    public void triggerCooldown() { currentCooldown = cooldown; }
    public void reduceCooldown() { if(currentCooldown>0) currentCooldown--; }
    public void resetCooldown() { currentCooldown = 0; }

    // Logic only, no UI
    public abstract void apply(Entity user, Entity target, BattlePanel panel);
}