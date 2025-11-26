package com.ror.gamemodel.Playable;

import com.ror.gamemodel.*;

public class SkyMage extends Entity {

    public SkyMage() {
        // name, maxHealth, currentHealth, atk, def
        super("Flashley the Windwhisperer", 120, 120, 25, 14);

        Skill tempestGale = new Skill("Tempest Gale", 22, "Attack", 2);
        Skill featherBarrier = new Skill("Feather Barrier", 0, "Defend", 3);
        Skill windwalk = new Skill("Windwalk", 0, "Evade", 3);

        setSkills(new Skill[]{tempestGale, featherBarrier, windwalk});
    }
}
