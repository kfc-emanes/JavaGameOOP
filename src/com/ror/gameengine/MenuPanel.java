package com.ror.gameengine;
import javax.swing.*;
import com.ror.gameutil.HoverButton;
import java.awt.*;

public class MenuPanel extends JPanel {
    private GameFrame parent;
    private HoverButton playButton;

    public MenuPanel(GameFrame parent) {
        this.parent = parent;

        setLayout(new BorderLayout());
        setBackground(Color.BLACK);

        JLabel title = new JLabel("Realms of Riftborne", SwingConstants.CENTER);
        title.setFont(new Font("Palatino Linotype", Font.BOLD, 64));
        title.setForeground(Color.WHITE);

        playButton = new HoverButton("Start Game");
        HoverButton exitButton = new HoverButton("Exit");

        playButton.addActionListener(e -> parent.showSelect());
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
