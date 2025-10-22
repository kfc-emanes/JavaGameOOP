package com.ror.gameengine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class CharacterSelectPanel extends JPanel {
    private JButton backButton;
    private JButton andrewButton;
    private JButton slot2Button;
    private JButton slot3Button;
    private JButton slot4Button;
    private JButton slot5Button;

    public CharacterSelectPanel(ActionListener listener) {
        setLayout(new BorderLayout());
        setBackground(Color.DARK_GRAY);

        JLabel title = new JLabel("Select Your Character", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 26));
        title.setForeground(Color.WHITE);

        JPanel characterPanel = new JPanel(new GridLayout(1, 5, 12, 12));
        characterPanel.setBackground(Color.DARK_GRAY);

        andrewButton = new JButton("<html>Andrew<br/>(Timeblade)</html>");
        slot2Button = new JButton("Pay 200PHP to unlock");
        slot3Button = new JButton("Pay 200PHP to unlock");
        slot4Button = new JButton("Pay 200PHP to unlock");
        slot5Button = new JButton("Pay 200PHP to unlock");

        characterPanel.add(andrewButton);
        characterPanel.add(slot2Button);
        characterPanel.add(slot3Button);
        characterPanel.add(slot4Button);
        characterPanel.add(slot5Button);

        backButton = new JButton("Back to Menu");

        add(title, BorderLayout.NORTH);
        add(characterPanel, BorderLayout.CENTER);
        add(backButton, BorderLayout.SOUTH);

        // Wire to central listener (Display)
        andrewButton.addActionListener(listener);
        slot2Button.setEnabled(false);
        slot3Button.setEnabled(false);
        slot4Button.setEnabled(false);
        slot5Button.setEnabled(false);
        backButton.addActionListener(listener);
    }

    public JButton getAndrewButton() { return andrewButton; }
    public JButton getBackButton() { return backButton; }
    public JButton getSlot2Button() { return slot2Button; }
    public JButton getSlot3Button() { return slot3Button; }
    public JButton getSlot4Button() { return slot4Button; }
    public JButton getSlot5Button() { return slot5Button; }
}