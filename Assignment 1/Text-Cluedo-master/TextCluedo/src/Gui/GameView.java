package Gui;

import Game.*;

import java.awt.event.KeyEvent;
import java.util.List;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * The entire game view
 */
public class GameView extends Canvas {
    public static final int CELL_SIZE = 16;
    private static final int GAME_WIDTH = 26;
    private static final int GAME_HEIGHT = 27;
    private static final int CANVAS_WIDTH = GAME_WIDTH * CELL_SIZE;
    private static final int CANVAS_HEIGHT = GAME_HEIGHT * CELL_SIZE;

    private Game game;
    private Window window;
    private Player currentPlayer;
    private GameController controller;

    private List<String> characters;
    private List<String> names;

    private JPanel messagePanel, buttonPanel, cardPanel;
    private Dice dice;

    /**
     * Create a view containing the game
     */
    public GameView(Window window, List<String> characters, List<String> names) {
        this.window = window;
        this.characters = characters;
        this.names = names;

        game = new Game(this, characters, names);
        currentPlayer = game.getPlayer(0);
        controller = new GameController(this);

        window.removeAll();
        window.setLayout(new BoxLayout(window, BoxLayout.Y_AXIS));

        // menu bar
        JMenuBar menuBar = new JMenuBar();
        JMenu gameMenu = new JMenu("Game");
        menuBar.add(gameMenu);

        JMenuItem restart = new JMenuItem("Restart");
        restart.addActionListener((ActionEvent e) -> restartGame());

        JMenuItem btMenu = new JMenuItem("Back To Menu");
        btMenu.addActionListener((ActionEvent e) -> backToMenu());

        JMenuItem exit = new JMenuItem("Exit");
        exit.addActionListener((ActionEvent e) -> this.window.close());

        gameMenu.add(restart);
        gameMenu.add(btMenu);
        gameMenu.add(exit);
        window.setMenuBar(menuBar);


        // create game canvas
        setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
        setMaximumSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
        addMouseListener(controller);
        window.add(this);

        // key listener
        setFocusable(true);
        requestFocus();
        addKeyListener(controller);

        // footer panel sizes
        int footerSize = (window.getHeight() - CANVAS_HEIGHT);
        dice = new Dice((int) (footerSize * 0.2));

        // message panel
        messagePanel = new JPanel();
        messagePanel.setMaximumSize(new Dimension(window.getWidth(), (int) (footerSize * 0.2)));
        window.add(messagePanel);

        // button panel
        buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setMaximumSize(new Dimension(window.getWidth(), (int) (footerSize * 0.4)));
        window.add(buttonPanel);

        // card panel
        cardPanel = new JPanel(new FlowLayout());
        cardPanel.setMaximumSize(new Dimension(window.getWidth(), (int) (footerSize * 0.4)));
        window.add(cardPanel);

        swapPlayer(currentPlayer);
        updatePlayerState();
    }

    /**
     * Update the buttons on the screen to reflect the player's current state
     */
    public void updatePlayerState() {
        buttonPanel.removeAll();
        messagePanel.removeAll();

        messagePanel.add(new JLabel(currentPlayer + "'s turn."));

        Player.PlayerState state = currentPlayer.getCurrentState();

        // moving state
        if (state == Player.PlayerState.MOVING) {
            buttonPanel.add(dice);
            JLabel turnsLeftLabel = new JLabel("Moves Left: " + currentPlayer.getMovesLeft());
            buttonPanel.add(turnsLeftLabel);

            messagePanel.add(new JLabel("Use the mouse or WASD/Arrow Keys to move."));
        } else {
            // roll dice
            if (state == Player.PlayerState.WAITING) {
                JButton rollDice = new JButton("Roll Dice");
                rollDice.setMnemonic(KeyEvent.VK_R);
                rollDice.setFocusable(false);
                rollDice.addActionListener((ActionEvent e) -> {
                    currentPlayer.rollDice(dice);
                    repaint();
                });
                buttonPanel.add(rollDice);
            }
            // finish turn
            else {
                JButton finishTurn = new JButton("Finish Turn");
                finishTurn.setMnemonic(KeyEvent.VK_F);
                finishTurn.setFocusable(false);
                finishTurn.addActionListener((ActionEvent e) -> {
                    currentPlayer.finishTurn();
                    repaint();
                });
                buttonPanel.add(finishTurn);
            }

            // accuse
            JButton accuse = new JButton("Accuse");
            accuse.setMnemonic(KeyEvent.VK_A);
            accuse.setFocusable(false);
            accuse.addActionListener((ActionEvent e) -> accuse());
            buttonPanel.add(accuse);

            // suggest
            if (currentPlayer.getSuspect().getCurrentRoom() != null) {
                JButton suggest = new JButton("Suggest");
                suggest.setMnemonic(KeyEvent.VK_S);
                suggest.setFocusable(false);
                suggest.addActionListener((ActionEvent e) -> suggest());
                buttonPanel.add(suggest);
            }

        }

        buttonPanel.repaint();
        window.redraw();
    }

