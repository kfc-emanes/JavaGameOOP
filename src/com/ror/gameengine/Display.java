package com.ror.gameengine;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Display extends JFrame implements ActionListener {

    private CardLayout layout;
    private GameFrame gameFrame; // Reference to GameFrame

    public Display() {
        setTitle("Realms of Riftborne - Debug Build v1.4.0");
        setSize(900, 700);
        setFont(new Font("Century Gothic", Font.PLAIN, 16));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        layout = new CardLayout();
        setLayout(layout);

        // Create GameFrame and panels
        gameFrame = new GameFrame(); // reference
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // button logic if needed
    }
}
