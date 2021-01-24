import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.text.DefaultCaret;

/**
 * This is a template GUI that you can use for your mapping program. It is an
 * *abstract class*, which means you'll need to extend it in your own program.
 * For a simple example of how to do this, have a look at the SquaresExample
 * class.
 * 
 * This GUI uses Swing, not the first-year UI library. Swing is not the focus of
 * this course, but it would be to your benefit if you took some time to
 * understand how this class works.
 * 
 * @author tony
 */
public abstract class GUI {
	

	/**
	 * Is called when the drawing area is redrawn and performs all the logic for
	 * the actual drawing, which is done with the passed Graphics object.
	 */
	protected abstract void redraw(Graphics g);

	/**
	 * @return the JTextArea at the bottom of the screen for output.
	 */
	public JTextArea getTextOutputArea() {
		return textOutputArea;
	}

	/**
	 * @return the dimensions of the drawing area.
	 */
	public Dimension getDrawingAreaDimension() {
		return drawing.getSize();
	}

	/**
	 * Redraws the window (including drawing pane). This is already done
	 * whenever a button is pressed or the search box is updated, so you
	 * probably won't need to call this.
	 */
	public void redraw() {
		frame.repaint();
	}
	
	/**
	 * Creates a dialog box asking the user for the amount of players
	 * @return Number of players
	 */
	public static int getNumPlayers() {
		Object[] choice = {"3", "4", "5", "6"};
		String s = (String)JOptionPane.showInputDialog(frame, "How many players?", "...", JOptionPane.PLAIN_MESSAGE, null, choice, "3");
		if((s != null) && (s.length() > 0)) {
			int num = Integer.parseInt(s);
			return num;
		}
		return -1;
	}
	
	/**
	 * Creates a dialog box asking for all the players names
	 * @param text
	 * @return gets the players name
	 */
	public static String getPlayerName(String text) {
		Object[] choice = null;
		String s = (String)JOptionPane.showInputDialog(frame, text+"Choose your player name", "...", JOptionPane.PLAIN_MESSAGE, null, choice, "3");
		if((s != null) && (s.length() > 0)) {
			return s;
		}
		return null; //Dead code
	}
	
	
	/**
	 * Creates a dialog box asking the user for what character they want to be
	 * @param text
	 * @return Character choice
	 */
	public static int getCharacterChoice(String text) {
		Object[] choice = {"Miss Scarlett", "Colonel Mustard", "Mrs. White", "Mr. Green", "Mrs. Peacock", "Professor Plum"};
		String s = (String)JOptionPane.showInputDialog(frame, text+"Which character would you like?", "...", JOptionPane.PLAIN_MESSAGE, null, choice, "3");
		if((s != null) && (s.length() > 0)) {
			switch(s) {
			case "Miss Scarlett": return 0;
			case "Colonel Mustard": return 1;
			case "Mrs. White": return 2;
			case "Mr. Green": return 3;
			case "Mrs. Peacock": return 4;
			case "Professor Plum": return 5;
			}
		}
		return -1; //Dead code
	}
	
	/**
	 * 
	 * @param text
	 * @return The choice of character chosen for accusation
	 */
	public static int character(String text) {
		Object[] choice = {"Miss Scarlett", "Colonel Mustard", "Mrs. White", "Mr. Green", "Mrs. Peacock", "Professor Plum"};
		String s = (String)JOptionPane.showInputDialog(frame, text+"Which character would you like to choose?", "...", JOptionPane.PLAIN_MESSAGE, null, choice, "3");
		if((s != null) && (s.length() > 0)) {
			switch(s) {
			case "Miss Scarlett": return 0;
			case "Colonel Mustard": return 1;
			case "Mrs. White": return 2;
			case "Mr. Green": return 3;
			case "Mrs. Peacock": return 4;
			case "Professor Plum": return 5;
			}
		}
		return -1; //Dead code
	}
	
	/**
	 * 
	 * @param text
	 * @return The choice of weapon chosen for accusation and suggestions
	 */
	public static int weapon(String text) {
		Object[] choice = {"Candlestick", "Dagger", "Lead Pipe", "Revolver", "Rope", "Spanner"};
		String s = (String)JOptionPane.showInputDialog(frame, text+"Which weapon would you like to choose?", "...", JOptionPane.PLAIN_MESSAGE, null, choice, "3");
		if((s != null) && (s.length() > 0)) {
			switch(s) {
			case "Candlestick": return 0;
			case "Dagger": return 1;
			case "Lead Pipe": return 2;
			case "Revolver": return 3;
			case "Rope": return 4;
			case "Spanner": return 5;
			}
		}
		return -1; //Dead code
	}
	
