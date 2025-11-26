package com.ror.gamemodel;

public class Tharn extends Entity {
    public Tharn() {
        super("Tharn, the Stone Golem", 200, 200, 16, 18);

        Skill earthSlam = new Skill("Earth Slam", 20, "Stun", 2);     // deals damage and stuns
        Skill rockskin = new Skill("Rockskin", 0, "Heal", 4);  // recovers 40% hp lost
        Skill seismicShock = new Skill("Seismic Shock", 22, "AoE", 3);// ground AoE damage

        setSkills(new Skill[] { earthSlam, rockskin, seismicShock });
    }
}