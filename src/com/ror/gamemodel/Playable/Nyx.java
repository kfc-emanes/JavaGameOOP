package com.ror.gamemodel.Playable;

import com.ror.gamemodel.*;

public class Nyx extends Entity {
    public Nyx() {
        super("Nyx Shadowveil", 95, 95, 28, 4);

        Skill shadowBlink = new Skill("Shadow Blink", 35, "TeleportAttack", 2); // teleport + strong hit
        Skill nightPoison = new Skill("Night Poison", 10, "Poison", 3);         // applies poison + slow
        Skill darkVeil = new Skill("Dark Veil", 0, "Stealth", 4);              // evade for 1 turn

        setSkills(new Skill[] { shadowBlink, nightPoison, darkVeil });
    }
}