    /**
     * Go back to the menu view
     */
    public void backToMenu() {
        new MenuView(window);
    }

    /**
     * Restart the game
     */
    public void restartGame() {
        game = new Game(this, characters, names);
        currentPlayer = game.getPlayer(0);

        swapPlayer(currentPlayer);
        repaint();
        window.redraw();
    }


    /**
     * Open accuse dialog
     */
    public void accuse() {
        // check in correct state
        if (currentPlayer.getCurrentState() == Player.PlayerState.MOVING || currentPlayer.getCurrentState() == Player.PlayerState.NOT_TURN)
            return;

        // create panel layouts
        JPanel accusePanel = new JPanel();
        accusePanel.setLayout(new FlowLayout());

        // suspects
        JPanel suspectPanel = new JPanel();
        accusePanel.add(suspectPanel);
        suspectPanel.setLayout(new BoxLayout(suspectPanel, BoxLayout.Y_AXIS));
        suspectPanel.add(new JLabel("Pick Suspect:"));
        ButtonGroup suspects = new ButtonGroup();
        for (String suspect : Game.allSuspects) {
            JRadioButton button = new JRadioButton(suspect);
            button.setActionCommand(suspect);
            suspects.add(button);
            suspectPanel.add(button);
        }

        // weapons
        JPanel weaponPanel = new JPanel();
        accusePanel.add(weaponPanel);
        weaponPanel.setLayout(new BoxLayout(weaponPanel, BoxLayout.Y_AXIS));
        weaponPanel.add(new JLabel("Pick Weapon:"));
        ButtonGroup weapons = new ButtonGroup();
        for (String weapon : Game.allWeapons) {
            JRadioButton button = new JRadioButton(weapon);
            button.setActionCommand(weapon);
            weapons.add(button);
            weaponPanel.add(button);
        }

        // rooms
        JPanel roomPanel = new JPanel();
        accusePanel.add(roomPanel);
        roomPanel.setLayout(new BoxLayout(roomPanel, BoxLayout.Y_AXIS));
        roomPanel.add(new JLabel("Pick Room:"));
        ButtonGroup rooms = new ButtonGroup();
        for (String room : Game.allRooms) {
            JRadioButton button = new JRadioButton(room);
            button.setActionCommand(room);
            rooms.add(button);
            roomPanel.add(button);
        }

        // get the picked option
        int option = JOptionPane.showConfirmDialog(window, accusePanel, "Pick the circumstances of the murder", JOptionPane.OK_CANCEL_OPTION);

        if (option == 0 && suspects.getSelection() != null && weapons.getSelection() != null && rooms.getSelection() != null) {
            Card suspect = new Card(suspects.getSelection().getActionCommand(), Card.CardType.SUSPECT);
            Card weapon = new Card(weapons.getSelection().getActionCommand(), Card.CardType.WEAPON);
            Card room = new Card(rooms.getSelection().getActionCommand(), Card.CardType.ROOM);

            // Check the accusation
            if (game.checkAccusation(suspect, weapon, room)) {
                showMessage("You guessed correctly! You win the game!");
                backToMenu();
            } else {
                showMessage("You guessed incorrectly! You are no longer in the game!");
                if (game.invalidAccusation(currentPlayer)) {
                    game.nextPlayer();
                } else {
                    showMessage("No one managed to guess the circumstances of the murder correctly so the game is over.");
                    backToMenu();
                }
            }
        }
    }


