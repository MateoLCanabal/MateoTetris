import java.awt.event.*; 
import java.awt.*; 
import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.sound.sampled.*;

public class MateoTetris implements ActionListener, KeyListener {
	private JFrame gameFrame;
	private JLabel score;
	private JButton toggleAudio;
	private JPanel gameArea;
	private JPanel bottomArea;
	private JPanel nextPieceDisplay;
	private Clip tetrisTheme;
	private boolean playing;
	private int period;
	private int scoreValue;
	
	private Piece currentPiece;
	private Piece nextPiece;
	
	private Accumalation myAccumalation;
	
	private ArrayList<JLabel> gameAreaCubes;
	private ArrayList<JLabel> nextPieceDisplayCubes;
	
	private ImageIcon[] cubeImages;
	private ImageIcon blankImage;
	
	public MateoTetris()
	{
		cubeImages = new ImageIcon[7];
		for (int i = 0; i < 7; i++)
		{
			if (i == 0)
			{
				cubeImages[i] = new ImageIcon("Images/lightblue.png");
			}
			else if (i == 1)
			{
				cubeImages[i] = new ImageIcon("Images/darkblue.png");
			}
			else if (i == 2)
			{
				cubeImages[i] = new ImageIcon("Images/orange.png");
			}
			else if (i == 3)
			{
				cubeImages[i] = new ImageIcon("Images/yellow.png");
			}
			else if (i == 4)
			{
				cubeImages[i] = new ImageIcon("Images/green.png");
			}
			else if (i == 5)
			{
				cubeImages[i] = new ImageIcon("Images/purple.png");
			}
			else
			{
				cubeImages[i] = new ImageIcon("Images/red.png");
			}
		}
		blankImage = new ImageIcon("Images/blank.jpeg");
		scoreValue = 0;
		period = 1000;
		currentPiece = new Piece();
		nextPiece = new Piece();
		myAccumalation = new Accumalation();
		initAudio();
		initCubes();
		prepareGUI();
	}
	
	private int to1D_gameArea(int r, int c)
	{
		return 10*r + c;
	}
	
	private int to1D_nextPieceDisplay(int r, int c)
	{
		return 4*r + c;
	}
	
	private void initCubes()
	{
		gameAreaCubes = new ArrayList<JLabel>();
		for (int i = 0; i < 10*40; i++)
		{
			gameAreaCubes.add(new JLabel(new ImageIcon("Images/blank.jpeg")));
		}
		
		nextPieceDisplayCubes = new ArrayList<JLabel>();
		for (int i = 0; i < 4*2; i++)
		{
			nextPieceDisplayCubes.add(new JLabel(new ImageIcon("Images/blank.jpeg")));
		}
	}
	
	private void initAudio()
	{
		try {
			tetrisTheme = AudioSystem.getClip();
			AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File("Audio/tetristheme.wav"));
			tetrisTheme.open(inputStream);
			tetrisTheme.loop(Clip.LOOP_CONTINUOUSLY);
			playing = true;
		} catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void prepareGUI()
	{
		gameFrame = new JFrame("Mateo Tetris");
		gameFrame.setSize(150, 660);
		
		score = new JLabel("Score: " + scoreValue);
		score.setSize(50, 60);
		score.setHorizontalAlignment(JLabel.CENTER);
		
		toggleAudio = new JButton(new ImageIcon("Images/mute.png"));
		toggleAudio.setSize(50, 60);
		toggleAudio.addActionListener(this);
		
		gameArea = new JPanel();
		gameArea.setSize(150, 600);
		gameArea.setLayout(new GridLayout(40, 10));
		for (int r = 0; r < 40; r++)
		{
			for (int c = 0; c < 10; c++)
			{
				gameArea.add(gameAreaCubes.get(to1D_gameArea(r, c)));
			}
		}
		gameArea.validate();
		
		nextPieceDisplay = new JPanel();
		nextPieceDisplay.setSize(50, 60);
		nextPieceDisplay.setLayout(new GridLayout(2, 4));
		for (int r = 0; r < 2; r++)
		{
			for (int c = 0; c < 4; c++)
			{
				nextPieceDisplay.add(nextPieceDisplayCubes.get(to1D_nextPieceDisplay(r, c)));
			}
		}
		nextPieceDisplay.validate();
		
		gameFrame.setLayout(new GridLayout(1, 2));
		gameFrame.add(gameArea);
		
		bottomArea = new JPanel();
		bottomArea.setSize(150, 60);
		bottomArea.setLayout(new GridLayout(18, 1));
		bottomArea.add(toggleAudio);
		bottomArea.add(score);
		bottomArea.add(nextPieceDisplay);
		bottomArea.validate();
		
		gameFrame.add(bottomArea);
	}
	
