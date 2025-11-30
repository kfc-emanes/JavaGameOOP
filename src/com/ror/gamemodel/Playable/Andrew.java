package com.ror.gamemodel.Playable;

import com.ror.gamemodel.*;

public class Andrew extends Entity {

    public Andrew() {
        super("Andrew the Timeblade", 115, 115, 18, 5);

        // Define skills as anonymous inner classes
        Skill chronoSlash = new Skill("Chrono Slash", 25, 1) {
            @Override
            public void apply(Entity user, Entity target) {
                int damage = power + user.getAtk();
                target.takeDamage(damage);
                System.out.println(user.getName() + " hits " + target.getName() + " for " + damage + " damage!");
            }
        };

        Skill timeShield = new Skill("Time Shield", 0, 3) {
            @Override
            public void apply(Entity user, Entity target) {
                user.setShieldActive(true);
                System.out.println(user.getName() + " activates Time Shield!");
            }
        };

        Skill reverseFlow = new Skill("Reverse Flow", 0, 4) {
            @Override
            public void apply(Entity user, Entity target) {
                int heal = (int)Math.ceil((user.getMaxHealth() - user.getCurrentHealth()) * 0.5);
                user.setCurrentHealth(Math.min(user.getMaxHealth(), user.getCurrentHealth() + heal));
                System.out.println(user.getName() + " heals for " + heal + " HP!");
            }
        };

        // Assign the skills to the entity
        setSkills(new Skill[] { chronoSlash, timeShield, reverseFlow });
    }
}
