//EXPERIMENTAL PHASE

package com.ror.gamemodel;

public class Andrew extends Entity {
    public Andrew() {
        super("Andrew the Timeblade", 115, 115, 18, 5);

        Skill chronoSlash = new Skill("Chrono Slash", 25, "Chrono", 0); // 2-turn cooldown
        Skill timeShield  = new Skill("Time Shield", 0, "Shield", 3);   // 3-turn cooldown
        Skill reverseFlow = new Skill("Reverse Flow", 0, "Reverse", 4); // 4-turn cooldown


        // set 3 skills
        setSkills(new Skill[] { chronoSlash, timeShield, reverseFlow });
    }
}