	private void display()
	{
		gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gameFrame.validate();
		gameFrame.pack();
		gameFrame.setResizable(false);
		gameFrame.setVisible(true);
	}
	
	private void displayNext()
	{
		boolean[][] occupy = nextPiece.getOccupy();
		for (int r = 0; r < occupy.length; r++)
		{
			for (int c = 0; c < occupy[0].length; c++)
			{
				if (occupy[r][c])
				{
					nextPieceDisplayCubes.get(to1D_nextPieceDisplay(r, c)).setIcon(cubeImages[nextPiece.getType()]);
				}
				else
				{
					nextPieceDisplayCubes.get(to1D_nextPieceDisplay(r, c)).setIcon(blankImage);
				}
			}
		}
		
	}
	
	private void displayCurrent()
	{
		boolean[][] occupy = currentPiece.getOccupy();
		for (int r = 0; r < occupy.length; r++)
		{
			for (int c = 0; c < occupy[0].length; c++)
			{
				if (c + currentPiece.getCol() < 0 || c + currentPiece.getCol() > 9 || r + currentPiece.getRow() < 0 || r + currentPiece.getRow() > 39)
					continue;
				if (occupy[r][c])
				{
					gameAreaCubes.get(to1D_gameArea(r + currentPiece.getRow(), c + currentPiece.getCol())).setIcon(cubeImages[currentPiece.getType()]);
				}
				else
				{
					gameAreaCubes.get(to1D_gameArea(r + currentPiece.getRow(), c + currentPiece.getCol())).setIcon(blankImage);
				}
			}
		}
	}
	
	private void clear(int row, int col, boolean[][] occupy)
	{
		for (int r = 0; r < occupy.length; r++)
		{
			for (int c = 0; c < occupy[0].length; c++)
			{
				if (occupy[r][c])
				{
					gameAreaCubes.get(to1D_gameArea(r + row, c + col)).setIcon(blankImage);
				}
			}
		}	
	}
	
	private void displayAccumalation()
	{
		for (int r = 0; r < 40; r++)
		{
			for (int c = 0; c < 10; c++)
			{
				if (myAccumalation.getOccupy()[r][c] == -1)
				{
					gameAreaCubes.get(to1D_gameArea(r, c)).setIcon(blankImage);
				}
				else
				{
					gameAreaCubes.get(to1D_gameArea(r, c)).setIcon(cubeImages[myAccumalation.getOccupy()[r][c]]);
				}
			}
		}
	}
	
	private boolean validRotation()
	{
		Piece testPiece = new Piece(currentPiece);
		testPiece.Rotate();
		boolean[][] testPieceOccupy = testPiece.getOccupy();
		for (int r = 0; r < testPieceOccupy.length; r++)
		{
			for (int c = 0; c < testPieceOccupy[0].length; c++)
			{
				if (!testPieceOccupy[r][c])
					continue;
				if (c + testPiece.getCol() < 0 || c + testPiece.getCol() > 9 || myAccumalation.getOccupy()[r + testPiece.getRow()][c + testPiece.getCol()] != -1)
					return false;
			}
		}
		return true;
	}
	
