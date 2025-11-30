package com.ror.gamemodel.Playable;

import com.ror.gameengine.BattlePanel;
import com.ror.gamemodel.*;

public class Andrew extends Entity {

    public Andrew() {
        super("Andrew the Timeblade", 115, 115, 18, 5);

        // Define skills as anonymous inner classes
        Skill chronoSlash = new Skill("Chrono Slash", 25, 1) {
            @Override
            public void apply(Entity user, Entity target, BattlePanel panel) {
                int damage = power + user.getAtk();
                target.takeDamage(damage);
                panel.log(user.getName() + " hits " + target.getName() + " for " + damage + " damage!");
            }
        };

        Skill timeShield = new Skill("Time Shield", 0, 2) {
            @Override
            public void apply(Entity user, Entity target, BattlePanel panel) {
                user.setShieldActive(true);
                panel.log(user.getName() + " activates Time Shield!");
            }
        };

        Skill reverseFlow = new Skill("Reverse Flow", 0, 2) {
            @Override
            public void apply(Entity user, Entity target, BattlePanel panel) {
                int heal = (int)Math.ceil((user.getMaxHealth() - user.getCurrentHealth()) * 0.5);
                user.setCurrentHealth(Math.min(user.getMaxHealth(), user.getCurrentHealth() + heal));
                panel.log(user.getName() + " heals for " + heal + " HP!");
            }
        };

        // Assign the skills to the entity
        setSkills(new Skill[] { chronoSlash, timeShield, reverseFlow });
    }
}
