package com.ror.gameengine;

import com.ror.gamemodel.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class BattlePanel extends JPanel {
    private GameFrame parent;
    private JButton backButton;
    private JTextArea battleLog;
    private JButton skillBtn1, skillBtn2, skillBtn3, backBtn;
    private JLabel playerHPLabel, enemyHPLabel, playerNameLabel, enemyNameLabel;

    private Entity player;
    private Entity enemy;
    private boolean playerTurn = true;

    private boolean playerShieldActive = false;
    private int delayedDamageToEnemy = 0;
    private int lastDamageTakenByPlayer = 0;
    private String mode = "Tutorial";
    private WorldManager worldManager = new WorldManager();


    public BattlePanel(GameFrame parent) {
        this.parent = parent;
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
        backBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to return to the Main Menu?",
                "Confirm Return",
                JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION) {
                parent.showMenu();
            }
        });

        bottom.add(skillBtn1);
        bottom.add(skillBtn2);
        bottom.add(skillBtn3);
        bottom.add(backBtn);

        add(bottom, BorderLayout.SOUTH);
        backBtn.setEnabled(false);

    }
    
    public void startBattle(Entity chosenPlayer) {
        this.player = chosenPlayer;
        this.enemy = new Goblin(); // tutorial starts here

        playerShieldActive = false;
        delayedDamageToEnemy = 0;
        lastDamageTakenByPlayer = 0;
        playerTurn = true;
        mode = "Tutorial";

        playerNameLabel.setText(player.getName());
        enemyNameLabel.setText(enemy.getName());
        updateHPLabels();

        Skill[] skills = player.getSkills();
        skillBtn1.setText(skills[0].getName());
        skillBtn2.setText(skills[1].getName());
        skillBtn3.setText(skills[2].getName());

        clearListeners();

        skillBtn1.addActionListener(e -> playerUseSkill(0));
        skillBtn2.addActionListener(e -> playerUseSkill(1));
        skillBtn3.addActionListener(e -> playerUseSkill(2));

        battleLog.setText("");
        log("⚔️ The Battle Begins. It's " + player.getName() + " VS " + enemy.getName() + "!");
        log("\nChoose a skill to begin your turn.");
    }

    private void clearListeners() {
        for (ActionListener al : skillBtn1.getActionListeners()) skillBtn1.removeActionListener(al);
        for (ActionListener al : skillBtn2.getActionListeners()) skillBtn2.removeActionListener(al);
        for (ActionListener al : skillBtn3.getActionListeners()) skillBtn3.removeActionListener(al);
    }

    private void playerUseSkill(int index) {
        if (!playerTurn) return;
        Skill s = player.getSkills()[index];

        if (s.isOnCooldown()) {
            log("⏳ " + s.getName() + " is on cooldown for " + s.getCurrentCooldown() + " more turns!");
            return;
        }

        log(player.getName() + " uses " + s.getName() + "!");

        String type = s.getType();
        switch (type.toLowerCase()) {
            case "chrono":
                delayedDamageToEnemy = s.getPower();
                log("⏳ Chrono Slash — damage will trigger after the enemy’s turn!");
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

        // 2. Trigger cooldown only if the skill actually has one
        if (s.getCooldown() > 0) {
            s.triggerCooldown();
        }           


        // Reduce cooldowns for other skills
        for (Skill skill : player.getSkills()) {
            if (skill != s) skill.reduceCooldown();
        }

        playerTurn = false;

        Timer timer = new Timer(900, e -> {
            ((Timer) e.getSource()).stop();
            enemyTurn();
        });
        timer.setRepeats(false);
        timer.start();
        
    }

    private void updateSkillButtons() {
        Skill[] skills = player.getSkills();

        skillBtn1.setText(skills[0].getName() + (skills[0].isOnCooldown() ? " (CD: " + skills[0].getCurrentCooldown() + ")" : ""));
        skillBtn2.setText(skills[1].getName() + (skills[1].isOnCooldown() ? " (CD: " + skills[1].getCurrentCooldown() + ")" : ""));
        skillBtn3.setText(skills[2].getName() + (skills[2].isOnCooldown() ? " (CD: " + skills[2].getCurrentCooldown() + ")" : ""));
    }

    private void enemyTurn() {
        // --- Enemy defeated early (before enemy acts) ---
        if (!enemy.isAlive()) {
            handleEnemyDefeat(enemy);
            return;
        }

        // --- Enemy’s turn ---
        if (playerShieldActive) {
            log("🛡️ The attack is blocked by your Time Shield!");
            playerShieldActive = false;
            lastDamageTakenByPlayer = 0;
        } else {
            int damage = Math.max(0, enemy.getAtk() - player.getDef());
            player.setCurrentHealth(player.getCurrentHealth() - damage);
            lastDamageTakenByPlayer = damage;
            log("👹 " + enemy.getName() + " attacks! You take " + damage + " damage.");
            updateHPLabels();
        }

        // --- Chrono Slash delayed damage ---
        if (delayedDamageToEnemy > 0 && enemy.isAlive()) {
            log("💫 Chrono Slash triggers — " + delayedDamageToEnemy + " delayed damage!");
            enemy.takeDamage(delayedDamageToEnemy);
            delayedDamageToEnemy = 0;
            updateHPLabels();

            if (!enemy.isAlive()) {
                handleEnemyDefeat(enemy);
                return;
            }
        }

        // --- Cooldown reductions ---
        for (Skill skill : player.getSkills()) {
            skill.reduceCooldown();
            updateSkillButtons();
        }

        // --- End turn check ---
        if (!player.isAlive()) {
            log("💀 You were defeated...");
            disableSkillButtons();
            return;
        }

        // --- Player’s next turn ---
        playerTurn = true;
        log("Your turn! Choose your next skill.");
    }

    private void clearBattleLog() {
        battleLog.setText("");
    }


    private void handleEnemyDefeat(Entity defeatedEnemy) {
        log("🏆 You defeated the " + defeatedEnemy.getName() + "!");
        disableSkillButtons();

        // ---- 20% HP Reward ----
        int hpGain = (int) (player.getMaxHealth() * 0.20);
        player.setCurrentHealth(Math.min(player.getMaxHealth(), player.getCurrentHealth() + hpGain));
        log("❤️ You restored +" + hpGain + " HP!");
        updateHPLabels();
        // ---- End 20% HP Reward ----

        Timer nextBattleTimer = new Timer(700, e -> {
            ((Timer) e.getSource()).stop();

            // --- TUTORIAL PHASE ---
            if (mode.equals("Tutorial")) {
                if (defeatedEnemy instanceof Goblin) {
                    JOptionPane.showMessageDialog(this,
                        "The Goblin collapses, dropping a strange sigil...\n" +
                        "From the shadows, a hooded Cultist steps forward.",
                        "Tutorial: Part II", JOptionPane.INFORMATION_MESSAGE);

                    enemy = new Cultist();
                    clearBattleLog();
                    healBetweenBattles();
                    enemyNameLabel.setText(enemy.getName());
                    log("🔥 A new foe approaches: " + enemy.getName() + "!");
                    updateHPLabels();
                    enableSkillButtons();
                    playerTurn = true;
                    return;
                }

                if (defeatedEnemy instanceof Cultist) {
                    JOptionPane.showMessageDialog(this,
                        "The Cultist’s whisper fades: 'He... watches from the Rift...'\n\n" +
                        "A surge of energy pulls you through — the Realms shift.",
                        "End of Tutorial", JOptionPane.INFORMATION_MESSAGE);

                    mode = "Realm1";
                    JOptionPane.showMessageDialog(this,
                        "🌩️ REALM I: AETHERIA 🌩️\n\n" +
                        "You awaken beneath stormy skies — Aetheria.\n" +
                        "Sky Serpents circle above, lightning dancing across their scales.",
                        "Chapter I: The Rift Opens",
                        JOptionPane.INFORMATION_MESSAGE);

                    enemy = new SkySerpent();   
                    clearBattleLog();
                    healBetweenBattles();
                    enemyNameLabel.setText(enemy.getName());
                    log("⚔️ A new foe approaches: " + enemy.getName() + "!");
                    updateHPLabels();
                    enableSkillButtons();
                    playerTurn = true;
                    enableBackButtonForRealDeal();
                    return;
                }
            }

            // --- REALM I: AETHERIA ---
            if (mode.equals("Realm1")) {
                if (defeatedEnemy instanceof SkySerpent) {
                    JOptionPane.showMessageDialog(this,
                        "The Sky Serpent bursts into feathers and lightning.\n" +
                        "From the thunderclouds above descends General Zephra, Storm Mage of the Rift.",
                        "⚡ Boss Battle: General Zephra ⚡",
                        JOptionPane.INFORMATION_MESSAGE);

                    enemy = new GeneralZephra();
                    clearBattleLog();                
                    healBetweenBattles();
                    enemyNameLabel.setText(enemy.getName());
                    log("⚡ A new foe approaches: " + enemy.getName() + "!");
                    updateHPLabels();
                    enableSkillButtons();
                    playerTurn = true;
                    return;
                }

                if (defeatedEnemy instanceof GeneralZephra) {
                    JOptionPane.showMessageDialog(this,
                        "Zephra’s thunderbird screeches as lightning fades.\n" +
                        "A fiery rift tears open beneath you...",
                        "🔥 Transition to Realm II: Ignara 🔥",
                        JOptionPane.INFORMATION_MESSAGE);

                    mode = "Realm2";
                    enemy = new MoltenImp();
                    clearBattleLog();
                    healBetweenBattles();
                    enemyNameLabel.setText(enemy.getName());
                    log("🔥 Realm II: Ignara — molten chaos awaits!");
                    updateHPLabels();
                    enableSkillButtons();
                    playerTurn = true;
                    return;
                }
            }

            // --- REALM II: IGNARA ---
            if (mode.equals("Realm2")) {
                if (defeatedEnemy instanceof MoltenImp) {
                    JOptionPane.showMessageDialog(this,
                        "The last Molten Imp bursts into flame...\n" +
                        "From the magma rises General Vulkrag, the Infernal Commander!",
                        "🔥 Boss Battle: General Vulkrag 🔥",
                        JOptionPane.INFORMATION_MESSAGE);

                    enemy = new GeneralVulkrag();
                    clearBattleLog();
                    healBetweenBattles();
                    enemyNameLabel.setText(enemy.getName());
                    log("🔥 A new foe approaches: " + enemy.getName() + "!");
                    updateHPLabels();
                    enableSkillButtons();
                    playerTurn = true;
                    return;
                }

                if (defeatedEnemy instanceof GeneralVulkrag) {
                    JOptionPane.showMessageDialog(this,
                        "Vulkrag’s molten armor cracks apart.\n" +
                        "Darkness seeps in from the edges of reality...",
                        "🌑 Transition to Realm III: Noxterra 🌑",
                        JOptionPane.INFORMATION_MESSAGE);

                    mode = "Realm3";
                    enemy = new ShadowCreeper();
                    clearBattleLog();
                    healBetweenBattles();
                    enemyNameLabel.setText(enemy.getName());
                    log("🌑 Realm III: Noxterra — the shadows hunger...");
                    updateHPLabels();
                    enableSkillButtons();
                    playerTurn = true;
                    return;
                }
            }

            // --- REALM III: NOXTERRA ---
            if (mode.equals("Realm3")) {
                if (defeatedEnemy instanceof ShadowCreeper) {
                    JOptionPane.showMessageDialog(this,
                        "The Shadow Creeper dissolves into mist...\n" +
                        "A dark laughter echoes — the Rift Lord himself descends.",
                        "💀 Final Boss: Lord Vorthnar 💀",
                        JOptionPane.INFORMATION_MESSAGE);

                    enemy = new Vorthnar();
                    clearBattleLog();
                    healBetweenBattles();
                    enemyNameLabel.setText(enemy.getName());
                    log("💀 The final boss approaches: " + enemy.getName() + "!");
                    updateHPLabels();
                    enableSkillButtons();
                    playerTurn = true;
                    return;
                }

                if (defeatedEnemy instanceof Vorthnar) {
                    JOptionPane.showMessageDialog(this,
                        "Vorthnar collapses — time itself shatters, then reforms.\n\n" +
                        "🏆 CHAPTER III COMPLETE 🏆\nYou have conquered the Realms!",
                        "🎉 Victory!", JOptionPane.INFORMATION_MESSAGE);

                    log("🎉 You defeated Lord Vorthnar! Chapter III complete!");
                    disableSkillButtons();
                    return;
                }
            }
        });
        nextBattleTimer.setRepeats(false);
        nextBattleTimer.start();
    }


    private void enableBackButtonForRealDeal() {
        backBtn.setEnabled(true);
        for (ActionListener al : backBtn.getActionListeners()) backBtn.removeActionListener(al);

        backBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                "Return to Main Menu? Your current progress will be lost.",
                "Confirm Exit",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                // 🔁 Change this depending on your main game structure
                // Example if using a card layout in GameFrame:
                JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
                if (topFrame instanceof GameFrame) {
                    ((GameFrame) topFrame).showMenu();
                }
            }
        });
    }

    private void healBetweenBattles() {
        int healAmount = 60;
        player.setCurrentHealth(Math.min(player.getMaxHealth(), player.getCurrentHealth() + healAmount));
        updateHPLabels();
        log("💖 You recover " + healAmount + " HP before the next battle!");
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

    private void enableSkillButtons() {
        skillBtn1.setEnabled(true);
        skillBtn2.setEnabled(true);
        skillBtn3.setEnabled(true);
    }

    public JButton getBackButton() {
        return backButton;
    }
}
