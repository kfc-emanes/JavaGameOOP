package com.ror.gameengine;

import com.ror.gamemodel.*;
import com.ror.gameutil.HoverButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class BattlePanel extends JPanel {
    public GameFrame parent;
    public JTextArea battleLog;
    public HoverButton skillBtn1, skillBtn2, skillBtn3, backBtn;
    public JLabel playerHPLabel, enemyHPLabel, playerNameLabel, enemyNameLabel, playerLevelLabel;

    private Entity player;
    private Entity enemy;
    public boolean playerTurn = true;

    int delayedDamageToEnemy = 0;
    int burnDamageToEnemy = 0;
    int burnTurnsRemaining = 0;

    public String mode = "Tutorial";
    public BattlePanel(GameFrame parent) {;
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);

        setupBattleLog();
        setupBottomButtons();
    }


    private void setupTopPanel() {
        JPanel top = new JPanel(new GridLayout(1, 2));
        top.setBackground(Color.BLACK);

        Font nameFont = new Font("SansSerif", Font.BOLD, 16);
        Font infoFont = new Font("SansSerif", Font.PLAIN, 14);
        Color white = Color.WHITE;

        // --- Player panel (left) ---
        JPanel playerPanel = new JPanel(new GridLayout(3, 1));
        playerPanel.setBackground(Color.BLACK);

        playerNameLabel = new JLabel(player.getName(), SwingConstants.CENTER);
        playerHPLabel = new JLabel("HP: " + player.getCurrentHealth() + "/" + player.getMaxHealth(), SwingConstants.CENTER);
        playerLevelLabel = new JLabel("Level: " + player.getLevel(), SwingConstants.CENTER);

        playerNameLabel.setFont(nameFont); playerNameLabel.setForeground(Color.white);
        playerHPLabel.setFont(infoFont); playerHPLabel.setForeground(Color.white);
        playerLevelLabel.setFont(infoFont); playerLevelLabel.setForeground(Color.white);

        playerPanel.add(playerNameLabel);
        playerPanel.add(playerHPLabel);
        playerPanel.add(playerLevelLabel);

        // --- Enemy panel (right) ---
        JPanel enemyPanel = new JPanel(new GridLayout(2, 1));
        enemyPanel.setBackground(Color.BLACK);

        enemyNameLabel = new JLabel(enemy.getName(), SwingConstants.CENTER);
        enemyHPLabel = new JLabel("HP: " + enemy.getCurrentHealth() + "/" + enemy.getMaxHealth(), SwingConstants.CENTER);

        enemyNameLabel.setFont(nameFont); enemyNameLabel.setForeground(white);
        enemyHPLabel.setFont(infoFont); enemyHPLabel.setForeground(white);

        enemyPanel.add(enemyNameLabel);
        enemyPanel.add(enemyHPLabel);

        // Add both sub-panels to the top panel
        top.add(playerPanel);
        top.add(enemyPanel);

        add(top, BorderLayout.NORTH);
    }


    private void setupBottomButtons() {
        JPanel bottom = new JPanel(new GridLayout(1, 4, 8, 8));
        bottom.setBackground(Color.DARK_GRAY);

        skillBtn1 = new HoverButton("Skill 1");
        skillBtn2 = new HoverButton("Skill 2");
        skillBtn3 = new HoverButton("Skill 3");
        backBtn = new HoverButton("Back");

        Font btnFont = new Font("SansSerif", Font.PLAIN, 16);
        skillBtn1.setFont(btnFont);
        skillBtn2.setFont(btnFont);
        skillBtn3.setFont(btnFont);
        backBtn.setFont(btnFont);

        backBtn.setEnabled(false); // unlock later after tutorial
        backBtn.addActionListener(e -> confirmBackToMenu());

        bottom.add(skillBtn1);
        bottom.add(skillBtn2);
        bottom.add(skillBtn3);
        bottom.add(backBtn);

        add(bottom, BorderLayout.SOUTH);
    }

    private void confirmBackToMenu() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Return to Main Menu? Progress will be lost.",
                "Confirm Exit", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            parent.showMenu();
        }
    }

    public void startBattle(Entity chosenPlayer) {
        this.player = chosenPlayer;
        this.enemy = new Goblin();
        resetPlayerEffects();
        battleLog.setText("");

        // Set tutorial enemy
        enemy = new Goblin();

        setupTopPanel();
        updateLabels();
        setupSkillButtons();

        log("⚔️ Battle Begins: " + player.getName() + " VS " + enemy.getName() + "!");
        JOptionPane.showMessageDialog(this,
                "Welcome to Realms of Riftborne. I see you have selected " + player.getName() + ". Here's a little let-you-know:\n" +
                    "[!] You are pitted against a succession of enemies. Defeat each one of them to get through the levels.\n" +
                    "[!] Defeating a miniboss will allow you to proceed to the next realm.\n" +
                    "[!] You retain full health after every major battle.\n" +
                    "[!] Your skills are your main method of attack, and certain skills will go on cooldown for a set amount of turns.\n" +
                    "Pick a skill to begin your turn!",
                    "Tutorial", JOptionPane.INFORMATION_MESSAGE);

        updateSkillButtons();
    }

    private void setupSkillButtons() {
        Skill[] skills = player.getSkills();
        clearSkillListeners();

        skillBtn1.setText(skills[0].getName());
        skillBtn2.setText(skills[1].getName());
        skillBtn3.setText(skills[2].getName());

        skillBtn1.addActionListener(e -> playerUseSkill(0));
        skillBtn2.addActionListener(e -> playerUseSkill(1));
        skillBtn3.addActionListener(e -> playerUseSkill(2));
    }

    private void clearSkillListeners() {
        for (ActionListener al : skillBtn1.getActionListeners()) skillBtn1.removeActionListener(al);
        for (ActionListener al : skillBtn2.getActionListeners()) skillBtn2.removeActionListener(al);
        for (ActionListener al : skillBtn3.getActionListeners()) skillBtn3.removeActionListener(al);
    }

    private void playerUseSkill(int slot) {
        Skill skill = player.getSkills()[slot];
        if (skill == null) return;

        if (skill.isOnCooldown()) {
            log(skill.getName() + " is on cooldown!");
            return;
        }

        // Use skill logic
        skill.apply(player, enemy, this);  
        updateLabels();        
        skill.triggerCooldown(); 

        // Check if enemy is dead
        if (!enemy.isAlive()) {
            log("✅ " + enemy.getName() + " defeated!");
            nextEnemyOrRealm();   
            return;
        }

        playerTurn = false;
        Timer enemyTimer = new Timer(1000, e -> enemyTurn()); 
        enemyTimer.setRepeats(false);
        enemyTimer.start();
    }

    private void enemyTurn() {
        if (!enemy.isAlive()) {
            handleEnemyDefeat();
            return;
        }

        // Burn damage
        if (burnTurnsRemaining > 0) {
            enemy.takeDamage(burnDamageToEnemy);
            burnTurnsRemaining--;
            log("🔥 Burn deals " + burnDamageToEnemy + " damage (" + burnTurnsRemaining + " turns left).");
            updateLabels();
            if (!enemy.isAlive()) { handleEnemyDefeat(); return; }
        }

        // Enemy attack logic
        // === Enemy attack logic ===
    if (enemy.isBlinded()) {
        log(enemy.getName() + " is blinded and misses!");
        enemy.setBlinded(false);
    }
    else if (player.isDodgeActive()) {
        log(player.getName() + " dodges the attack!");
        player.setDodgeActive(false);
    }
    else if (player.isShieldActive()) {
        log(player.getName() + "'s shield blocks the attack!");
        player.setShieldActive(false);
    }
    else {
        int damage = Math.max(0, enemy.getAtk() - player.getDef());
        player.takeDamage(damage);
        log(enemy.getName() + " attacks for " + damage + " damage!");
        updateLabels();
    }


        // Delayed enemy damage (e.g., Chrono Slash)
        if (delayedDamageToEnemy > 0 && enemy.isAlive()) {
            log("💫 Delayed damage triggers for " + delayedDamageToEnemy + "!");
            enemy.takeDamage(delayedDamageToEnemy);
            delayedDamageToEnemy = 0;
            updateLabels();
            if (!enemy.isAlive()) { handleEnemyDefeat(); return; }
        }

        // Reduce player skill cooldowns at end of enemy turn
        for (Skill s : player.getSkills()) s.reduceCooldown();
        updateSkillButtons();

        if (!player.isAlive()) {
            log("💀 You have been defeated!");
            disableSkillButtons();
            return;
        }

        playerTurn = true;
        log("Your turn! Choose your next skill.");
    }

    private void handleEnemyDefeat() {
        log("🏆 You defeated " + enemy.getName() + "!");
        
        // Reset all player skill cooldowns
        for (Skill s : player.getSkills()) {
            s.resetCooldown();  // make sure you have this method in Skill
        }
        updateSkillButtons(); // refresh button texts
        
        disableSkillButtons();
        player.levelUp(0.10, 0.10);

        // Realm / Tutorial progression
        Timer nextBattleTimer = new Timer(700, e -> {
            ((Timer) e.getSource()).stop();
            nextEnemyOrRealm();
        });
        nextBattleTimer.setRepeats(false);
        nextBattleTimer.start();
    }


    private void refreshEnemyPanel() {
        enemyNameLabel.setText(enemy.getName());
        enemyHPLabel.setText("HP: " + enemy.getCurrentHealth() + "/" + enemy.getMaxHealth());
        enemyHPLabel.setForeground(Color.green);
    }

        private void nextEnemyOrRealm() {
        // Tutorial
        if (mode.equals("Tutorial")) {
            if (enemy instanceof Goblin) {
                player.levelUp(0.10, 0.10);
                resetCooldownsOnKill();       // reset skill cooldowns
                enemy = new Cultist();
                updateLabels();
                refreshEnemyPanel();
                clearBattleLog();                     // clear previous logs
                log("🔥 New enemy: " + enemy.getName());
                enableSkillButtons();
                setupSkillButtons();
                return;
            }
            if (enemy instanceof Cultist) {
                player.levelUp(0.10, 0.10);
                resetCooldownsOnKill();         
                mode = "Realm1";
                enemy = new SkySerpent();
                updateLabels();
                healPlayerFull();
                refreshEnemyPanel();
                clearBattleLog();
                log("🏁 Tutorial complete! Welcome to Realm 1.");
                log("🔥 New enemy: " + enemy.getName());
                enableSkillButtons();
                setupSkillButtons();
                backBtn.setEnabled(true);
                JOptionPane.showMessageDialog(this,
                "🌩️ REALM I: AETHERIA 🌩️\n\n" +
                "You awaken beneath stormy skies — Aetheria.\n" +
                "Sky Serpents circle above, lightning dancing across their scales.",
                "Chapter I: The Rift Opens",
                JOptionPane.INFORMATION_MESSAGE);
                return;
            }
        }

        // Realm 1
        if (mode.equals("Realm1")) {
            if (enemy instanceof SkySerpent) {
                resetCooldownsOnKill(); // reset cooldowns
                enemy = new GeneralZephra();
                updateLabels();
                refreshEnemyPanel();
                clearBattleLog();
                log("🔥 New enemy: " + enemy.getName());
                enableSkillButtons();
                setupSkillButtons();
                JOptionPane.showMessageDialog(this,
                    "The Sky Serpent bursts into feathers and lightning.\n" +
                    "From the thunderclouds above descends General Zephra, Storm Mage of the Rift.",
                    "⚡ Boss Battle: General Zephra ⚡",
                    JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            if (enemy instanceof GeneralZephra) {
                JOptionPane.showMessageDialog(this,
                    "Zephra’s thunderbird screeches as lightning fades.\n" +
                    "A fiery rift tears open beneath you...",
                    "🔥 Transition to Realm II: Ignara 🔥",
                    JOptionPane.INFORMATION_MESSAGE);
                player.levelUp(0.10, 0.10);
                resetCooldownsOnKill();
                healPlayerFull();
                mode = "Realm2";
                enemy = new MoltenImp();
                updateLabels();
                refreshEnemyPanel();
                clearBattleLog();
                log("🏁 Realm 1 complete!");
                log("🔥 New enemy: " + enemy.getName());
                enableSkillButtons();
                setupSkillButtons();
                return;
            }
        }

        // Realm 2
        if (mode.equals("Realm2")) {
            if (enemy instanceof MoltenImp) {
                resetCooldownsOnKill();
                enemy = new GeneralVulkrag();
                updateLabels();
                refreshEnemyPanel();
                clearBattleLog();
                log("🔥 New enemy: " + enemy.getName());
                enableSkillButtons();
                setupSkillButtons();
                JOptionPane.showMessageDialog(this,
                    "The last Molten Imp bursts into flame...\n" +
                    "From the magma rises General Vulkrag, the Infernal Commander!",
                    "🔥 Boss Battle: General Vulkrag 🔥",
                    JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            if (enemy instanceof GeneralVulkrag) {
                JOptionPane.showMessageDialog(this,
                    "Vulkrag’s molten armor cracks apart.\n" +
                    "Darkness seeps in from the edges of reality...",
                    "🌑 Transition to Realm III: Noxterra 🌑",
                    JOptionPane.INFORMATION_MESSAGE);
                player.levelUp(0.10, 0.10);
                resetCooldownsOnKill();
                healPlayerFull();
                mode = "Realm3";
                enemy = new ShadowCreeper();
                updateLabels();
                refreshEnemyPanel();
                clearBattleLog();
                log("🏁 Realm 2 complete!");
                log("🔥 New enemy: " + enemy.getName());
                enableSkillButtons();
                setupSkillButtons();
                return;
            }
        }

        // Realm 3
        if (mode.equals("Realm3")) {
            if (enemy instanceof ShadowCreeper) {
                JOptionPane.showMessageDialog(this,
                    "The Shadow Creeper dissolves into mist...\n" +
                    "A dark laughter echoes — the Rift Lord himself descends.",
                    "💀 Final Boss: Lord Vorthnar 💀",
                    JOptionPane.INFORMATION_MESSAGE);
                resetCooldownsOnKill();
                player.levelUp(0.15, 0.15);
                healPlayerFull();
                enemy = new Vorthnar();
                updateLabels();
                refreshEnemyPanel();
                clearBattleLog();
                log("🔥 New enemy: " + enemy.getName());
                log("You Feel a surge of power course through you!");
                enableSkillButtons();
                setupSkillButtons();
                return;
            }
            if (enemy instanceof Vorthnar) {
                clearBattleLog();
                log("🏆 CHAPTER III COMPLETE! You defeated Lord Vorthnar!");
                updateLabels();
                disableSkillButtons();
            }
        }
    }


    private void healPlayerFull() {
        player.setCurrentHealth(player.getMaxHealth());
        log("💖 Player healed to full health!");
    }

    private void clearBattleLog() {
        battleLog.setText("");
    }

    private void updateSkillButtons() {
        Skill[] skills = player.getSkills();
        skillBtn1.setText(skills[0].getName() + (skills[0].isOnCooldown() ? " (CD: " + skills[0].getCurrentCooldown() + ")" : ""));
        skillBtn2.setText(skills[1].getName() + (skills[1].isOnCooldown() ? " (CD: " + skills[1].getCurrentCooldown() + ")" : ""));
        skillBtn3.setText(skills[2].getName() + (skills[2].isOnCooldown() ? " (CD: " + skills[2].getCurrentCooldown() + ")" : ""));
    }

    private void updateLabels() {
        playerHPLabel.setText("HP: " + player.getCurrentHealth() + "/" + player.getMaxHealth());
        double playerHpPercent = Math.max(0, (double) player.getCurrentHealth() / player.getMaxHealth());
        if (playerHpPercent <= 0.3) {
            playerHPLabel.setForeground(Color.RED);
        } else {
            playerHPLabel.setForeground(Color.green);
        }

        if (enemy != null) {
            enemyHPLabel.setText("HP: " + enemy.getCurrentHealth() + "/" + enemy.getMaxHealth());
            double enemyHpPercent = Math.max(0, (double) enemy.getCurrentHealth() / enemy.getMaxHealth());
            if (enemyHpPercent <= 0.3) {
                enemyHPLabel.setForeground(Color.RED);
            } else {
                enemyHPLabel.setForeground(Color.green);
            }
        }

        playerLevelLabel.setText("Level: " + player.getLevel());
    }

    public void log(String msg) {
        battleLog.append(msg + "\n\n");
        battleLog.setCaretPosition(battleLog.getDocument().getLength()); // auto-scroll
    }

    private void disableSkillButtons() {
        skillBtn1.setEnabled(false);
        skillBtn2.setEnabled(false);
        skillBtn3.setEnabled(false);
    }

    private void enableSkillButtons() {
        skillBtn1.setEnabled(true);
        skillBtn2.setEnabled(true);
        skillBtn3.setEnabled(true);
    }

    private void resetPlayerEffects() {
    if (player != null) {
        player.setShieldActive(false);
        player.setDodgeActive(false);
        player.setBlinded(false);
    }

    if (enemy != null) {
        enemy.setBlinded(false);
    }
}


    private void setupBattleLog() {
        battleLog = new JTextArea();
        battleLog.setEditable(false);
        battleLog.setBackground(Color.BLACK);
        battleLog.setForeground(Color.WHITE);
        battleLog.setFont(new Font("Monospaced", Font.PLAIN, 14));
        battleLog.setLineWrap(true);
        battleLog.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(battleLog);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        add(scrollPane, BorderLayout.CENTER);
    }

    private void resetCooldownsOnKill() {
        for (Skill s : player.getSkills()) {
            if (s != null) {
                s.resetCooldown();   // <-- this calls Skill.java resetCooldown()
            }
        }
        updateSkillButtons();
    }
}