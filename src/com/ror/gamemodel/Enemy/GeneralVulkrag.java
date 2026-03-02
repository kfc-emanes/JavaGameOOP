package com.ror.gamemodel.Enemy;

import com.ror.gamemodel.Entity;
import com.ror.gamemodel.Skill;
import com.ror.gameutil.BattleView;     // <--- CORRECTED IMPORT
import com.ror.gameutil.BattleUtility; // <--- CORRECTED IMPORT
import java.util.ArrayList;

/**
 * Represents General Vulkrag, a heavy-hitting enemy wielding a giant flame axe.
 * Extends the base Entity class and defines a single, powerful physical attack.
 */
public class GeneralVulkrag extends Entity {

    public GeneralVulkrag() {
        // super(name, maxHealth, attackPower, baseDefense, speed)
        super("General Vulkrag", 220, 45, 12); // wields giant flame axe
        setupSkills();
    }

    @Override
    protected void setupSkills() {
        // This 'skills' initialization now works if you change the field in Entity.java to 'protected'.
        this.skills = new ArrayList<>();

        // Skill 1: Flame Axe Swing (Single Normal Attack)
        this.skills.add(new Skill("Flame Axe Swing", "A devastating swing with a giant flame axe.", 1) {
            @Override
            public void apply(Entity user, Entity target, BattleView view) {
                int calculatedAttackPower = (int)(user.getAtk() * 1.4);
                
                int damage = BattleUtility.calculateDamage(calculatedAttackPower, target.getDef());
                target.takeDamage(damage);

                view.logMessage("🔥 " + user.getName() + " slams " + target.getName() + " with a Flame Axe Swing, dealing " + damage + " damage.");
            }
        });
    }

    // The 'levelUp' method is correctly removed here, assuming you remove the abstract method from Entity.java.
}