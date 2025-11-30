package com.ror.gamemodel;

public abstract class Entity {
    public String name;
    public int maxHealth;
    public int currHealth;
    public int atk;
    public int def;
    public Skill[] skills;
    public int currentCooldown;
    public int level = 0;

    protected boolean shieldActive = false; // track shield status
    private boolean dodgeActive = false;
    private boolean blinded = false;

    public Entity(String name, int maxHealth, int currHealth, int atk, int def) {
        this.name = name;
        this.maxHealth = maxHealth;
        this.currHealth = currHealth;
        this.atk = atk;
        this.def = def;
        this.skills = new Skill[3];
    }

    public String getName() { return name; }
    public int getMaxHealth() { return maxHealth; }
    public int getCurrentHealth() { return currHealth; }
    public int getAtk() { return atk; }
    public int getDef() { return def; }
    public Skill[] getSkills() { return skills; }
    public int getLevel() { return level; }

    public void setCurrentHealth(int health) {
        this.currHealth = Math.max(0, Math.min(health, maxHealth));
    }

    public void takeDamage(int dmg) {
        int actualDamage = Math.max(0, dmg - def);
        if (shieldActive) {
            actualDamage = 0; // shield blocks damage
            shieldActive = false; // shield consumed
            System.out.println(name + "'s shield blocked the attack!");
        }
        currHealth -= actualDamage;
        if (currHealth < 0) currHealth = 0;
        System.out.println(name + " took " + actualDamage + " damage! " + name + " has " + currHealth + " HP left.");
    }

    public void attack(Entity target) {
        System.out.println(name + " attacks " + target.getName() + " for " + atk + " damage!");
        target.takeDamage(atk);
    }

    public boolean isAlive() {
        return currHealth > 0;
    }

    public void setSkill(int slot, Skill skill) {
        if (slot >= 0 && slot < skills.length) {
            skills[slot] = skill;
        } else {
            throw new IllegalArgumentException("Invalid skill slot: " + slot);
        }
    }

    public void useSkill(int slot, Entity target) {
        if (slot >= 0 && slot < skills.length && skills[slot] != null) {
            Skill skill = skills[slot];
            System.out.println(name + " uses " + skill.getName() + " on " + target.getName() + "!");
            skill.apply(this, target); // now using apply()
        } else {
            System.out.println("Invalid skill slot or no skill equipped!");
        }
    }

    public Skill getSkillByName(String skillName) {
        for (Skill skill : skills) {
            if (skill.getName().equalsIgnoreCase(skillName)) return skill;
        }
        return null;
    }

    public void setSkills(Skill[] skills) {
        this.skills = skills;
    }

    public void levelUp(double hpPercent, double atkPercent) {
        int hpIncrease = (int) Math.round(maxHealth * hpPercent);
        int atkIncrease = (int) Math.round(atk * atkPercent);

        maxHealth += hpIncrease;
        currHealth += hpIncrease;
        atk += atkIncrease;

        if (skills != null) {
            for (Skill skill : skills) {
                if (skill != null) skill.resetCooldown();
            }
        }
        level++;
        System.out.println(name + " leveled up! Max HP +" + hpIncrease + ", ATK +" + atkIncrease);
        System.out.println("All skill cooldowns have been reset!");
    }

    // LOOK OVER HERE -- NEW shield setter & getter for characters, this one's for andrew
    public void setShieldActive(boolean active) {
        this.shieldActive = active;
        if (active) System.out.println(name + " activates a shield!");
    }

    public boolean isShieldActive() {
        return shieldActive;
    }

    //for Flashey(windwalk)
    public void setDodgeActive(boolean active) {
        this.dodgeActive = active;
    }

    public boolean isDodgeActive() {
        return dodgeActive;
    }

    //for Nyx(dark veil)
    public boolean isBlinded() {
        return blinded;
    }

    public void setBlinded(boolean blinded) {
        this.blinded = blinded;
        if (blinded) {
            System.out.println(name + " is now blinded!");
        } else {
            System.out.println(name + " is no longer blinded!");
        }
    }

}