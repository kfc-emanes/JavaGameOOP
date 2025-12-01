package com.ror.gameutil;

import com.ror.gamemodel.Entity;

/**
 * Stateless utility class for common combat actions and calculations.
 * NOTE: The old file BattleUtil.java must be deleted to avoid class conflicts.
 */
public class BattleUtility { 

    /*
     * Executes the enemy's attack sequence, applying status checks (Blind, Dodge, Shield).
     * Arguments:
     * - attacker: The attacking entity (Enemy).
     * - target: The targeted entity (Player).
     * - view: The BattleView instance to log actions.
     */
    public static void executeEnemyAttack(Entity attacker, Entity target, BattleView view) {
        
        if (!attacker.isAlive() || !target.isAlive()) {
            return;
        }

        // 1. Check Attacker Status (Blinded)
        if (attacker.isBlinded()) {
            view.logMessage(attacker.getName() + " is blinded and misses!");
            attacker.setBlinded(false); 
            return;
        }

        // 2. Check Target Defensive Statuses (Dodge, Shield)
        if (target.isDodgeActive()) {
            view.logMessage(target.getName() + " dodges the attack!");
            target.setDodgeActive(false);
            return;
        }
        
        if (target.isShieldActive()) {
            view.logMessage(target.getName() + "'s shield blocks the attack!");
            target.setShieldActive(false);
            return;
        }
        
        // 3. Calculate and Apply Damage
        int damage = calculateDamage(attacker.getAtk(), target.getDef());
        target.takeDamage(damage);
        view.logMessage(attacker.getName() + " attacks for " + damage + " damage!");
    }

    /*
     * Calculates final damage after defense reduction. Damage is always at least 0.
     * Arguments:
     * - attack: The attacker's raw attack value.
     * - defense: The target's raw defense value.
     * Returns: The final damage dealt.
     */
    public static int calculateDamage(int atk, int defense) {
        return Math.max(0, atk - defense);
    }

    /*
     * Resets temporary combat effects (like single-turn buffs/debuffs) 
     * on both player and enemy, typically at the start of a new round or battle.
     */
    public static void resetCombatEffects(Entity player, Entity enemy) {
        if (player != null) {
            player.setShieldActive(false);
            player.setDodgeActive(false);
            player.setBlinded(false);
        }
        if (enemy != null) {
            enemy.setBlinded(false);
        }
    }
}