package com.ror.gameengine;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import com.ror.gameutil.HoverButton;
import java.awt.*;


public class GUI extends JPanel {
    private GameFrame parent;
    private HoverButton playButton;
    private HoverButton optionsButton; 
    private HoverButton creditsButton;
    private HoverButton exitButton;

    public GUI(GameFrame parent) {
        this.parent = parent;

        
        setLayout(new BorderLayout(0, 50)); 
        setBackground(Color.BLACK);
        // Add minimal border padding
        setBorder(new EmptyBorder(50, 50, 50, 50)); 

       
        JLabel title = new JLabel("Realms of Riftborne", SwingConstants.CENTER);
        title.setFont(new Font("Palatino Linotype", Font.BOLD, 72));
        title.setForeground(new Color(255, 215, 0)); 
      
        title.setBorder(new EmptyBorder(0, 0, 20, 0));
        
        
        
        
        JPanel buttonContainer = new JPanel();
        
        buttonContainer.setLayout(new BoxLayout(buttonContainer, BoxLayout.Y_AXIS)); 
        buttonContainer.setOpaque(false); 
        
        
        Dimension buttonSize = new Dimension(280, 60); 
        Font btnFont = new Font("Century Gothic", Font.BOLD, 20);

        
        playButton = createStyledButton("Start Game", btnFont, buttonSize);
        optionsButton = createStyledButton("Options", btnFont, buttonSize);
        creditsButton = createStyledButton("Credits", btnFont, buttonSize);
        exitButton = createStyledButton("Exit", btnFont, buttonSize);
        
        
        playButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        optionsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        creditsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        
        buttonContainer.add(playButton);
        buttonContainer.add(Box.createRigidArea(new Dimension(0, 20))); 
        buttonContainer.add(optionsButton);
        buttonContainer.add(Box.createRigidArea(new Dimension(0, 20))); 
        buttonContainer.add(creditsButton);
        buttonContainer.add(Box.createRigidArea(new Dimension(0, 20))); 
        buttonContainer.add(exitButton);

        
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setOpaque(false);
        centerWrapper.add(buttonContainer);


        
        add(title, BorderLayout.NORTH);
        add(centerWrapper, BorderLayout.CENTER); 

        
        playButton.addActionListener(e -> parent.showSelect());
        exitButton.addActionListener(e -> System.exit(0));
        
        optionsButton.addActionListener(e -> handleNotImplemented("Options Screen"));
        creditsButton.addActionListener(e -> handleNotImplemented("Credits Screen"));
    }

    private HoverButton createStyledButton(String text, Font font, Dimension size) {
        HoverButton button = new HoverButton(text);
        button.setFont(font);
        button.setPreferredSize(size);
        button.setMaximumSize(size); 
        return button;
    }
    
    private void handleNotImplemented(String screenName) {
        JOptionPane.showMessageDialog(this,
            screenName + " is not yet implemented. This will be added in a future version!",
            "Feature Coming Soon",
            JOptionPane.INFORMATION_MESSAGE);
    }

    public JButton getPlayButton() {
        return playButton;
    }
}