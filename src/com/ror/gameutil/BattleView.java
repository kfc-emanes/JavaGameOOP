package com.ror.gameutil;

public interface BattleView {
    // Appends a message to the battle log. */
    void logMessage(String message);
    
    // Refreshes all health bars, names, and player stats on the screen. */
    void updateDisplay();
    
    // Updates skill button labels, including cooldowns. */
    void updateSkillButtons();
    
    // Enables or disables all skill buttons simultaneously. */
    void setSkillButtonsEnabled(boolean enabled);
    
    // Shows a modal pop-up message for story progression or confirmation. */
    void showProgressionMessage(String title, String message);
    
    // Transitions the main frame back to the menu screen. */
    void transitionToMenu();
    
    // Returns the result of a yes/no dialog (e.g., JOptionPane.YES_OPTION). */
    int showConfirmDialog(String title, String message);
}