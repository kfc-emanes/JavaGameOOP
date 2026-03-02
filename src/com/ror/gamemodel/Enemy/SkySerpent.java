package com.ror.gamemodel.Enemy;

import com.ror.gamemodel.Entity;
import com.ror.gamemodel.Skill;
import com.ror.gameutil.BattleView;

public class SkySerpent extends Entity {

    public SkySerpent() {
        super("Sky Serpent", 120, 22); // HP 120, atk 22
        setupSkills();
    }

    @Override
    protected void setupSkills() {
        addSkill(new Skill("Wing Slash", "Aerial strike with wings.", 2) {
            @Override
            public void apply(Entity user, Entity target, BattleView view) {
                target.takeDamage(user.getAtk() + 5);
            }
        });

        addSkill(new Skill("Serpent’s Coil", "Constrains and damages the target.", 3) {
            @Override
            public void apply(Entity user, Entity target, BattleView view) {
                target.takeDamage(user.getAtk());
                target.setBlinded(true); // simple debuff
            }
        });
    }

    @Override
    public void levelUp() {
        // No leveling for enemies
    }
}