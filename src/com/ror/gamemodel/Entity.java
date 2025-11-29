package com.ror.gamemodel;

public class Entity {
    public String name;
    public int maxHealth;
    public int currHealth;
    public int atk;
    public int def;
    public Skill[] skills;
    public int currentCooldown;
    public int level = 0;

    public Entity(String name, int maxHealth, int currHealth, int atk, int def) {
        this.name = name;
        this.maxHealth = maxHealth;
        this.currHealth = currHealth;
        this.atk = atk;
        this.def = def;
        this.skills = new Skill[3];
    }

    public String getName() {
        return name;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getCurrentHealth() {
        return currHealth;
    }

    public int getAtk() {
        return atk;
    }

    public int getDef() {
        return def;
    }

    public void setCurrentHealth(int health) {
        this.currHealth = Math.max(0, Math.min(health, maxHealth));
    }

    public void setCurrentCooldown(int cooldown) {
        this.currentCooldown = cooldown;
    }

    public Skill[] getSkills() {
        return skills;
    }

    public void takeDamage(int dmg) {
        int actualDamage = Math.max(0, dmg - def);
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
            skill.use(this, target);
        } else {
            System.out.println("Invalid skill slot or no skill equipped!");
        }
    }

    public Skill getSkillByName(String name) {
    for (Skill skill : skills) {
        if (skill.getName().equalsIgnoreCase(name)) {
            return skill;
        }
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

        // Reset all skill cooldowns
        if (skills != null) {
            for (Skill skill : skills) {
                if (skill != null) skill.resetCooldown(); // reset to 0
            }
        }
        level++;
        System.out.println(name + " leveled up! Max HP +" + hpIncrease + ", ATK +" + atkIncrease);
        System.out.println("All skill cooldowns have been reset!");
    }

    public int getLevel() {
        return level;
    }
}