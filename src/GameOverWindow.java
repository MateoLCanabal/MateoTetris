import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class GameOverWindow extends JDialog implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private LinkedHashMap<String, Integer> highScores;
	private JLabel gameOver;
	private JLabel enterName;
	private JLabel listOfScores;
	private JButton submit;
	private JTextField name;
	private ObjectOutputStream highScoresObjectOut;
	private ObjectInputStream highScoresObjectIn;
	private FileInputStream highScoresFileIn;
	private FileOutputStream highScoresFileOut;
	private int scoreVal;
	
	@SuppressWarnings("unchecked")
	public GameOverWindow(JFrame parent, boolean modal, int scoreVal)
	{
		super(parent, modal);
		try {
			highScoresFileIn = new FileInputStream("Records/highscores.ser");
			highScoresObjectIn = new ObjectInputStream(highScoresFileIn);
			highScores = (LinkedHashMap<String, Integer>) highScoresObjectIn.readObject();
			highScoresObjectIn.close();
			highScoresFileIn.close();
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			highScores = new LinkedHashMap<String, Integer>();
		}
		gameOver = new JLabel("GAME OVER! Score = " + scoreVal);
		enterName = new JLabel("Please enter your name: ");
		name = new JTextField();
		submit = new JButton("Submit");
		submit.addActionListener(this);
		listOfScores = new JLabel(generateNameList());
		this.scoreVal = scoreVal;
		this.setLayout(new GridLayout(3, 1));
		JPanel middlePanel = new JPanel();
		middlePanel.setLayout(new GridLayout(3, 1));
		middlePanel.add(enterName);
		middlePanel.add(name);
		middlePanel.add(submit);
		this.add(gameOver);
		this.add(middlePanel);
		this.add(listOfScores);
		this.pack();
		this.setTitle("GAME OVER");
		this.setAlwaysOnTop(true);
		this.setResizable(false);
		this.setVisible(true);
	}
	
	private String generateNameList()
	{
		if (highScores.isEmpty())
			return "...";
		String output = "<html>";
		Set<String> keys = highScores.keySet();
		ArrayList<Integer> values = new ArrayList<Integer>();
		values.addAll(highScores.values());
		Collections.sort(values, Comparator.reverseOrder());
		ArrayList<String> printedNames = new ArrayList<String>();
		int i = 1;
		for (int value : values)
		{
			for (String key : keys)
			{
				if (highScores.get(key) == value && !printedNames.contains(key))
				{
					output += i + ". " + key + ": " + value + "<br/>";
					i++;
					printedNames.add(key);
				}
			}
		}
		return output + "</html>";
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (highScores.containsKey(name.getText()))
		{
			if (highScores.get(name.getText()) < scoreVal)
				highScores.put(name.getText(), scoreVal);
		}
		else
			highScores.put(name.getText(), scoreVal);
		try {
			highScoresFileOut = new FileOutputStream("Records/highscores.ser");
			highScoresObjectOut = new ObjectOutputStream(highScoresFileOut);
			highScoresObjectOut.writeObject(highScores);
			highScoresObjectOut.close();
			highScoresFileOut.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		this.setVisible(false);
		this.dispose();
	}
}
