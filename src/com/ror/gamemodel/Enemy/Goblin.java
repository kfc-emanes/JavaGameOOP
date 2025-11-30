package com.ror.gamemodel.Enemy;

import com.ror.gamemodel.Entity;
import com.ror.gamemodel.Skill;
import com.ror.gameutil.BattleView;

public class Goblin extends Entity {

    public Goblin() {
        super("Goblin", 75, 12); // HP 75, atk 12
        setupSkills();
    }

    @Override
    protected void setupSkills() {
        // Goblin only has one basic attack
        addSkill(new Skill("Claw Swipe", "A quick claw attack.", 1) {
            @Override
            public void apply(Entity user, Entity target, BattleView view) {
                target.takeDamage(user.getAtk());
            }
        });
    }

    @Override
    public void levelUp() {
        // Enemies don’t need leveling in a simple project
    }
}