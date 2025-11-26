package com.ror.gamemodel.Playable;

import com.ror.gamemodel.*;

public class FlameWarrior extends Entity {

    public FlameWarrior() {
        // name, maxHealth, currentHealth, atk, def
        super("Drax the Flamebound", 160, 160, 30, 18);

        Skill infernoStrike = new Skill("Inferno Strike", 20, "Attack", 2);
        Skill flameRoar = new Skill("Flame Roar", 0, "Buff", 3);
        Skill moltenWall = new Skill("Molten Wall", 0, "Defend", 4);

        setSkills(new Skill[]{infernoStrike, flameRoar, moltenWall});
    }
}
