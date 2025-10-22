package com.ror.gameengine;

import com.ror.gamemodel.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Display extends JFrame implements ActionListener {

    private CardLayout layout;
    private MenuPanel menuPanel;
    private CharacterSelectPanel selectPanel;
    private BattlePanel battlePanel;

    public Display() {
        setTitle("Realms of Riftborne - Debug Build v2.0");
        setSize(700, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        layout = new CardLayout();
        setLayout(layout);

        menuPanel = new MenuPanel(this);
        selectPanel = new CharacterSelectPanel(this);
        battlePanel = new BattlePanel();

        add(menuPanel, "Menu");
        add(selectPanel, "Select");
        add(battlePanel, "Battle");

        layout.show(getContentPane(), "Menu");
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();

        if (src == menuPanel.getPlayButton()) {
            layout.show(getContentPane(), "Select");
        } 
        else if (src == selectPanel.getAndrewButton()) {
            battlePanel.startBattle(new Andrew());
            layout.show(getContentPane(), "Battle");
        } 
        else if (src == selectPanel.getBackButton()) {
            layout.show(getContentPane(), "Menu");
        }
    }
}