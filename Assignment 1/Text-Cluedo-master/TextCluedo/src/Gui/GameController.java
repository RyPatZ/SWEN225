package Gui;

import Game.Cell;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import static java.awt.event.KeyEvent.*;

/**
 * The controller for a game
 * Handles various listeners and key presses
 */
public class GameController implements MouseListener, KeyListener {
    private GameView view;

    /**
     * Init game controller
     *
     * @param view
     */
    public GameController(GameView view) {
        this.view = view;
    }

    /**
     * Click on canvas - move to a given cell
     *
     * @param event
     */
    @Override
    public void mouseClicked(MouseEvent event) {
        if (event.getButton() != MouseEvent.BUTTON1) return;

        int cellX = event.getX() / GameView.CELL_SIZE;
        int cellY = event.getY() / GameView.CELL_SIZE;

        Cell cell = view.getGame().getBoard().getCell(cellX, cellY);

        if (cell != null) {
			view.getPlayer().move(cell);
        }
    }

    /**
     * Key pressed on screen
     * Events should all have safe guards to ensure they only occur during the correct state
     *
     * @param e
     */
    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case VK_W:
            case VK_UP:
                view.getPlayer().move(Cell.Direction.UP);
                break;
            case VK_A:
                view.accuse();
            case VK_LEFT:
                view.getPlayer().move(Cell.Direction.LEFT);
                break;
            case VK_S:
                view.suggest();
            case VK_DOWN:
                view.getPlayer().move(Cell.Direction.DOWN);
                break;
            case VK_D:
            case VK_RIGHT:
                view.getPlayer().move(Cell.Direction.RIGHT);
                break;
            case VK_R:
                view.getPlayer().rollDice(view.getDice());
                break;
            case VK_F:
                view.getPlayer().finishTurn();
                break;
        }
    }

    /**
     * Press on canvas
     *
     * @param event
     */
    @Override
    public void mousePressed(MouseEvent event) {
    }

    /**
     * Release on canvas
     *
     * @param event
     */
    @Override
    public void mouseReleased(MouseEvent event) {
    }

    /**
     * Mouse enter canvas
     *
     * @param event
     */
    @Override
    public void mouseEntered(MouseEvent event) {
    }

    /**
     * Mouse exit canvas
     *
     * @param event
     */
    @Override
    public void mouseExited(MouseEvent event) {
    }

    /**
     * Key typed on screen
     *
     * @param e
     */
    @Override
    public void keyTyped(KeyEvent e) {
    }

    /**
     * Key released on screen
     *
     * @param e
     */
    @Override
    public void keyReleased(KeyEvent e) {
    }
}
