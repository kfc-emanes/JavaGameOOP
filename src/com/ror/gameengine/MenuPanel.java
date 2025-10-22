package com.ror.gameengine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class MenuPanel extends JPanel {
    private JButton playButton;
    private JButton exitButton;

    public MenuPanel(ActionListener listener) {
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);

        // Game Title
        JLabel title = new JLabel("Realms of Riftborne", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 28));
        title.setForeground(Color.WHITE);

        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 1, 10, 10));
        buttonPanel.setBackground(Color.BLACK);

        playButton = new JButton("Play");
        exitButton = new JButton("Exit");

        // Styling (optional)
        playButton.setFont(new Font("SansSerif", Font.BOLD, 18));
        exitButton.setFont(new Font("SansSerif", Font.BOLD, 18));

        buttonPanel.add(playButton);
        buttonPanel.add(exitButton);

        add(title, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Hook up buttons to Display’s listener
        playButton.addActionListener(listener);
        exitButton.addActionListener(e -> System.exit(0));
    }

    public JButton getPlayButton() {
        return playButton;
    }
}