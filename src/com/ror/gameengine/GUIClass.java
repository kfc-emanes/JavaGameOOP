package com.ror.gameengine;

import com.ror.gamemodel.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GUIClass {

    // Window + container
    private JFrame window;
    private Container con;

    // Action handler
    private ActionListener choiceHandler;

    // Example UI components
    private JPanel buttonPanel;
    private JButton btn1, btn2;

   
    public GUIClass() {

        
        this.choiceHandler = new ChoiceHandler();

        window = new JFrame("Game UI");
        window.setSize(800, 600);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLayout(null);
        window.setVisible(true);

        con = window.getContentPane();

        createUI();
    }

   
    private void createUI() {
        buttonPanel = new JPanel();
        buttonPanel.setBounds(250, 200, 300, 200);
        buttonPanel.setBackground(new Color(0, 0, 0, 0));
        buttonPanel.setLayout(new GridLayout(2, 1, 0, 15));
        con.add(buttonPanel);

        btn1 = createStyledButton("Start", "START");
        btn2 = createStyledButton("Exit", "EXIT");

        buttonPanel.add(btn1);
        buttonPanel.add(btn2);
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
            String cmd = e.getActionCommand();
            System.out.println("Button clicked: " + cmd);

            switch (cmd) {
                case "START":
                    JOptionPane.showMessageDialog(null, "Game Starting...");
                    break;

                case "EXIT":
                    System.exit(0);
                    break;
            }
        }
    }

    
    public static void main(String[] args) {
        new GUIClass();
    }
}