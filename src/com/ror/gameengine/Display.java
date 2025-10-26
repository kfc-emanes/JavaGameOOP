package com.ror.gameengine;

import com.ror.gamemodel.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Display extends JFrame implements ActionListener {

    private CardLayout layout;
    private GameFrame gameFrame; // Reference to GameFrame

    public Display() {
        setTitle("Realms of Riftborne - Debug Build v2.0");
        setSize(700, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        layout = new CardLayout();
        setLayout(layout);

        // Create GameFrame and panels
        gameFrame = new GameFrame(); // reference so we can call its showBattle etc.
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // button logic if needed
    }
}
