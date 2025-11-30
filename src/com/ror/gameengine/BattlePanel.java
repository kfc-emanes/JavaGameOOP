package com.ror.gameengine;

import com.ror.gamemodel.Entity;
import com.ror.gamemodel.Skill;
import com.ror.gameutil.BattleView;
import com.ror.gameutil.BattleManager;
import com.ror.gameutil.HoverButton;

import javax.swing.*;
import javax.swing.border.Border;

import java.awt.*;
import java.awt.event.*;

public class BattlePanel extends JPanel implements BattleView {
    
    // --- UI Components ---
    public GameFrame parent;
    public JTextArea battleLog;
    public HoverButton skillBtn1, skillBtn2, skillBtn3, backBtn;
    public JLabel playerHPLabel, enemyHPLabel, playerNameLabel, enemyNameLabel, playerLevelLabel;
    public JProgressBar playerHPBar, enemyHPBar;
    
    // --- Reference to the Controller/Logic Layer
    private final BattleManager battleManager; 

    public BattlePanel(GameFrame parent) {
        this.parent = parent;
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);

        this.battleManager = new BattleManager(this);
        setupBattleLog();
    }

    // PUBLIC ENTRY POINT
    
    public void startBattle(Entity chosenPlayer) {
        // 1. Initialize all UI components (buttons, labels, bars) with placeholder values.
        setupTopPanel();
        setupBottomPanel(); 
        
        // 2. Initialize the Logic (Controller), which sets real player/enemy objects
        // and calls updateDisplay() and updateSkillButtons().
        battleManager.startBattle(chosenPlayer);
        
        // 3. Attach action listeners
        setupSkillButtons();
    }

    private void setupSkillButtons() {
        // We need a defensive check here too, in case setupBottomPanel fails silently.
        if (skillBtn1 == null || skillBtn2 == null || skillBtn3 == null) {
            System.err.println("Warning: Cannot set up skill listeners. Buttons are null.");
            return;
        }

        clearSkillListeners(); 
        
        // DELEGATE: All button clicks go straight to the initialized manager
        skillBtn1.addActionListener(e -> battleManager.processPlayerAction(0));
        skillBtn2.addActionListener(e -> battleManager.processPlayerAction(1));
        skillBtn3.addActionListener(e -> battleManager.processPlayerAction(2));
        
        updateSkillButtons();
    }

    private void clearSkillListeners() {
        // Utility method to remove old listeners before adding new ones
        if (skillBtn1 != null) for (ActionListener al : skillBtn1.getActionListeners()) skillBtn1.removeActionListener(al);
        if (skillBtn2 != null) for (ActionListener al : skillBtn2.getActionListeners()) skillBtn2.removeActionListener(al);
        if (skillBtn3 != null) for (ActionListener al : skillBtn3.getActionListeners()) skillBtn3.removeActionListener(al);
    }
    
    private void handleBackClick() {
        // DELEGATE: Manager handles the consequence of backing out
        battleManager.confirmBackToMenu();
    }
    
    // BATTLEVIEW IMPLEMENTATION (UI Rendering Methods)
    
    @Override
    public void logMessage(String msg) {
        Font logFont = new Font("Courier New", Font.PLAIN, 16); 
        battleLog.setFont(logFont);
        battleLog.append(msg + "\n\n");
        battleLog.setCaretPosition(battleLog.getDocument().getLength());
    }

    @Override
    public void updateDisplay() {
        Entity player = battleManager.getPlayer();
        Entity enemy = battleManager.getEnemy();
        
        if (player == null || enemy == null) return;

        // Player UI Update
        playerNameLabel.setText(player.getName()); // Set actual name
        playerHPBar.setMaximum(player.getMaxHealth());
        playerHPBar.setValue(player.getCurrentHealth());
        double playerHpPercent = (double) player.getCurrentHealth() / player.getMaxHealth();
        playerHPBar.setForeground(playerHpPercent <= 0.3 ? Color.RED : Color.GREEN);
        playerHPBar.setString("HP: " + player.getCurrentHealth() + "/" + player.getMaxHealth());
        playerLevelLabel.setText("Level: " + player.getLevel());

        // Enemy UI Update
        enemyNameLabel.setText(enemy.getName()); // Set actual name
        enemyHPBar.setMaximum(enemy.getMaxHealth());
        enemyHPBar.setValue(enemy.getCurrentHealth());
        double enemyHpPercent = (double) enemy.getCurrentHealth() / enemy.getMaxHealth();
        enemyHPBar.setForeground(enemyHpPercent <= 0.3 ? Color.RED : Color.GREEN);
        enemyHPLabel.setText("HP: " + enemy.getCurrentHealth() + "/" + enemy.getMaxHealth());
    }
    
    @Override
    public void updateSkillButtons() {
        Entity player = battleManager.getPlayer();
        if (player == null) return;
        
        // --- CRITICAL DEFENSIVE CHECK (Line 114 is near here) ---
        // If skillBtn1 is null, the buttons have not been initialized by setupBottomPanel().
        if (skillBtn1 == null) {
            // We exit gracefully instead of crashing.
            System.err.println("ERROR: Cannot update skill buttons; UI components are not yet initialized.");
            return;
        }
        // --- END CRITICAL DEFENSIVE CHECK ---
        
        // Uses battleManager.getPlayer() safely, since the data is initialized in startBattle now.
        Skill[] skills = battleManager.getPlayer().getSkills().toArray(new Skill[0]);
        
        // Update button text with cooldowns
        if (skills.length > 0)
            skillBtn1.setText(skills[0].getName() + (skills[0].isOnCooldown() ? " (CD: " + skills[0].getCurrentCooldown() + ")" : ""));
        if (skills.length > 1)
            skillBtn2.setText(skills[1].getName() + (skills[1].isOnCooldown() ? " (CD: " + skills[1].getCurrentCooldown() + ")" : ""));
        if (skills.length > 2)
            skillBtn3.setText(skills[2].getName() + (skills[2].isOnCooldown() ? " (CD: " + skills[2].getCurrentCooldown() + ")" : ""));

        // Update enable state based on logic from manager
        setSkillButtonsEnabled(battleManager.playerTurn); 
        
        // Re-disable if on cooldown (prevents clicks while CD > 0)
        if (skills.length > 0 && skills[0].isOnCooldown()) skillBtn1.setEnabled(false);
        if (skills.length > 1 && skills[1].isOnCooldown()) skillBtn2.setEnabled(false);
        if (skills.length > 2 && skills[2].isOnCooldown()) skillBtn3.setEnabled(false);
    }
    
    @Override
    public void setSkillButtonsEnabled(boolean enabled) {
        // Defensive check here as well
        if (skillBtn1 == null) return; 

        skillBtn1.setEnabled(enabled);
        skillBtn2.setEnabled(enabled);
        skillBtn3.setEnabled(enabled);
    }

    @Override
    public void showProgressionMessage(String title, String message) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
    }
    
    @Override
    public int showConfirmDialog(String title, String message) {
        return JOptionPane.showConfirmDialog(this,
                message,
                title, JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
    }

    @Override
    public void transitionToMenu() {
        parent.showMenu();
    }

    // UI SETUP METHODS 

    private void setupTopPanel() { 
        // Use placeholder values for initialization. updateDisplay() will set the real data later.

        JPanel top = new JPanel(new FlowLayout(FlowLayout.CENTER));
        top.setBackground(Color.BLACK);
        top.setOpaque(true);

        enemyNameLabel = new JLabel("Enemy", SwingConstants.CENTER); // Placeholder
        enemyNameLabel.setForeground(Color.WHITE);
        enemyNameLabel.setFont(new Font("Century Gothic", Font.BOLD, 14));
        enemyNameLabel.setPreferredSize(new Dimension(150, 30));

        enemyHPBar = new JProgressBar(0, 100); // Placeholder max
        enemyHPBar.setValue(100); // Placeholder current
        enemyHPBar.setPreferredSize(new Dimension(250, 20));
        enemyHPBar.setStringPainted(false);
        enemyHPBar.setForeground(Color.GREEN);
        enemyHPBar.setBackground(Color.DARK_GRAY);
        enemyHPBar.setBorder(BorderFactory.createEmptyBorder());

        enemyHPLabel = new JLabel("HP: 100/100", SwingConstants.CENTER); // Placeholder text
        enemyHPLabel.setForeground(Color.WHITE);
        enemyHPLabel.setFont(new Font("Century Gothic", Font.PLAIN, 12));
        enemyHPLabel.setPreferredSize(new Dimension(100, 30));

        JPanel enemyStack = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        enemyStack.setBackground(Color.BLACK);
        enemyStack.add(enemyNameLabel);
        enemyStack.add(enemyHPBar);
        enemyStack.add(enemyHPLabel);

        JPanel enemyBox = new JPanel(new BorderLayout());
        enemyBox.setBackground(Color.BLACK);
        enemyBox.setOpaque(true);
        enemyBox.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.WHITE, 2),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        enemyBox.add(enemyStack, BorderLayout.CENTER);
        enemyBox.setPreferredSize(new Dimension(520, 50));

        top.add(enemyBox);
        add(top, BorderLayout.NORTH);
    }

    private void setupBottomPanel() {
        // We no longer try to instantiate 'Entity' here. We use placeholders.
        
        // CRITICAL: Initialize buttons here!
        skillBtn1 = new HoverButton("Skill 1");
        skillBtn2 = new HoverButton("Skill 2");
        skillBtn3 = new HoverButton("Skill 3");
        backBtn = new HoverButton("Back");

        JPanel bottom = new JPanel();
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.Y_AXIS));
        bottom.setBackground(Color.BLACK);
        bottom.setOpaque(true);

        // Player Info Labels (Placeholder values)
        playerNameLabel = new JLabel("Player", SwingConstants.LEFT);
        playerNameLabel.setForeground(Color.WHITE);
        playerNameLabel.setFont(new Font("SansSerif", Font.BOLD, 14));

        playerHPLabel = new JLabel("HP: 0/0", SwingConstants.RIGHT); 
        playerHPLabel.setForeground(Color.WHITE);
        playerHPLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));

        playerHPBar = new JProgressBar(0, 100); // Placeholder max
        playerHPBar.setValue(0); // Placeholder current
        playerHPBar.setPreferredSize(new Dimension(320, 16));
        playerHPBar.setStringPainted(true);
        playerHPBar.setForeground(Color.WHITE);
        playerHPBar.setBackground(Color.DARK_GRAY);

        playerLevelLabel = new JLabel("Lv: 1", SwingConstants.RIGHT);
        playerLevelLabel.setForeground(Color.WHITE);
        playerLevelLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));

        JPanel playerInfo = new JPanel(new BorderLayout(8,2));
        playerInfo.setBackground(Color.BLACK);
        
        JPanel leftStack = new JPanel(new GridLayout(1,1));
        leftStack.setBackground(Color.BLACK);
        leftStack.add(playerNameLabel);

        JPanel rightStack = new JPanel(new GridLayout(2,1));
        rightStack.setBackground(Color.BLACK);
        rightStack.add(playerHPLabel);
        rightStack.add(playerLevelLabel);

        playerInfo.add(leftStack, BorderLayout.WEST);
        playerInfo.add(playerHPBar, BorderLayout.CENTER);
        playerInfo.add(rightStack, BorderLayout.EAST);

        JPanel playerBox = new JPanel(new BorderLayout());
        playerBox.setBackground(Color.BLACK);
        playerBox.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.WHITE, 2),
                BorderFactory.createEmptyBorder(8,12,8,12)
        ));
        playerBox.add(playerInfo, BorderLayout.CENTER);
        playerBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 64));

        // Buttons row
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 24, 8));
        buttonsPanel.setBackground(Color.BLACK);

        Font btnFont = new Font("SansSerif", Font.PLAIN, 14);
        skillBtn1.setFont(btnFont);
        skillBtn2.setFont(btnFont);
        skillBtn3.setFont(btnFont);
        backBtn.setFont(btnFont);

        Border btnBorder = BorderFactory.createLineBorder(Color.WHITE, 2);
        skillBtn1.setBorder(btnBorder);
        skillBtn2.setBorder(btnBorder);
        skillBtn3.setBorder(btnBorder);
        backBtn.setBorder(btnBorder);

        backBtn.addActionListener(e -> handleBackClick()); // Use new delegation method

        buttonsPanel.add(skillBtn1);
        buttonsPanel.add(skillBtn2);
        buttonsPanel.add(skillBtn3);
        buttonsPanel.add(backBtn); 

        bottom.add(Box.createVerticalStrut(8));
        bottom.add(playerBox);
        bottom.add(Box.createVerticalStrut(12));
        bottom.add(buttonsPanel);
        bottom.add(Box.createVerticalStrut(8));

        add(bottom, BorderLayout.SOUTH);
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

        scrollPane.setBorder(null);
        scrollPane.setBackground(Color.BLACK);
        scrollPane.getViewport().setBackground(Color.BLACK);
        scrollPane.setViewportBorder(null);

        JPanel corner = new JPanel();
        corner.setBackground(Color.BLACK);
        corner.setOpaque(true);
        scrollPane.setCorner(JScrollPane.LOWER_RIGHT_CORNER, corner);
        scrollPane.setCorner(JScrollPane.UPPER_RIGHT_CORNER, corner);

        JScrollBar vbar = scrollPane.getVerticalScrollBar();
        if (vbar != null) {
            vbar.setBackground(Color.BLACK);
            vbar.setOpaque(true);
        }

        add(scrollPane, BorderLayout.CENTER);
    }
}