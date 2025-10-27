package com.ror.gameengine;

import javax.swing.*;
import java.awt.*;

public class CharacterSelectPanel extends JPanel {
    private JButton backButton;
    private JButton andrewButton;
    private JButton flameWarriorButton;
    private JButton skyMageButton;
    private JButton slot2Button;
    private JButton slot3Button;
    private JButton slot4Button;
    private JButton slot5Button;

    private GameFrame parent; // ✅ reference to GameFrame

    public CharacterSelectPanel(GameFrame parent) {
        this.parent = parent;
        setLayout(new BorderLayout());
        setBackground(Color.DARK_GRAY);

        // --- Title ---
        JLabel title = new JLabel("Select Your Character", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 26));
        title.setForeground(Color.WHITE);

        // --- Character Buttons ---
        JPanel characterPanel = new JPanel(new GridLayout(1, 5, 12, 12));
        characterPanel.setBackground(Color.DARK_GRAY);

        andrewButton = new JButton("<html>Andrew<br/>(Timeblade)</html>");
        flameWarriorButton = new JButton("<html>Drax<br/>(Flame Warrior)</html>");
        skyMageButton = new JButton("<html>Flashey<br/>(Sky Mage)</html>");
        slot4Button = new JButton("Pay 200PHP to unlock");
        slot5Button = new JButton("Pay 200PHP to unlock");

        characterPanel.add(andrewButton);
        characterPanel.add(flameWarriorButton);
        characterPanel.add(skyMageButton);
        characterPanel.add(slot4Button);
        characterPanel.add(slot5Button);

        // --- Back Button ---
        backButton = new JButton("Back to Menu");

        // --- Layout ---
        add(title, BorderLayout.NORTH);
        add(characterPanel, BorderLayout.CENTER);
        add(backButton, BorderLayout.SOUTH);

        // --- Button Logic ---
        andrewButton.addActionListener(e -> parent.showBattle(new com.ror.gamemodel.Andrew()));
        backButton.addActionListener(e -> parent.showMenu());
        flameWarriorButton.addActionListener(e -> parent.showBattle(new com.ror.gamemodel.FlameWarrior()));
        skyMageButton.addActionListener(e -> parent.showBattle(new com.ror.gamemodel.SkyMage()));

        // Disable locked slots
        slot4Button.setEnabled(false);
        slot5Button.setEnabled(false);
    }

    // --- Getters (Optional, for flexibility) ---
    public JButton getAndrewButton() { return andrewButton; }
    public JButton getBackButton() { return backButton; }
    public JButton getFlameWarriorButton() { return flameWarriorButton; }
    public JButton getSkyMageButton() { return skyMageButton; }
    public JButton getSlot4Button() { return slot4Button; }
    public JButton getSlot5Button() { return slot5Button; }
}
