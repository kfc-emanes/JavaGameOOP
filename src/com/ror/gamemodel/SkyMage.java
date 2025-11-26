package com.ror.gamemodel;

public class SkyMage extends Entity {

    public SkyMage() {
        super("Flashley the Windwhisperer", 120, 120, 25, 14);

        Skill tempestGale = new Skill("Tempest Gale", 22, "Attack", 2);
        Skill featherBarrier = new Skill("Feather Barrier", 0, "Heal", 2);
        Skill windwalk = new Skill("Windwalk", 0, "Dodge", 2);

        setSkills(new Skill[]{tempestGale, featherBarrier, windwalk});
    }
}
