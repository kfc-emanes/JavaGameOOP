package com.ror.gamemodel.Playable;

import com.ror.gamemodel.Entity;
import com.ror.gamemodel.Skill;
import com.ror.gameutil.BattleView;

public class Nyx extends Entity {

    public Nyx() {
        super("Nyx", 110, 30);
        setupSkills();
    }

    @Override
    protected void setupSkills() {
        // Skill 1: Shadowblink
        addSkill(new Skill("Shadowblink", "Normal teleport attack.", 2, 40) {
            @Override
            public void apply(Entity user, Entity target, BattleView view) {
                target.takeDamage(user.getAtk() + 10);
            }
        });

        // Skill 2: Night's Cowl
        addSkill(new Skill("Night's Cowl", "Surprise attack dealing big damage.", 4, 30) {
            @Override
            public void apply(Entity user, Entity target, BattleView view) {
                target.takeDamage(user.getAtk() + 25);
            }
        });

        // Skill 3: Dark Veil
        addSkill(new Skill("Dark Veil", "Blinds enemies for next attack.", 3, 0) {
            @Override
            public void apply(Entity user, Entity target, BattleView view) {
                target.setBlinded(true); // uses Entity’s blind logic
            }
        });
    }

    @Override
    public void levelUp() {
        maxHealth += (int)(maxHealth * 0.10); // Nyx is agile, not tanky
        atk += (int)(atk * 0.20);             // higher attack growth
        currentHealth = maxHealth;
    }
}