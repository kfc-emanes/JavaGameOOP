package com.ror.gameengine;

import com.ror.gamemodel.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GUIClass {

    
    private ActionListener choiceHandler;

    
    public GUIClass() {
        this.choiceHandler = new ChoiceHandler(); 
    }

    private JButton createStyledButton(String text, String command) {
        JButton button = new JButton(text);
        button.setActionCommand(command);
        button.setFont(new Font("Segoe UI", Font.BOLD, 22));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(50, 50, 50));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        button.setOpaque(true);

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(70, 70, 70));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(50, 50, 50));
            }
        });

        button.addActionListener(choiceHandler);
        return button;
    }

   
    class ChoiceHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Button clicked: " + e.getActionCommand());
        }
    }
}