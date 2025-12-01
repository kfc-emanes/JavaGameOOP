package com.ror.gamemodel.Enemy;

import com.ror.gamemodel.Entity;

import com.ror.gamemodel.Skill;
import com.ror.gameutil.BattleView;
import com.ror.gameutil.BattleUtility;
import java.util.ArrayList;

public class Vorthnar extends Entity {

    public Vorthnar() {
        super("Vorthnar, The Eternal", 400, 400); 
        setupSkills(); 
    }

    @Override
    protected void setupSkills() {
        this.skills = new ArrayList<>();

        // Skill 1: Eternal Blast (Single Normal Attack)
        this.skills.add(new Skill("Eternal Blast", "A standard, powerful energy attack.", 1, 55) {
            
            @Override
            public void apply(Entity user, Entity target, BattleView view) {
                // Standard damage with a slight multiplier (1.2) appropriate for a boss
                int damage = BattleUtility.calculateDamage((int)(user.getAtk() * 1.2), target.getDef());
                target.takeDamage(damage);
                view.logMessage("💥 " + user.getName() + " unleashes an Eternal Blast, dealing " + damage + " damage!");
            }
        });
    }
}