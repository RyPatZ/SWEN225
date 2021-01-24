package Gui;

import Game.Game;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.*;
import java.awt.*;
import java.util.List;

/**
 * Represents the
 */
public class MenuView {
    private static final int PANEL_HEIGHT = 40;

    private Window window;
    private JComboBox playerSelect;
    private JPanel characterSelector;
    private int nPlayers;
    private List<JTextField> playerNames = new ArrayList<>();
    private List<JComboBox> playerCharacters = new ArrayList<>();

    /**
     * Create a menu view
     */
    public MenuView(Window window) {
        this.window = window;
        window.setTitle("Pick players");
        window.removeAll();
        window.setLayout(new BoxLayout(window, BoxLayout.Y_AXIS));
        window.add(Box.createRigidArea(new Dimension(window.getWidth(), (window.getHeight() - PANEL_HEIGHT * 8) / 2)));

        // player number selector
        nPlayers = 3;

        JPanel playerSelectPanel = new JPanel(new FlowLayout());

        JLabel playerSelectLabel = new JLabel("Select amount of Players:");

        playerSelect = new JComboBox(new String[]{"3", "4", "5", "6"});
        playerSelect.setSelectedIndex(0);
        playerSelectPanel.add(playerSelectLabel);
        playerSelectPanel.add(playerSelect);
        playerSelectPanel.setMaximumSize(new Dimension(window.getWidth(), PANEL_HEIGHT));
        window.add(playerSelectPanel);

        // create player amount selector updater
        playerSelect.addActionListener((ActionEvent e) ->
        {
            nPlayers = playerSelect.getSelectedIndex() + 3;
            recreateCharacterSelect();
        });

        // character selector
        characterSelector = new JPanel();
        window.add(characterSelector);
        recreateCharacterSelect();

        // bottom panel
        JPanel footerPanel = new JPanel(new FlowLayout());
        window.add(footerPanel);
        JButton doneButton = new JButton("Done");

        doneButton.addActionListener((ActionEvent e) -> createGame());


        JButton exitButton = new JButton("Exit");

        exitButton.addActionListener((ActionEvent e) -> window.close());

        footerPanel.add(doneButton);
        footerPanel.add(exitButton);
        footerPanel.setMaximumSize(new Dimension(window.getWidth(), PANEL_HEIGHT));

        window.add(Box.createVerticalGlue());

        // redraw
        window.redraw();

    }

    /**
     * Create the character selector
     */
    private void recreateCharacterSelect() {
        // remove previous items
        characterSelector.removeAll();
        playerNames.clear();
        playerCharacters.clear();

        // reformat layout
        characterSelector.setLayout(new GridLayout(nPlayers, 2));
        characterSelector.setMaximumSize(new Dimension(window.getWidth() / 2, PANEL_HEIGHT * nPlayers));

        // create fields
        for (int i = 1; i <= nPlayers; i++) {
            JTextField field = new JTextField("Player " + i);
            JComboBox combo = new JComboBox(Game.allSuspects);
            combo.setFocusable(false);

            combo.addActionListener((ActionEvent e) -> {
                updateCharacterValidity();
            });

            characterSelector.add(field);
            characterSelector.add(combo);

            playerNames.add(field);
            playerCharacters.add(combo);
        }

        // redraw
        updateCharacterValidity();
    }


    /**
     * Update the colours of the combo boxes to show validity
     */
    private void updateCharacterValidity() {
        for (JComboBox character : playerCharacters) {
            int selectedIndex = character.getSelectedIndex();
            int nCopies = 0;
            for (JComboBox character2 : playerCharacters) {
                if (character2.getSelectedIndex() == selectedIndex) nCopies++;
            }

            // valid if this character selection is unique
            if (nCopies == 1) character.setBorder(BorderFactory.createLineBorder(Color.BLUE));
            else character.setBorder(BorderFactory.createLineBorder(Color.RED));
        }

        window.redraw();
    }


    /**
     * Create the game if possible to
     */
    public void createGame() {
        // check validity
        Set<String> characterSet = new HashSet<>();
        Set<String> nameSet = new HashSet<>();
        for (JComboBox comboBox : playerCharacters) {
            if (!characterSet.add((String) comboBox.getSelectedItem())) {
                // invalid
                JOptionPane.showMessageDialog(window, "Error: There are multiple players using the same character.");
                return;
            }
        }
        for (JTextField field : playerNames) {
            if (!nameSet.add(field.getText())) {
                // invalid
                JOptionPane.showMessageDialog(window, "Error: There are multiple players using the same username.");
                return;
            }
        }


        List<String> characterStrings = new ArrayList<>();
        for (JComboBox comboBox : playerCharacters) characterStrings.add((String) comboBox.getSelectedItem());

        List<String> names = new ArrayList<>();
        for (JTextField field : playerNames) names.add(field.getText());

        new GameView(window, characterStrings, names);
    }
}
