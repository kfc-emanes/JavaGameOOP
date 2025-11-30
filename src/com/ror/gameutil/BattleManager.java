package com.ror.gameutil;

import com.ror.gamemodel.Entity;
import com.ror.gamemodel.Skill;
import com.ror.gamemodel.Enemy.Cultist;
import com.ror.gamemodel.Enemy.Goblin; // Import the initial enemy class

import javax.swing.Timer;
import javax.swing.JOptionPane; 

/**
 * Manages the state and flow of the battle, acting as the Controller in the MVC pattern.
 * It coordinates the Model (Entity/Skill) and the View (BattleView/BattlePanel).
 */
public class BattleManager {
    
    private final BattleView view; 
    private Entity player;
    private Entity enemy;
    
    public boolean playerTurn = true; 
    private String mode = "Tutorial";

    private final int ENEMY_TURN_DELAY = 1000;
    private final int NEXT_BATTLE_DELAY = 700;

    // =================================================================
    // PRIMARY CONSTRUCTOR
    // =================================================================
    // Keep this constructor for external use if needed
    public BattleManager(Entity player, Entity enemy, BattleView view) {
        this.player = player;
        this.enemy = enemy;
        this.view = view;
    }

    // Secondary Constructor (Used by BattlePanel to initialize manager with the view)
    public BattleManager(BattleView view) {
        this.view = view;
    }
    
    // Setters for flexibility
    public void setPlayer(Entity player) { this.player = player; }
    public void setEnemy(Entity enemy) { this.enemy = enemy; }
    
    public Entity getPlayer() { return player; }
    public Entity getEnemy() { return enemy; }

    // =================================================================
    // CORE BATTLE FLOW
    // =================================================================

    /**
     * Starts the battle, setting the chosen player and the first enemy.
     * @param chosenPlayer The Entity representing the player character.
     */
    public void startBattle(Entity chosenPlayer) {
    this.player = chosenPlayer;
    this.enemy = new Goblin(); // Or whatever the first enemy is
    this.mode = "Tutorial";

    // 1. Reset all combat effects on the newly loaded entities
    BattleUtility.resetCombatEffects(player, enemy);
    
    // 2. Reset skills and cooldowns
    resetCooldownsOnKill(); 
    // ^ This method calls view.updateSkillButtons() which REQUIRES skillBtn1 to be initialized

    // 3. Log start messages
    view.logMessage("A battle begins! " + player.getName() + " vs. " + enemy.getName() + ".");
    
    // 4. Update the display *after* everything is set up.
    view.updateDisplay(); // Can safely update HP bars now

    this.playerTurn = true; // Player goes first
    view.setSkillButtonsEnabled(true);
}
    
    // The rest of the methods remain unchanged:
    
    public void processPlayerAction(int slot) {
        if (!playerTurn) {
            view.logMessage("It's not your turn!");
            return;
        }
        
        // Use toArray for safe indexed access
        Skill[] skills = player.getSkills().toArray(new Skill[0]);
        
        if (slot >= skills.length) {
            view.logMessage("Invalid skill slot selected.");
            return;
        }
        
        Skill skill = skills[slot]; 

        if (skill.isOnCooldown()) {
            view.logMessage(skill.getName() + " is on cooldown! " + skill.getCurrentCooldown() + " turns remaining.");
            return;
        }

        skill.apply(player, enemy, view); 
        skill.triggerCooldown();

        view.updateDisplay();
        view.setSkillButtonsEnabled(false); 

        if (!enemy.isAlive()) {
            handleEnemyDefeat();
            return;
        }

        playerTurn = false;
        Timer enemyTimer = new Timer(ENEMY_TURN_DELAY, e -> enemyTurn());
        enemyTimer.setRepeats(false);
        enemyTimer.start();
    }

    private void enemyTurn() {
        if (!enemy.isAlive()) {
            handleEnemyDefeat();
            return;
        }

        BattleUtility.executeEnemyAttack(enemy, player, view);
        
        view.updateDisplay(); 
        
        tickPlayerCooldowns();
        view.updateSkillButtons();

        if (!player.isAlive()) {
            view.logMessage("💀 You have been defeated! Game Over.");
            view.setSkillButtonsEnabled(false);
            return;
        }

        playerTurn = true;
        view.logMessage("Your turn! Choose your next skill.");
        view.setSkillButtonsEnabled(true);
        view.updateSkillButtons(); 
    }

    private void handleEnemyDefeat() {
        view.logMessage("🏆 You defeated " + enemy.getName() + "!");
        
        // Calls the no-argument levelUp()
        player.levelUp(); 
        resetCooldownsOnKill();
        
        Timer nextBattleTimer = new Timer(NEXT_BATTLE_DELAY, e -> {
            ((Timer) e.getSource()).stop();
            nextEnemyOrRealm();
        });
        nextBattleTimer.setRepeats(false);
        nextBattleTimer.start();
    }

    // =================================================================
    // PROGRESSION LOGIC
    // =================================================================
    
    private void nextEnemyOrRealm() {
        view.logMessage("\n--- Transitioning to Next Encounter ---\n");
        
        if (mode.equals("Tutorial")) {
            if (enemy instanceof Goblin) {
                enemy = new Cultist();
                view.logMessage("🔥 New enemy: " + enemy.getName());
            } else if (enemy instanceof Cultist) {
                mode = "Realm1";
                healPlayerFull(); 
                view.showProgressionMessage("Chapter I: The Rift Opens", "🏁 Tutorial complete! Welcome to Realm 1.");
            }
        }
        
        if (enemy != null) {
            BattleUtility.resetCombatEffects(player, enemy);
            resetCooldownsOnKill();
            view.updateDisplay();
            view.setSkillButtonsEnabled(true);
        }
    }

    // =================================================================
    // UTILITY METHODS
    // =================================================================

    private void healPlayerFull() {
        player.setCurrentHealth(player.getMaxHealth()); 
        view.logMessage("Regained Vitality!");
    }

    private void tickPlayerCooldowns() {
        for (Skill s : player.getSkills()) s.reduceCooldown();
    }
    
    private void resetCooldownsOnKill() {
        if (player == null) {
            System.err.println("Warning: Attempted to reset cooldowns before player was set.");
            return;
        }
        for (Skill s : player.getSkills()) s.resetCooldown();
        view.updateSkillButtons();
    }
    
    public void confirmBackToMenu() {
        int confirm = view.showConfirmDialog("Confirm Exit", "Return to Main Menu? Progress will be lost.");
        
        if (confirm == JOptionPane.YES_OPTION) { 
            view.transitionToMenu();
        }
    }
}