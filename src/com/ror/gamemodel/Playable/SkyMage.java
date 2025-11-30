package com.ror.gamemodel.Playable;

import com.ror.gameengine.BattlePanel;
import com.ror.gamemodel.*;

public class SkyMage extends Entity {

    public SkyMage() {
        super("Flashey the Windwhisperer", 120, 120, 25, 14);

        Skill tempestGale = new Skill("Tempest Gale", 22, 2) {
            @Override
            public void apply(Entity user, Entity target, BattlePanel panel) {
                int damage = power + user.getAtk();
                target.takeDamage(damage);
                panel.log(user.getName() + " unleashes Tempest Gale for " + damage + " damage!");
            }
        };

        Skill featherBarrier = new Skill("Feather Barrier", 0, 3) {
            @Override
            public void apply(Entity user, Entity target, BattlePanel panel) {
                int heal = (int) Math.ceil((user.getMaxHealth() - user.getCurrentHealth()) * 0.4);
                user.setCurrentHealth(Math.min(user.getMaxHealth(), user.getCurrentHealth() + heal));
                panel.log(user.getName() + " restores " + heal + " HP with Feather Barrier!");
            }
        };

        Skill windwalk = new Skill("Windwalk", 0, 2) {
            @Override
            public void apply(Entity user, Entity target, BattlePanel panel) {
                user.setDodgeActive(true);
                panel.log(user.getName() + " activates Windwalk! Will dodge the next attack.");
            }
        };

        setSkills(new Skill[]{tempestGale, featherBarrier, windwalk});
    }
}
