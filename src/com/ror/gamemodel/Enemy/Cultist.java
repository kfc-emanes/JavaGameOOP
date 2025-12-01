package com.ror.gamemodel.Enemy;

import com.ror.gamemodel.Entity;
import com.ror.gamemodel.Skill;
import com.ror.gameutil.BattleView;

public class Cultist extends Entity {

    public Cultist() {
        super("Cultist of the Rift", 95, 20); // HP 95, atk 20
        setupSkills();
    }

    @Override
    protected void setupSkills() {
        addSkill(new Skill("Dark Chant", "A sinister chant that damages the target.", 2, 10) {
            @Override
            public void apply(Entity user, Entity target, BattleView view) {
                target.takeDamage(user.getAtk());
            }
        });
    }

    @Override
    public void levelUp() {
        // Enemies don’t level up
    }
}