	private boolean validMove(int deltaC)
	{
		boolean[][] currentPieceOccupy = currentPiece.getOccupy();
		for (int r = 0; r < currentPieceOccupy.length; r++)
		{
			for (int c = 0; c < currentPieceOccupy[0].length; c++)
			{
				if (!currentPieceOccupy[r][c])
					continue;
				if (c + currentPiece.getCol() + deltaC < 0 || c + currentPiece.getCol() + deltaC > 9 || myAccumalation.getOccupy()[r + currentPiece.getRow()][c + currentPiece.getCol() + deltaC] != -1)
					return false;
			}
		}
		return true;
	}
	
	private boolean hasCollided()
	{
		boolean[][] currentPieceOccupy = currentPiece.getOccupy();
		for (int r = 0; r < currentPieceOccupy.length; r++)
		{
			for (int c = 0; c < currentPieceOccupy[0].length; c++)
			{
				if (!currentPieceOccupy[r][c])
					continue;
				if (r + currentPiece.getRow() + 1 > 39)
					return true;
				if (myAccumalation.getOccupy()[r + currentPiece.getRow() + 1][c + currentPiece.getCol()] != -1)
					return true;
			}
		}
		return false;
	}
	
	private boolean gameOver()
	{
		boolean over = false;
		for (int c = 0; c < 10; c++)
		{
			over = over || (myAccumalation.getOccupy()[0][c] != -1);
		}
		return over;
	}
	
	private void GamePlaySequence()
	{
		gameFrame.setFocusable(true);
		gameFrame.addKeyListener(this);
		
		while (true)
		{
			if (gameOver())
			{
				new GameOverWindow(gameFrame, true, scoreValue);
				myAccumalation = new Accumalation();
				scoreValue = 0;
				score.setText("Score: " + scoreValue);
			}
			currentPiece = nextPiece;
			nextPiece = new Piece();
			displayNext();
			displayCurrent();
			displayAccumalation();
			boolean first = true;
			while (true)
			{
				try {
					TimeUnit.MILLISECONDS.sleep(period);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (!first)
				{
					clear(currentPiece.getRow(), currentPiece.getCol(), currentPiece.getOccupy());
					currentPiece.MoveDown(1);
				}
				first = false;
				displayCurrent();
				if (hasCollided())
				{
					myAccumalation.receievePiece(currentPiece);
					scoreValue += myAccumalation.clearedLines();
					score.setText("Score: " + scoreValue);
					break;
				}
			}
		}
	}
	
	public void actionPerformed(ActionEvent e)
	{
		gameFrame.requestFocus();
		if (playing)
		{
			playing = false;
			tetrisTheme.stop();
			toggleAudio.setIcon(new ImageIcon("Images/unmute.png"));
		}
		else
		{
			playing = true;
			tetrisTheme.loop(Clip.LOOP_CONTINUOUSLY);
			toggleAudio.setIcon(new ImageIcon("Images/mute.png"));
		}
	}
	
	public static void main(String[] args) {
		MateoTetris runner = new MateoTetris();
		runner.display();
		runner.GamePlaySequence();
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_A && validMove(-1))
		{
			clear(currentPiece.getRow(), currentPiece.getCol(), currentPiece.getOccupy());
			currentPiece.MoveLeft();
			displayCurrent();
		}
		else if (e.getKeyCode() == KeyEvent.VK_D && validMove(1))
		{
			clear(currentPiece.getRow(), currentPiece.getCol(), currentPiece.getOccupy());
			currentPiece.MoveRight();
			displayCurrent();
		}
		else if (e.getKeyCode() == KeyEvent.VK_W && validRotation())
		{
			clear(currentPiece.getRow(), currentPiece.getCol(), currentPiece.getOccupy());
			currentPiece.Rotate();
			displayCurrent();
		}
		else if (e.getKeyCode() == KeyEvent.VK_S)
		{
			period = 50;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_S)
		{
			period = 1000;
		}
	}

}
