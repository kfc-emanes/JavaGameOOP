package com.ror.gamemodel.Enemy;

import com.ror.gamemodel.Entity;
import com.ror.gamemodel.Skill;
import com.ror.gameutil.BattleView;
import com.ror.gameutil.BattleUtility;
import java.util.ArrayList;

/**
 * Represents General Zephra, a powerful enemy specializing in storm magic.
 * Extends the base Entity class and defines a single lightning-based attack.
 */
public class GeneralZephra extends Entity {

    public GeneralZephra() {
        // super(name, maxHealth, currentHealth, attackPower, baseDefense, speed)
        super("General Zephra", 180, 40, 15); // storm magic, rides a thunderbird
        setupSkills();
    }

    @Override
    protected void setupSkills() {
        this.skills = new ArrayList<>();

        // Skill 1: Lightning Bolt (Single Normal Attack)
        this.skills.add(new Skill("Lightning Bolt", "A fierce magical attack using storm energy.", 1) {
            @Override
            public void apply(Entity user, Entity target, BattleView view) {

                int damage = BattleUtility.calculateDamage((int)(user.getAtk() * 1.1), target.getDef());
                target.takeDamage(damage);

                view.logMessage("⚡ " + user.getName() + " conjures a Lightning Bolt, dealing " + damage + " damage.");
            }
        });
    }
}