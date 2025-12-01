package com.ror.gamemodel.Playable;

import com.ror.gamemodel.Entity;
import com.ror.gamemodel.Skill;
import com.ror.gameutil.BattleView;

public class SkyMage extends Entity {

    public SkyMage() {
        super("Sky Mage", 100, 25);
        setupSkills();
    }

    @Override
    protected void setupSkills() {
        // Skill 1: Tempest Gale
        addSkill(new Skill("Tempest Gale", "Slashes enemies with air blades.", 2, 35) {
            @Override
            public void apply(Entity user, Entity target, BattleView view) {
                target.takeDamage(user.getAtk() + 5); // scales with atk
            }
        });

        // Skill 2: Feather Strike
        addSkill(new Skill("Feather Strike", "High damage, high precision move.", 3, 45) {
            @Override
            public void apply(Entity user, Entity target, BattleView view) {
                target.takeDamage(user.getAtk() + 15);
            }
        });

        // Skill 3: Windwalk
        addSkill(new Skill("Windwalk", "Dodges next attack completely.", 4, 0) {
            @Override
            public void apply(Entity user, Entity target, BattleView view) {
                user.setDodgeActive(true); // uses Entity’s dodge logic
            }
        });
    }

    @Override
    public void levelUp() {
        maxHealth += (int)(maxHealth * 0.10); // lighter growth
        atk += (int)(atk * 0.20);             // stronger attack scaling
        currentHealth = maxHealth;
    }
}