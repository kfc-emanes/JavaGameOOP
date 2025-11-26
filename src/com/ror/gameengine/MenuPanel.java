package com.ror.gameengine;

import javax.swing.*;
import java.awt.*;

public class MenuPanel extends JPanel {
    private GameFrame parent;  // Reference to the main frame so it says unused meh
    private JButton playButton;

    public MenuPanel(GameFrame parent) {
        this.parent = parent; // Store the reference diri   

        setLayout(new BorderLayout());
        setBackground(Color.BLACK);

        JLabel title = new JLabel("Realms of Riftborne", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 32));
        title.setForeground(Color.WHITE);

        playButton = new JButton("Start Game");
        JButton exitButton = new JButton("Exit");

        playButton.addActionListener(e -> parent.showSelect());  // Go to select screen
        exitButton.addActionListener(e -> System.exit(0));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.add(playButton);
        buttonPanel.add(exitButton);

        add(title, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public JButton getPlayButton() {
        return playButton;
    }
}
