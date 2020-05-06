import java.util.Random;


public class Piece {
	private int currentRow;		// top left corner
	private int currentColumn;	// top left corner
	
	private int type;			// 0-6
	private int orientation;	// 0-3, each signify a 90 degree CW rotation
	
	private boolean[][] occupy;
	
	private void generateOccupy()
	{
		occupy = new boolean[2][4];
		if (type == 0)
		{
			for (int r = 0; r < 2; r++)
			{
				for (int c = 0; c < 4; c++)
				{
					if (r == 0)
					{
						occupy[r][c] = false;
					}
					else
					{
						occupy[r][c] = true;
					}
				}
			}
		}
		else if (type == 1)
		{
			for (int r = 0; r < 2; r++)
			{
				for (int c = 0; c < 4; c++)
				{
					if (r == 0 && c != 0 || c == 3)
					{
						occupy[r][c] = false;
					}
					else
					{
						occupy[r][c] = true;
					}
				}
			}
		}
		else if (type == 2)
		{
			for (int r = 0; r < 2; r++)
			{
				for (int c = 0; c < 4; c++)
				{
					if (r == 0 && c != 3 || c == 0)
					{
						occupy[r][c] = false;
					}
					else
					{
						occupy[r][c] = true;
					}
				}
			}
		}
		else if (type == 3)
		{
			for (int r = 0; r < 2; r++)
			{
				for (int c = 0; c < 4; c++)
				{
					if (c == 0 || c == 3)
					{
						occupy[r][c] = false;
					}
					else
					{
						occupy[r][c] = true;
					}
				}
			}
		}
		else if (type == 4)
		{
			for (int r = 0; r < 2; r++)
			{
				for (int c = 0; c < 4; c++)
				{
					if (c == 0 && r == 0 || c == 2 && r == 1 || c == 3)
					{
						occupy[r][c] = false;
					}
					else
					{
						occupy[r][c] = true;
					}
				}
			}
		}
		else if (type == 5)
		{
			for (int r = 0; r < 2; r++)
			{
				for (int c = 0; c < 4; c++)
				{
					if (c == 0 && r == 0 || c == 2 && r == 0 || c == 3)
					{
						occupy[r][c] = false;
					}
					else
					{
						occupy[r][c] = true;
					}
				}
			}
		}
		else if (type == 6)
		{
			for (int r = 0; r < 2; r++)
			{
				for (int c = 0; c < 4; c++)
				{
					if (c == 0 && r == 1 || c == 2 && r == 0 || c == 3)
					{
						occupy[r][c] = false;
					}
					else
					{
						occupy[r][c] = true;
					}
				}
			}
		}
	}
	
	public int getType()
	{
		return type;
	}
	
	public int getRow()
	{
		return currentRow;
	}
	
	public int getCol()
	{
		return currentColumn;
	}
	
	public void MoveLeft()
	{
		currentColumn--;
	}
	
	public void MoveRight()
	{
		currentColumn++;
	}
	
	public void MoveDown(int rate)
	{
		currentRow += rate;
	}
	
	public void Rotate()
	{
		orientation = (orientation + 1) % 4;
	}
	
	public boolean[][] getOccupy()
	{
		if (orientation == 0)
		{
			return occupy;
		}
		else if (orientation == 1)
		{
			boolean[][] occupy_1 = new boolean[4][2];
			for (int r = 0; r < 4; r++)
			{
				for (int c = 0; c < 2; c++)
				{
					occupy_1[r][c] = occupy[1-c][r];
				}
			}
			return occupy_1;
		}
		else if (orientation == 2)
		{
			boolean[][] occupy_2 = new boolean[2][4];
			for (int r = 0; r < 2; r++)
			{
				for (int c = 0; c < 4; c++)
				{
					occupy_2[r][c] = occupy[1-r][3-c];
				}
			}
			return occupy_2;
		}
		else
		{
			boolean[][] occupy_3 = new boolean[4][2];
			for (int r = 0; r < 4; r++)
			{
				for (int c = 0; c < 2; c++)
				{
					occupy_3[r][c] = occupy[c][(3-r)];
				}
			}
			return occupy_3;
		}
	}
	
	public Piece(Piece p)
	{
		this.currentColumn = p.currentColumn;
		this.currentRow = p.currentRow;
		this.occupy = p.occupy;
		this.orientation = p.orientation;
		this.type = p.type;
	}
	
	public Piece()
	{
		orientation = 0;
		currentRow = 0;
		currentColumn = 3;
		type = new Random().nextInt(7);
		generateOccupy();
	}

}
