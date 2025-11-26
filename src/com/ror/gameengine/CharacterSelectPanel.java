package com.ror.gameengine;

//import com.ror.gamemodel.*;
import javax.swing.*;
import java.awt.*;

public class CharacterSelectPanel extends JPanel {
    private JButton backButton;
    private JButton andrewButton;
    private JButton flameWarriorButton;
    private JButton skyMageButton;
    private JButton NyxButton;    
    private JButton TharnButton;  
    //private JButton slot2Button;
    //private JButton slot3Button;
    //private JButton slot4Button;
    //private JButton slot5Button;

    private GameFrame parent; // reference to GameFrame

    public CharacterSelectPanel(GameFrame parent) {
        this.parent = parent;
        setLayout(new BorderLayout());
        setBackground(Color.DARK_GRAY);

        // Title 
        JLabel title = new JLabel("Select Your Character", SwingConstants.CENTER);
        title.setFont(new Font("Century Gothic", Font.BOLD, 26));
        title.setForeground(Color.WHITE);

        // Character Buttons
        JPanel characterPanel = new JPanel(new GridLayout(1, 5, 12, 12));
        characterPanel.setBackground(Color.DARK_GRAY);

        andrewButton = createImageButton("Andrew (Timeblade)", "/com/ror/gamemodel/Assets/Images/Andrew.png");
        flameWarriorButton = createImageButton("Drax (Flame Warrior)", "/com/ror/gamemodel/Assets/Images/Drax.png");
        skyMageButton = createImageButton("Flashey (Sky Mage)", "/com/ror/gamemodel/Assets/Images/Flashley.png");
        NyxButton = createImageButton("Nyx (Assassin)", "/com/ror/gamemodel/Assets/Images/Nyx.png");
        TharnButton = createImageButton("Tharn (Stone Golem)", "/com/ror/gamemodel/Assets/Images/Tharn.png");

        characterPanel.add(andrewButton);
        characterPanel.add(flameWarriorButton);
        characterPanel.add(skyMageButton);
        characterPanel.add(NyxButton);
        characterPanel.add(TharnButton);

        //Back Button
        backButton = new JButton("Back to Menu >>>");

        //Layout
        add(title, BorderLayout.NORTH);
        add(characterPanel, BorderLayout.CENTER);
        add(backButton, BorderLayout.SOUTH);

        //Button Logic
        andrewButton.addActionListener(e -> parent.showBattle(new com.ror.gamemodel.Playable.Andrew()));
        backButton.addActionListener(e -> parent.showMenu());
        flameWarriorButton.addActionListener(e -> parent.showBattle(new com.ror.gamemodel.Playable.FlameWarrior()));
        skyMageButton.addActionListener(e -> parent.showBattle(new com.ror.gamemodel.Playable.SkyMage()));
        NyxButton.addActionListener(e -> parent.showBattle(new com.ror.gamemodel.Playable.Nyx()));
        TharnButton.addActionListener(e -> parent.showBattle(new com.ror.gamemodel.Playable.Tharn()));


        // Disable locked slots
        //slot4Button.setEnabled(false);
       // slot5Button.setEnabled(false);
    }

    //IMAGES BEHIND BUTTONS
    private JButton createImageButton(String label, String imgPath) {
        
        try {
        ImageIcon icon = new ImageIcon(getClass().getResource(imgPath));
        Image rawImg = icon.getImage();

        JButton button = new JButton(label) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                int bw = getWidth();
                int bh = getHeight();
                int imgW = rawImg.getWidth(null);
                int imgH = rawImg.getHeight(null);

                double scale = Math.max(
                    (double) bw / imgW,
                    (double) bh / imgH
                );

                int newW = (int) (imgW * scale);
                int newH = (int) (imgH * scale);

                int x = (bw - newW) / 2;
                int y = (bh - newH) / 2;

                g.drawImage(rawImg, x, y, newW, newH, this);

                Graphics2D g2d = (Graphics2D) g;
                int fontSize = bh / 35;
                g2d.setFont(new Font("Century Gothic", Font.BOLD, fontSize));

                FontMetrics fm = g2d.getFontMetrics();
                int txtW = fm.stringWidth(label);
                int txtH = fm.getAscent();

                int tx = (bw - txtW) / 2;
                int ty = (bh + txtH) / 2 - 3;

                g2d.setColor(Color.BLACK);
                g2d.drawString(label, tx - 1, ty - 1);
                g2d.drawString(label, tx - 1, ty + 1);
                g2d.drawString(label, tx + 1, ty - 1);
                g2d.drawString(label, tx + 1, ty + 1);

                g2d.setColor(getForeground());
                g2d.drawString(label, tx, ty);
            }
        };

        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.setVerticalTextPosition(SwingConstants.CENTER);

        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setOpaque(false);

        button.setForeground(Color.WHITE);
        button.setFont(new Font("Century Gothic", Font.BOLD, 16));
        
        return button;
        } catch (Exception e) {
            System.err.println("Error loading image for button: " + imgPath);
            JButton fallbackButton = new JButton(label);
            fallbackButton.setFont(new Font("Century Gothic", Font.BOLD, 16));
            return fallbackButton;
        }
    }

    // Getter (for flexibility)
    public JButton getAndrewButton() { return andrewButton; }
    public JButton getBackButton() { return backButton; }
    public JButton getFlameWarriorButton() { return flameWarriorButton; }
    public JButton getSkyMageButton() { return skyMageButton; }
    public JButton getNyxButton() { return NyxButton; }
    public JButton getTharnButton() { return TharnButton; }
}
