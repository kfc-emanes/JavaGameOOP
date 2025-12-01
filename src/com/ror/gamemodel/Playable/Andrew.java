package com.ror.gamemodel.Playable;

import com.ror.gamemodel.Entity;
import com.ror.gamemodel.Skill;
import com.ror.gameutil.BattleView;
import com.ror.gameutil.BattleUtility;
import java.util.ArrayList;

public class Andrew extends Entity {

    public Andrew() {
        // Use the 5-argument canonical constructor: (name, maxHealth, attack, defense, speed)
        super("Andrew", 100, 20, 10, 10); 
        setupSkills();
    }

    @Override
    protected void setupSkills() {
        this.skills = new ArrayList<>();

        // Skill 1: Punch (Basic Attack)
        this.skills.add(new Skill("ChronoSlash", "A time slash.", 0, 30) {
            @Override
            public void apply(Entity user, Entity target, BattleView view) {
                // target is defined ONLY within this apply method's scope
                int calculatedAttack = (int)(user.getAtk() * 1.0);
                int damage = BattleUtility.calculateDamage(calculatedAttack, target.getDef());
                target.takeDamage(damage);
                view.logMessage("👊 " + user.getName() + " Slashes " + target.getName() + ", dealing " + damage + " damage.");
            }
        });

        // Skill 2: Guard (Defense Buff)
        this.skills.add(new Skill("Shield", "Raises defense for one turn.", 1, 0) {
            @Override
            public void apply(Entity user, Entity target, BattleView view) {
                user.setShieldActive(true);
                view.logMessage("🛡️ " + user.getName() + " enters a defensive stance.");
            }
        });

        this.skills.add(new Skill("Chrono bash", "Attack with your bare fists", 1, 0) {
            @Override
            public void apply(Entity user, Entity target, BattleView view) {
                user.setShieldActive(true);
                view.logMessage("🛡️ " + user.getName() + " Heals 40% of hp.");
            }
        });
    }
    
    /**
     * Overrides the empty levelUp() in Entity to provide custom stat increases.
     */
    @Override
    public void levelUp() {
        // Use the public setters (setAtk, setDef) from Entity instead of private field access.
        int newAtk = getAtk() + 2;
        int newDef = getDef() + 1;
        
        setAtk(newAtk);
        setDef(newDef);
        this.maxHealth += 5;
        this.currentHealth = this.maxHealth;
        
        // This section no longer contains any reference to 'target', resolving the error.
    }
}