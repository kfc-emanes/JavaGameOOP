package com.ror.gamemodel.Playable;

import com.ror.gamemodel.Entity;
import com.ror.gamemodel.Skill;
import com.ror.gameutil.BattleView;

public class Tharn extends Entity {

    public Tharn() {
        super("Tharn", 130, 20);
        setupSkills();
    }

    @Override
    protected void setupSkills() {
        // Skill 1: Earthslam
        addSkill(new Skill("Earthslam", "Basic attack.", 1) {
            @Override
            public void apply(Entity user, Entity target, BattleView view) {
                target.takeDamage(user.getAtk() + 10);
            }
        });

        // Skill 2: Rockskin
        addSkill(new Skill("Rockskin", "Heals 40% of lost HP.", 3) {
            @Override
            public void apply(Entity user, Entity target, BattleView view) {
                int lostHP = user.getMaxHealth() - user.getCurrentHealth();
                int healAmount = (int)(lostHP * 0.4);
                user.heal(healAmount); // uses Entity’s heal logic
            }
        });

        // Skill 3: Seismic Shock
        addSkill(new Skill("Seismic Shock", "Moderate damage to enemy.", 2) {
            @Override
            public void apply(Entity user, Entity target, BattleView view) {
                target.takeDamage(user.getAtk() + 20);
            }
        });
    }

    @Override
    public void levelUp() {
        maxHealth += (int)(maxHealth * 0.20); // tanky growth
        atk += (int)(atk * 0.10);             // modest attack growth
        currentHealth = maxHealth;
    }
}