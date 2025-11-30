package com.ror.gamemodel.Playable;

import com.ror.gamemodel.*;

public class Tharn extends Entity {

    public Tharn() {
        super("Tharn, the Stone Golem", 200, 200, 16, 18);

        Skill earthSlam = new Skill("Earth Slam", 22, 2) {
            @Override
            public void apply(Entity user, Entity target) {
                int damage = power + user.getAtk();
                target.takeDamage(damage);
                System.out.println(user.getName() + " hits " + target.getName() + " with Earth Slam for " + damage + " damage!");
            }
        };

        Skill rockskin = new Skill("Rockskin", 0, 4) {
            @Override
            public void apply(Entity user, Entity target) {
                int heal = (int) Math.ceil((user.getMaxHealth() - user.getCurrentHealth()) * 0.4);
                user.setCurrentHealth(Math.min(user.getMaxHealth(), user.getCurrentHealth() + heal));
                System.out.println(user.getName() + " uses Rockskin and heals for " + heal + " HP!");
            }
        };

        Skill seismicShock = new Skill("Seismic Shock", 25, 3) {
            @Override
            public void apply(Entity user, Entity target) {
                int damage = power + user.getAtk();
                target.takeDamage(damage);
                System.out.println(user.getName() + " slams the ground with Seismic Shock for " + damage + " damage!");
            }
        };

        setSkills(new Skill[]{earthSlam, rockskin, seismicShock});
    }
}
