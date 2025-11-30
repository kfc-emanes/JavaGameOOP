package com.ror.gamemodel;

import java.util.List;
import java.util.ArrayList;

/**
 * Base class for all combat participants (Players and Enemies).
 */
public abstract class Entity {
    
    // --- Fields ---
    public String name;
    public int maxHealth; // Protected visibility for player access
    public int currentHealth; // Protected visibility for player access
    public int atk;         // Attack power, accessed via getAtk()
    public int def;         // Defense value, accessed via getDef()
    public int level = 1; // Default level 1
    protected int speed;    // Speed stat, can influence turn order
    
    // Status Effects
    public boolean isShieldActive = false;
    public boolean isDodgeActive = false;
    public boolean isBlinded = false;
    
    // Skills are stored as a List, which caused the mismatch error
    public List<Skill> skills = new ArrayList<>(); 

    // =================================================================
    // PRIMARY (CANONICAL) CONSTRUCTOR - All others chain here.
    // =================================================================
    public Entity(String name, int maxHealth, int atk, int def, int speed) {
        this.name = name;
        this.maxHealth = maxHealth;
        this.currentHealth = maxHealth;
        this.atk = atk;
        this.def = def;

    }
    
    // CONVENIENCE CONSTRUCTOR 1 (4 Arguments)
    public Entity(String name, int maxHealth, int atk, int def) {
        // Chains to primary constructor with default speed (e.g., 10)
        this(name, maxHealth, atk, def, 10); 
    }
    
    // CONVENIENCE CONSTRUCTOR 2 (3 Arguments)
    public Entity(String name, int maxHealth, int atk) {
        // Chains to 4-argument constructor with default defense (e.g., 5)
        this(name, maxHealth, atk, 5); 
    }
    
    // --- Abstract Method (Must be implemented by subclasses) ---
    protected abstract void setupSkills();
    
    // --- LEVEL UP LOGIC (Non-Abstract and Overridable) ---

    /**
     * Base implementation of levelUp. Enemies will use this version, which does nothing.
     * Player classes will OVERRIDE this method to implement custom stat increases.
     */
    public void levelUp() {
        // Default (Enemy) behavior: Do nothing.
    }
    
    /**
     * Method used externally (e.g., by the GameManager) to trigger leveling and apply modifiers.
     * This method should only be called on Player entities.
     * @param atkModifier The ratio by which to increase attack.
     * @param defModifier The ratio by which to increase defense.
     */
    public void applyLevelUpModifiers(double atkModifier, double defModifier) {
        // 1. Call the subclass's specific levelUp logic (e.g., Player's override)
        levelUp(); 
        
        // 2. Apply global modifier logic, if necessary
        this.atk = (int)(this.atk * (1 + atkModifier));
        this.def = (int)(this.def * (1 + defModifier));
        this.maxHealth = (int)(this.maxHealth * (1 + atkModifier)); // Example: Health scales with Attack
        this.currentHealth = this.maxHealth; // Restore health on level up
        
        // 3. Increment the level counter
        this.level++;
    }

    // --- CORE COMBAT METHODS ---
    
    public void takeDamage(int damage) {
        this.currentHealth = Math.max(0, this.currentHealth - damage);
    }
    
    public void heal(int amount) {
        this.currentHealth = Math.min(this.maxHealth, this.currentHealth + amount);
    }
    
    public boolean isAlive() {
        return currentHealth > 0;
    }

    protected void addSkill(Skill skill) {
        this.skills.add(skill);
    }

    // --- GETTERS ---
    
    public String getName() { return name; }
    public int getMaxHealth() { return maxHealth; }
    public int getCurrentHealth() { return currentHealth; }
    public int getAtk() { return atk; }
    public int getDef() { return def; } 
    public int getLevel() { return level; } 

    // Returns a copy of the List for safety
    public List<Skill> getSkills() { return new ArrayList<>(skills); } 

    public boolean isShieldActive() { return isShieldActive; }
    public boolean isDodgeActive() { return isDodgeActive; }
    public boolean isBlinded() { return isBlinded; }

    // --- SETTERS ---
    
    public void setCurrentHealth(int currentHealth) { 
        this.currentHealth = Math.min(currentHealth, maxHealth);
    }

    public void setShieldActive(boolean status) { 
        this.isShieldActive = status;
    }

    public void setDodgeActive(boolean status) { 
        this.isDodgeActive = status;
    }

    public void setBlinded(boolean status) {
        this.isBlinded = status;
    }
    
    public void activateShield() {
        setShieldActive(true);
    }

     public void setAtk(int newAtk) { 
        this.atk = newAtk;
    }

    public void setDef(int newDef) { 
        this.def = newDef;
    }
}