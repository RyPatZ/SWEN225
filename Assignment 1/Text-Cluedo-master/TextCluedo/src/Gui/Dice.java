package Gui;

import java.awt.*;

/**
 * Represents a pair of dice on the GUI
 */
public class Dice extends Canvas {
    private int num1 = 6, num2 = 6;
    private int size;

    /**
     * Initialize the dice with a given size
     *
     * @param size
     */
    public Dice(int size) {
        this.size = size;
        setSize((int) (size * 2.25), size);
    }

    /**
     * Change the number on the dice
     */
    public void roll() {
        num1 = (int) (Math.random() * 6) + 1;
        num2 = (int) (Math.random() * 6) + 1;
        repaint();
    }

    /**
     * Get the value from the dice
     *
     * @return
     */
    public int getValue() {
        return num1 + num2;
    }

    /**
     * Draw the dice
     *
     * @param g
     */
    @Override
    public void paint(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, size, size);
        g.fillRect((int) (size * 1.25), 0, size, size);

        g.setColor(Color.BLACK);

        drawDots(g, num1, 0, 0);
        drawDots(g, num2, (int) (size * 1.25), 0);

        g.drawRect(0, 0, size - 1, size - 1);
        g.drawRect((int) (size * 1.25), 0, size - 1, size - 1);

        g.dispose();
    }

    /**
     * Draw dots at the given position for the given number
     *
     * @param g
     * @param num
     * @param x
     * @param y
     */
    public void drawDots(Graphics g, int num, int x, int y) {
        int dotSize = size / 4;

        switch (num) {
            case 1:
                g.fillOval(x + size / 2 - dotSize / 2, y + size / 2 - dotSize / 2, dotSize, dotSize);
                break;
            case 2:
                g.fillOval(x + size / 4 - dotSize / 2, y + (size * 3) / 4 - dotSize / 2, dotSize, dotSize);
                g.fillOval(x + (size * 3) / 4 - dotSize / 2, y + size / 4 - dotSize / 2, dotSize, dotSize);
                break;
            case 3:
                g.fillOval(x + size / 4 - dotSize / 2, y + (size * 3) / 4 - dotSize / 2, dotSize, dotSize);
                g.fillOval(x + size / 2 - dotSize / 2, y + size / 2 - dotSize / 2, dotSize, dotSize);
                g.fillOval(x + (size * 3) / 4 - dotSize / 2, y + size / 4 - dotSize / 2, dotSize, dotSize);
                break;
            case 4:
                g.fillOval(x + size / 4 - dotSize / 2, y + (size * 3) / 4 - dotSize / 2, dotSize, dotSize);
                g.fillOval(x + size / 4 - dotSize / 2, y + size / 4 - dotSize / 2, dotSize, dotSize);
                g.fillOval(x + (size * 3) / 4 - dotSize / 2, y + size / 4 - dotSize / 2, dotSize, dotSize);
                g.fillOval(x + (size * 3) / 4 - dotSize / 2, y + (size * 3) / 4 - dotSize / 2, dotSize, dotSize);
                break;
            case 5:
                g.fillOval(x + size / 4 - dotSize / 2, y + (size * 3) / 4 - dotSize / 2, dotSize, dotSize);
                g.fillOval(x + size / 4 - dotSize / 2, y + size / 4 - dotSize / 2, dotSize, dotSize);
                g.fillOval(x + (size * 3) / 4 - dotSize / 2, y + size / 4 - dotSize / 2, dotSize, dotSize);
                g.fillOval(x + (size * 3) / 4 - dotSize / 2, y + (size * 3) / 4 - dotSize / 2, dotSize, dotSize);
                g.fillOval(x + size / 2 - dotSize / 2, y + size / 2 - dotSize / 2, dotSize, dotSize);
                break;
            case 6:
                g.fillOval(x + size / 3 - dotSize / 2, y + size / 4 - dotSize / 2, dotSize, dotSize);
                g.fillOval(x + size / 3 - dotSize / 2, y + size / 2 - dotSize / 2, dotSize, dotSize);
                g.fillOval(x + size / 3 - dotSize / 2, y + (size * 3) / 4 - dotSize / 2, dotSize, dotSize);
                g.fillOval(x + (size * 2) / 3 - dotSize / 2, y + size / 4 - dotSize / 2, dotSize, dotSize);
                g.fillOval(x + (size * 2) / 3 - dotSize / 2, y + size / 2 - dotSize / 2, dotSize, dotSize);
                g.fillOval(x + (size * 2) / 3 - dotSize / 2, y + (size * 3) / 4 - dotSize / 2, dotSize, dotSize);
                break;
        }
    }

}
