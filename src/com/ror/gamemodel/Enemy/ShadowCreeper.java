package com.ror.gamemodel.Enemy;

import com.ror.gamemodel.Entity;
import com.ror.gamemodel.Skill;
import com.ror.gameutil.BattleView;

public class ShadowCreeper extends Entity {

    public ShadowCreeper() {
        super("Shadow Creeper", 100, 20); // HP 100, atk 20
        setupSkills();
    }

    @Override
    protected void setupSkills() {
        // Poison attack
        addSkill(new Skill("Venom Strike", "Poisons the target.", 2) {
            @Override
            public void apply(Entity user, Entity target, BattleView view) {
                target.takeDamage(user.getAtk());
                target.setBlinded(true); // reuse blind as a simple debuff
            }
        });

        // Drain attack
        addSkill(new Skill("Life Drain", "Steals health from the target.", 3) {
            @Override
            public void apply(Entity user, Entity target, BattleView view) {
                target.takeDamage(user.getAtk());
                user.heal(user.getAtk() / 2); // heal half of damage dealt
            }
        });
    }

    @Override
    public void levelUp() {
        // No leveling for enemies
    }
}