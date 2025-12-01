package com.ror.gamemodel;

import com.ror.gameutil.BattleView;

public abstract class Skill {
    public final String name;
    public final String description;
    public final int maxCooldown;
    public int currentCooldown;
    public int atk;

    public Skill(String name, String description, int maxCooldown, int atk) {
        this.name = name;
        this.description = description;
        this.maxCooldown = maxCooldown;
        this.atk = atk;
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