    /**
     * Open suggest dialog
     */
    public void suggest() {
        // check in correct state
        if (currentPlayer.getCurrentState() == Player.PlayerState.MOVING || currentPlayer.getCurrentState() == Player.PlayerState.NOT_TURN ||
                currentPlayer.getSuspect().getCurrentRoom() == null) return;


        // create panel layouts
        JPanel accusePanel = new JPanel();
        accusePanel.setLayout(new FlowLayout());

        // suspects
        JPanel suspectPanel = new JPanel();
        accusePanel.add(suspectPanel);
        suspectPanel.setLayout(new BoxLayout(suspectPanel, BoxLayout.Y_AXIS));
        suspectPanel.add(new JLabel("Pick Suspect:"));
        ButtonGroup suspects = new ButtonGroup();
        for (String suspect : Game.allSuspects) {
            JRadioButton button = new JRadioButton(suspect);
            button.setActionCommand(suspect);
            suspects.add(button);
            suspectPanel.add(button);
        }

        // weapons
        JPanel weaponPanel = new JPanel();
        accusePanel.add(weaponPanel);
        weaponPanel.setLayout(new BoxLayout(weaponPanel, BoxLayout.Y_AXIS));
        weaponPanel.add(new JLabel("Pick Weapon:"));
        ButtonGroup weapons = new ButtonGroup();
        for (String weapon : Game.allWeapons) {
            JRadioButton button = new JRadioButton(weapon);
            button.setActionCommand(weapon);
            weapons.add(button);
            weaponPanel.add(button);
        }

        // get the picked option
        int option = JOptionPane.showConfirmDialog(window, accusePanel, "Suggest the weapon and suspect for the murder", JOptionPane.OK_CANCEL_OPTION);

        if (option == 0 && suspects.getSelection() != null && weapons.getSelection() != null) {
            Card suspect = new Card(suspects.getSelection().getActionCommand(), Card.CardType.SUSPECT);
            Card weapon = new Card(weapons.getSelection().getActionCommand(), Card.CardType.WEAPON);
            Card room = new Card(currentPlayer.getSuspect().getCurrentRoom().getName(), Card.CardType.ROOM);

            game.checkSuggestion(suspect, weapon, room);
            game.nextPlayer();
        }

    }

    /**
     * Show the dialog that appears when someone has one of the cards that someone suggested
     *
     * @param player
     * @param refutedCards
     */
    public void showRefutationDialog(Player player, List<Card> refutedCards) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(new JLabel(player + ", Pick a card to refute:"));

        ButtonGroup cards = new ButtonGroup();
        boolean first = true;
        for (Card card : refutedCards) {
            JRadioButton button = new JRadioButton(card.toString());
            button.setActionCommand(card.toString());
            panel.add(button);
            cards.add(button);

            if (first) button.setSelected(true);
            first = false;
        }

        JOptionPane.showMessageDialog(window, panel, player + ", choose a card to refute to " + currentPlayer, JOptionPane.QUESTION_MESSAGE);

        showMessage(currentPlayer + ", " + player + " has the " + cards.getSelection().getActionCommand() + " card.");

    }

    /**
     * Swap the current player in the window to the view of another player
     *
     * @param newPlayer
     */
    public void swapPlayer(Player newPlayer) {
        this.currentPlayer = newPlayer;

        window.setTitle(newPlayer.toString());

        // update the cards
        cardPanel.removeAll();
        for (Card card : newPlayer.getCards()) {
            JLabel c = new JLabel(card.toString());
            c.setBorder(new CompoundBorder(BorderFactory.createLineBorder(Color.BLACK), new EmptyBorder(5, 10, 5, 10)));
            cardPanel.add(c);
        }
        cardPanel.repaint();

        repaint();

        window.redraw();
    }

    /**
     * Draw the current state of the board
     */
    public void paint(Graphics g) {
        Board board = game.getBoard();

        for (int y = 0; y < GAME_HEIGHT; y++) {
            for (int x = 0; x < GAME_WIDTH; x++) {
                Cell cell = board.getCell(x, y);
                cell.draw(g, CELL_SIZE);
            }
        }

        // highlight the player
        currentPlayer.getSuspect().getLocation().highlightCell(g, CELL_SIZE, Color.YELLOW);
        currentPlayer.getSuspect().getLocation().outlineCell(g, CELL_SIZE, Color.YELLOW);

        // highlight possible moves
        if (currentPlayer.getCurrentState() == Player.PlayerState.MOVING) {
            List<Cell> cells = currentPlayer.getAvailableCells();

            for(Cell cell : cells) {
                cell.highlightCell(g, CELL_SIZE, new Color(255, 255, 50));
            }
        }
    }

    /**
     * Get the current player
     *
     * @return
     */
    public Player getPlayer() {
        return currentPlayer;
    }

    /**
     * Get the game
     *
     * @return
     */
    public Game getGame() {
        return game;
    }

    /**
     * Show a message dialog
     *
     * @param message
     */
    public void showMessage(String message) {
        JOptionPane.showMessageDialog(window, message);
    }

    /**
     * Get the dice
     *
     * @return
     */
    public Dice getDice() {
        return dice;
    }
}
