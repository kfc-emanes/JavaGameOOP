package com.ror.gameengine;

import com.ror.gamemodel.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class BattlePanel extends JPanel {
    private JTextArea battleLog;
    private JButton skillBtn1, skillBtn2, skillBtn3, backBtn;
    private JLabel playerHPLabel, enemyHPLabel, playerNameLabel, enemyNameLabel;

    private Entity player;
    private Entity enemy;
    private boolean playerTurn = true;

    private boolean playerShieldActive = false;
    private int delayedDamageToEnemy = 0;
    private int lastDamageTakenByPlayer = 0;

    public BattlePanel() {
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);

        // --- Top: HP and names ---
        JPanel top = new JPanel(new GridLayout(2, 2));
        top.setBackground(Color.BLACK);

        playerNameLabel = new JLabel("Player", SwingConstants.CENTER);
        enemyNameLabel = new JLabel("Enemy", SwingConstants.CENTER);
        playerHPLabel = new JLabel("HP: --", SwingConstants.CENTER);
        enemyHPLabel = new JLabel("HP: --", SwingConstants.CENTER);

        Color white = Color.WHITE;
        playerNameLabel.setForeground(white);
        enemyNameLabel.setForeground(white);
        playerHPLabel.setForeground(white);
        enemyHPLabel.setForeground(white);

        top.add(playerNameLabel);
        top.add(enemyNameLabel);
        top.add(playerHPLabel);
        top.add(enemyHPLabel);

        add(top, BorderLayout.NORTH);

        // --- Center: battle log ---
        battleLog = new JTextArea();
        battleLog.setEditable(false);
        battleLog.setBackground(Color.BLACK);
        battleLog.setForeground(Color.GREEN);
        battleLog.setFont(new Font("Monospaced", Font.PLAIN, 13));
        add(new JScrollPane(battleLog), BorderLayout.CENTER);

        // --- Bottom: skill buttons ---
        JPanel bottom = new JPanel(new GridLayout(1, 4, 8, 8));
        bottom.setBackground(Color.DARK_GRAY);

        skillBtn1 = new JButton("Skill 1");
        skillBtn2 = new JButton("Skill 2");
        skillBtn3 = new JButton("Skill 3");
        backBtn = new JButton("Back");

        bottom.add(skillBtn1);
        bottom.add(skillBtn2);
        bottom.add(skillBtn3);
        bottom.add(backBtn);

        add(bottom, BorderLayout.SOUTH);
    }

    public void startBattle(Entity chosenPlayer) {
        this.player = chosenPlayer;
        this.enemy = new Goblin(); // always fights goblin for now

        playerShieldActive = false;
        delayedDamageToEnemy = 0;
        lastDamageTakenByPlayer = 0;
        playerTurn = true;

        playerNameLabel.setText(player.getName());
        enemyNameLabel.setText(enemy.getName());
        updateHPLabels();

        Skill[] skills = player.getSkills();
        skillBtn1.setText(skills[0].getName());
        skillBtn2.setText(skills[1].getName());
        skillBtn3.setText(skills[2].getName());

        // Remove old listeners so we don’t stack duplicates
        clearListeners();

        // Assign skill actions
        skillBtn1.addActionListener(e -> playerUseSkill(0));
        skillBtn2.addActionListener(e -> playerUseSkill(1));
        skillBtn3.addActionListener(e -> playerUseSkill(2));

        battleLog.setText("");
        log("⚔️ Battle Start! " + player.getName() + " vs " + enemy.getName());
        log("Choose a skill to begin your turn.");
    }

    private void clearListeners() {
        for (ActionListener al : skillBtn1.getActionListeners()) skillBtn1.removeActionListener(al);
        for (ActionListener al : skillBtn2.getActionListeners()) skillBtn2.removeActionListener(al);
        for (ActionListener al : skillBtn3.getActionListeners()) skillBtn3.removeActionListener(al);
    }

    private void playerUseSkill(int index) {
    if (!playerTurn) return;
    Skill s = player.getSkills()[index];

    // 🔹 1. Check if skill is on cooldown
    if (s.isOnCooldown()) {
        log("⏳ " + s.getName() + " is on cooldown for " + s.getCurrentCooldown() + " more turns!");
        return;
    }

    log(player.getName() + " uses " + s.getName() + "!");

    String type = s.getType();
    switch (type.toLowerCase()) {
        case "chrono":
            delayedDamageToEnemy = s.getPower();
            log("⏳ Chrono Slash — damage will trigger after the Goblin’s turn!");
            break;
        case "shield":
            playerShieldActive = true;
            log("🛡️ Time Shield activated! You’ll block the next attack.");
            break;
        case "reverse":
            int heal = lastDamageTakenByPlayer > 0 ? lastDamageTakenByPlayer : 10;
            player.setCurrentHealth(Math.min(player.getMaxHealth(), player.getCurrentHealth() + heal));
            log("♻️ Reverse Flow restores " + heal + " HP!");
            updateHPLabels();
            break;
        default:
            enemy.takeDamage(s.getPower() + player.getAtk());
            log("💥 " + enemy.getName() + " takes " + (s.getPower() + player.getAtk()) + " damage!");
            updateHPLabels();
            break;
    }

    // 🔹 2. Trigger cooldown for this skill
    s.triggerCooldown();

    playerTurn = false;

    Timer timer = new Timer(900, e -> {
        ((Timer) e.getSource()).stop();
        enemyTurn();
    });
    timer.setRepeats(false);
    timer.start();
}


    private void enemyTurn() {
        if (!enemy.isAlive()) {
            checkEnd();
            return;
        }

        if (playerShieldActive) {
            log("🛡️ Goblin attacks, but your Time Shield blocks it!");
            playerShieldActive = false;
            lastDamageTakenByPlayer = 0;
        } else {
            int damage = Math.max(0, enemy.getAtk() - player.getDef());
            player.setCurrentHealth(player.getCurrentHealth() - damage);
            lastDamageTakenByPlayer = damage;
            log("👹 Goblin attacks! You take " + damage + " damage.");
            updateHPLabels();
        }

        if (delayedDamageToEnemy > 0 && enemy.isAlive()) {
            log("💫 Chrono Slash triggers — " + delayedDamageToEnemy + " delayed damage!");
            enemy.takeDamage(delayedDamageToEnemy);
            delayedDamageToEnemy = 0;
            updateHPLabels();
        }

        checkEnd();

        if (player.isAlive() && enemy.isAlive()) {
            playerTurn = true;
            log("It’s your turn again — choose a skill.");
        }
        for (Skill skill : player.getSkills()) {
            skill.reduceCooldown();
        }

    }

    private void updateHPLabels() {
        playerHPLabel.setText("HP: " + player.getCurrentHealth() + "/" + player.getMaxHealth());
        enemyHPLabel.setText("HP: " + enemy.getCurrentHealth() + "/" + enemy.getMaxHealth());
    }

    private void log(String msg) {
        battleLog.append(msg + "\n");
    }

    private void checkEnd() {
        if (!enemy.isAlive()) {
            log("🏆 You defeated the Goblin!");
            disableSkillButtons();
        } else if (!player.isAlive()) {
            log("💀 You were defeated...");
            disableSkillButtons();
        }
    }

    private void disableSkillButtons() {
        skillBtn1.setEnabled(false);
        skillBtn2.setEnabled(false);
        skillBtn3.setEnabled(false);
    }
}