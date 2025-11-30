package com.ror.gamemodel;

import com.ror.gameutil.BattleView;

public abstract class Skill {
    private final String name;
    private final String description;
    private final int maxCooldown;
    private int currentCooldown;

    public Skill(String name, String description, int maxCooldown) {
        this.name = name;
        this.description = description;
        this.maxCooldown = maxCooldown;
        this.currentCooldown = 0;
    }

    public abstract void apply(Entity user, Entity target, BattleView view);

    // --- Cooldown Logic ---
    public void triggerCooldown() {
        this.currentCooldown = this.maxCooldown;
    }

    public void reduceCooldown() {
        if (this.currentCooldown > 0) {
            this.currentCooldown--;
        }
    }

    public void resetCooldown() {
        this.currentCooldown = 0;
    }
    
    // Getters 
    public String getName() { return name; }
    public String getDescription() { return description; }
    public boolean isOnCooldown() { return currentCooldown > 0; }
    public int getCurrentCooldown() { return currentCooldown; }
}