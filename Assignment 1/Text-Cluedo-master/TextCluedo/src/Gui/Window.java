package Gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * Represents a window.
 * Made from a JFrame with a JPanel containing the whole
 */
public class Window extends JPanel implements WindowListener {
    private JFrame frame;

    /**
     * Create a window
     *
     * @param name   Title of the JFrame
     * @param width  width of the window
     * @param height height of the window
     */
    public Window(String name, int width, int height) {
        frame = new JFrame(name);
        frame.setSize(new Dimension(width, height));
        frame.setLayout(new BorderLayout());
        frame.setResizable(false);
        frame.addWindowListener(this);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setVisible(true);

        frame.add(this);
    }

    /**
     * Update the window
     */
    public void redraw() {
        repaint();
        frame.revalidate();
    }


    /**
     * Width of the window
     *
     * @return
     */
    public int getWidth() {
        return frame.getWidth();
    }

    /**
     * Height of the window
     *
     * @return
     */
    public int getHeight() {
        return frame.getHeight();
    }

    /**
     * Ask for confirmation to close the window and close if confirmed
     */
    public void close() {
        int option = JOptionPane.showConfirmDialog(this, "Are you sure you want to exit?", "Exit confirmation", JOptionPane.OK_CANCEL_OPTION);
        if (option == 0)
            frame.dispose();
    }

    /**
     * Change the title of the window
     *
     * @param title
     */
    public void setTitle(String title) {
        frame.setTitle(title);
    }

    /**
     * Set the menu bar
     *
     * @param menuBar
     */
    public void setMenuBar(JMenuBar menuBar) {
        frame.setJMenuBar(menuBar);
    }


    /**
     * When you try to close the window
     *
     * @param e
     */
    @Override
    public void windowClosing(WindowEvent e) {
        close();
    }

    /**
     * Unused
     *
     * @param e
     */
    @Override
    public void windowClosed(WindowEvent e) {
    }

    /**
     * Unused
     *
     * @param e
     */
    @Override
    public void windowOpened(WindowEvent e) {
    }

    /**
     * Unused
     *
     * @param e
     */
    @Override
    public void windowIconified(WindowEvent e) {
    }

    /**
     * Unused
     *
     * @param e
     */
    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    /**
     * Unused
     *
     * @param e
     */
    @Override
    public void windowActivated(WindowEvent e) {
    }

    /**
     * Unused
     *
     * @param e
     */
    @Override
    public void windowDeactivated(WindowEvent e) {
    }
}