	/**
	 * 
	 * @param text
	 * @return The choice of room chosen for accusation and suggestions
	 */
	public static int room(String text) {
		Object[] choice = {"Kitchen", "Ball Room", "Conservatory", "Billiard Room", "Library", "Study", "Dining Room", "Hall", "Lounge"};
		String s = (String)JOptionPane.showInputDialog(frame, text+"Which room would you like to choose?", "...", JOptionPane.PLAIN_MESSAGE, null, choice, "3");
		if((s != null) && (s.length() > 0)) {
			switch(s) {
			case "Kitchen": return 0;
			case "Ball Room": return 1;
			case "Conservatory": return 2;
			case "Billiard Room": return 3;
			case "Library": return 4;
			case "Study": return 5;
			case "Dining Room": return 6;
			case "Hall": return 7;
			case "Lounge": return 8;
			}
		}
		return -1; //Dead code
	}



	private static final int DEFAULT_DRAWING_HEIGHT = 400;
	private static final int DEFAULT_DRAWING_WIDTH = 400;
	private static final int TEXT_OUTPUT_ROWS = 5;
	private static final int SEARCH_COLS = 15;



	private static JFrame frame;

	private JPanel controls;
	private JComponent drawing; 
	private JTextArea textOutputArea;

	private JTextField search;

	public GUI() {
		initialise();
	}

	@SuppressWarnings("serial")
	private void initialise() {

		JButton quit = new JButton("Quit");
		quit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				System.exit(0); 
			}
		});

		JButton west = new JButton("\u2190");
		west.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				Board.setInp("a");
				redraw();
			}
		});

		JButton east = new JButton("\u2192");
		east.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				Board.setInp("d");
				redraw();
			}
		});

		JButton north = new JButton("\u2191");
		north.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				Board.setInp("w");
				redraw();
			}
		});

		JButton south = new JButton("\u2193");
		south.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				Board.setInp("s");
				redraw();
			}
		});
		
		JButton accuse = new JButton("Accuse");
		accuse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				Board.setInp("c");
				redraw();
			}
		});
		
		JButton suggest = new JButton("Suggest");
		suggest.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				Board.setInp("g");
				redraw();
			}
		});
		
		JButton end = new JButton("End Turn");
		end.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				Board.setInp("e");
				redraw();
			}
		});
		
		JButton hand = new JButton("Hand");
		hand.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				Board.setInp("h");
				redraw();
			}
		});

		controls = new JPanel();
		controls.setLayout(new BoxLayout(controls, BoxLayout.LINE_AXIS));


		Border edge = BorderFactory.createEmptyBorder(5, 5, 5, 5);
		controls.setBorder(edge);

		JPanel loadquit = new JPanel();
		loadquit.setLayout(new GridLayout(2, 1));

		loadquit.setMaximumSize(new Dimension(50, 100));
		loadquit.add(quit);
		controls.add(loadquit);

		controls.add(Box.createRigidArea(new Dimension(15, 0)));

		JPanel navigation = new JPanel();
		navigation.setMaximumSize(new Dimension(150, 60));
		navigation.setLayout(new GridLayout(2, 3));
		navigation.add(suggest);
		navigation.add(north);
		navigation.add(accuse);
		navigation.add(end);
		navigation.add(west);
		navigation.add(south);
		navigation.add(east);
		navigation.add(hand);
		controls.add(navigation);
		controls.add(Box.createRigidArea(new Dimension(15, 0)));
		
		controls.add(Box.createHorizontalGlue());

		drawing = new JComponent() {
			protected void paintComponent(Graphics g) {
				redraw(g);
			}
		};
		drawing.setPreferredSize(new Dimension(DEFAULT_DRAWING_WIDTH,
				DEFAULT_DRAWING_HEIGHT));

		drawing.setVisible(true);

		drawing.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				
				redraw();
			}
		});

		drawing.addMouseWheelListener(new MouseAdapter() {
			public void mouseWheelMoved(MouseWheelEvent e) {
			}
		});
		


		textOutputArea = new JTextArea(TEXT_OUTPUT_ROWS, 0);
		textOutputArea.setLineWrap(true);
		textOutputArea.setWrapStyleWord(true); 
		textOutputArea.setEditable(false);
		JScrollPane scroll = new JScrollPane(textOutputArea);
	
		DefaultCaret caret = (DefaultCaret) textOutputArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);



		JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		split.setDividerSize(5); 
		split.setContinuousLayout(true); 
		split.setResizeWeight(1); 

		split.setBorder(BorderFactory.createEmptyBorder());
		split.setTopComponent(drawing);
		split.setBottomComponent(scroll);
		
		frame = new JFrame("Cluedo");

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.add(controls, BorderLayout.NORTH);
		frame.add(split, BorderLayout.CENTER);


		frame.pack();
		frame.setVisible(true);
	}
}
