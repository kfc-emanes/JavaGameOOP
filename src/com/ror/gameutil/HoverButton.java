package com.ror.gameutil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class HoverButton extends JButton {

    public HoverButton(String text) {
        super(text);

        setPreferredSize(new Dimension(220, 60));
        setFont(new Font("Century Gothic", Font.BOLD, 24));

        setFocusPainted(false);
        setOpaque(true);
        setContentAreaFilled(true);

        setBackground(Color.BLACK);
        setForeground(Color.WHITE);
        setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(Color.WHITE);
                setForeground(Color.BLACK);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(Color.BLACK);
                setForeground(Color.WHITE);
            }
        });
    }
    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (!enabled) {
            setBackground(Color.DARK_GRAY);
            setForeground(Color.LIGHT_GRAY);
        } else {
            setBackground(Color.BLACK);
            setForeground(Color.WHITE);
        }
    }
}