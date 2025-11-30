package com.ror.gamemodel.Playable;

import com.ror.gamemodel.Entity;
import com.ror.gamemodel.Skill;
import com.ror.gameutil.BattleView;

public class FlameWarrior extends Entity {

    public FlameWarrior() {
        super("Flame Warrior", 150, 40);
        setupSkills();
    }

    @Override
    protected void setupSkills() {
        // Skill 1: Inferno Strike
        addSkill(new Skill("Inferno Strike", "Deals heavy fire damage.", 1) {
            @Override
            public void apply(Entity user, Entity target, BattleView view) {
                target.takeDamage(user.getAtk() + 20); // scales with atk
            }
        });

        // Skill 2: Flame Roar
        addSkill(new Skill("Flame Roar", "Damages the enemy with a fiery shout.", 0) {
            @Override
            public void apply(Entity user, Entity target, BattleView view) {
                target.takeDamage(user.getAtk() + 10);
            }
        });

        // Skill 3: Molten Fist
        addSkill(new Skill("Molten Fist", "A powerful molten punch.", 2) {
            @Override
            public void apply(Entity user, Entity target, BattleView view) {
                target.takeDamage(user.getAtk() + 25);
            }
        });
    }

    @Override
    public void levelUp() {
        maxHealth += (int)(maxHealth * 0.20); // Flame Warrior grows tankier
        atk += (int)(atk * 0.10);             // modest attack growth
        currentHealth = maxHealth;
    }
}