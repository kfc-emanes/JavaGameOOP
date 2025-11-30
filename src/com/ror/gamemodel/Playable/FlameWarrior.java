package com.ror.gamemodel.Playable;

import com.ror.gamemodel.*;

public class FlameWarrior extends Entity {

    public FlameWarrior() {
        super("Drax the Flamebound", 160, 160, 30, 18);
        
        //blud dosen't need unique skills to defeat the boss
        Skill infernoStrike = new Skill("Inferno Strike", 20, 2) {
            @Override
            public void apply(Entity user, Entity target) {
                int damage = this.getPower() + user.getAtk();
                target.takeDamage(damage);
                System.out.println(user.getName() + " hits " + target.getName() + " with Inferno Strike for " + damage + " damage!");
            }
        };

        Skill flameRoar = new Skill("Flame Roar", 0, 3) {
            @Override
            public void apply(Entity user, Entity target) {
                System.out.println(user.getName() + " uses Flame Roar to intimidate the enemy!");
            }
        };

        Skill moltenWall = new Skill("Molten Wall", 0, 4) {
            @Override
            public void apply(Entity user, Entity target) {
                System.out.println(user.getName() + " braces behind Molten Wall!");
            }
        };

        setSkills(new Skill[]{infernoStrike, flameRoar, moltenWall});
    }
}
