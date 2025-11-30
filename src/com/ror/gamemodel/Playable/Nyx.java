package com.ror.gamemodel.Playable;

import com.ror.gamemodel.*;

public class Nyx extends Entity {
    public Nyx() {
        super("Nyx Shadowveil", 95, 95, 28, 4);

        Skill shadowBlink = new Skill("Shadow Blink", 35, 2) { // teleport + strong hit
            @Override
            public void apply(Entity user, Entity target) {
                int damage = power + user.getAtk();
                target.takeDamage(damage);
                System.out.println(user.getName() + " teleports and strikes " + target.getName() + " for " + damage + " damage!");
            }
        };

        Skill nightPoison = new Skill("Night Poison", 10, 2) { // applies poison + slow
            @Override
            public void apply(Entity user, Entity target) {
                target.takeDamage(power); // simple poison damage for now
                System.out.println(user.getName() + " poisons " + target.getName() + " for " + power + " damage!");
                // Here you could also add status effects like slow if you implement them
            }
        };

        Skill darkVeil = new Skill("Dark Veil", 0, 3) { // blind / evade
            @Override
            public void apply(Entity user, Entity target) {
                target.setBlinded(true); // you'll need to implement setBlinded in Entity
                System.out.println(user.getName() + " casts Dark Veil! " + target.getName() + " is blinded!");
            }
        };

        setSkills(new Skill[]{shadowBlink, nightPoison, darkVeil});
    }
}
