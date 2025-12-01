package com.ror.gamemodel.Enemy;

import com.ror.gamemodel.Entity;
import com.ror.gamemodel.Skill;
import com.ror.gameutil.BattleView;

public class MoltenImp extends Entity {

    public MoltenImp() {
        super("Molten Imp", 80, 18); // HP 80, atk 18
        setupSkills();
    }

    @Override
    protected void setupSkills() {
        addSkill(new Skill("Fire Burst", "Small AoE fire attack.", 3, 15) {
            @Override
            public void apply(Entity user, Entity target, BattleView view) {
                target.takeDamage(user.getAtk());
            }
        });
    }

    @Override
    public void levelUp() {
        // No leveling for enemies
    }
}